package com.holkiew.yomenik.battlesim.ship.travel;

import com.google.common.collect.Lists;
import com.holkiew.yomenik.battlesim.common.ReactorWorker;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.battle.BattleStage;
import com.holkiew.yomenik.battlesim.ship.battlesimulator.dto.NewBattleRequest;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import com.holkiew.yomenik.battlesim.ship.travel.port.BattleHistoryPort;
import com.holkiew.yomenik.battlesim.ship.travel.port.FleetRepository;
import com.holkiew.yomenik.battlesim.ship.travel.port.PlanetPort;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;


@RequiredArgsConstructor
@Log4j2
@Component
public class TravelMissionWorker implements ReactorWorker {

    private final FleetRepository fleetRepository;
    private final PlanetPort planetService;
    private final BattleHistoryPort battleService;

    @Override
    public Flux<?> getUpstream() {
        return Flux.interval(Duration.ofSeconds(1L))
                .flatMap(l -> fleetRepository.findAllByArrivalTimeBeforeAndMissionCompletedFalse(LocalDateTime.now()))
                .flatMap(this::getFleetTargetPlanets)
                .flatMap(this::executeMission)
                .flatMap(this::cleanupExecutedMissions);
    }

    @Override
    public Consumer<Throwable> doOnError() {
        return log::error;
    }

    @Override
    public Runnable doOnTerminate() {
        return () -> log.error("Worker " + this.getClass().getSimpleName() + " terminated");
    }

    @Override
    public Consumer<Subscription> doOnSubscribe() {
        return subscription -> log.info("Worker " + this.getClass().getSimpleName() + " initialized");
    }

    private Mono<FleetTargetPlanets> getFleetTargetPlanets(Fleet fleet) {
        return planetService.findById(fleet.getPlanetIdFrom())
                .zipWith(planetService.findById(fleet.getPlanetIdTo()))
                .map(tuple -> new FleetTargetPlanets(tuple.getT1(), tuple.getT2(), fleet));
    }

    private Mono<FleetTargetPlanets> executeMission(FleetTargetPlanets ftp) {
        ftp.fleet.setMissionCompleted(true);
        switch (ftp.fleet.getMissionType()) {
            case MOVE:
            case TRANSFER_BACK:
                addFleetToTargetPlanet(ftp);
                break;
            case ATTACK:
                return getFleetDuringBattle(ftp)
                        .flatMap(fleetRepository::save).thenReturn(ftp);
            case ATTACK_BATTLE:
                return manageAfterBattleResultsAndGetTransferBackFleet(ftp)
                        .flatMap(fleetRepository::save).thenReturn(ftp);
            case TRANSFER:
                Fleet transferFleet = getFleetOnReturnMission(ftp);
                return fleetRepository.save(transferFleet).thenReturn(ftp);
        }
        return Mono.just(ftp);
    }

    private Mono<Fleet> getFleetDuringBattle(FleetTargetPlanets ftp) {
        return battleService.newBattle(new NewBattleRequest(ftp.fleet.getShips(), ftp.planetFrom.getUserId(), ftp.planetTo.getResidingFleet(), ftp.planetTo.getUserId()))
                .map(battleHistory -> {
                    ftp.planetTo.setDuringBattle(true);
                    var fleetDuringBattle = new Fleet(ftp.fleet.getShips(), battleHistory.getId());
                    fleetDuringBattle.setRouteOnPlanets(ftp.planetTo, ftp.planetFrom, battleHistory.getEndDate(), TravelMissonType.ATTACK_BATTLE, ftp.fleet.getId());
                    return fleetDuringBattle;
                });
    }

    private Mono<Fleet> manageAfterBattleResultsAndGetTransferBackFleet(FleetTargetPlanets ftp) {
        return battleService.findById(ftp.fleet.getRelatedBattleHistoryId())
                .flatMap(battleHistory -> {
                    var lastStageRecap = battleHistory.getBattleRecapMap().get(BattleStage.END);
                    var armyPlanetTo = lastStageRecap.getArmy2Recap().getShips();
                    ftp.planetTo.setResidingFleet(armyPlanetTo);
                    ftp.planetTo.setDuringBattle(false);
                    var fleet = lastStageRecap.getArmy1Recap().getShips();
                    return fleet.isEmpty() ? Mono.empty() : Mono.just(getFleetOnReturnMission(ftp, fleet));
                });
    }

    private Fleet getFleetOnReturnMission(FleetTargetPlanets ftp) {
        return getFleetOnReturnMission(ftp, ftp.fleet.getShips());
    }

    private Fleet getFleetOnReturnMission(FleetTargetPlanets ftp, Map<String, Long> returningShips) {
        var transferBackFleet = new Fleet(returningShips);
        Duration flightDuration = Duration.between(ftp.fleet.getDepartureTime(), ftp.fleet.getArrivalTime());
        var relatedIds = Lists.newArrayList(ftp.fleet.getRelatedBattleHistoryId());
        relatedIds.add(ftp.fleet.getId());
        transferBackFleet.setRouteOnPlanets(ftp.planetFrom, ftp.planetTo, ftp.fleet.getArrivalTime().plusSeconds(flightDuration.toSeconds()), TravelMissonType.TRANSFER_BACK, relatedIds);
        return transferBackFleet;
    }

    private void addFleetToTargetPlanet(FleetTargetPlanets ftp) {
        ftp.fleet.getShips().forEach((key, value) -> ftp.planetTo.getResidingFleet().merge(key, value, Long::sum));
    }

    private Mono<Fleet> cleanupExecutedMissions(FleetTargetPlanets ftp) {
        ftp.planetFrom.getOnRouteFleets().get(ftp.fleet.getMissionType()).remove(ftp.fleet.getId());
        ftp.planetTo.getOnRouteFleets().get(ftp.fleet.getMissionType()).remove(ftp.fleet.getId());
        return planetService.saveAll(Flux.just(ftp.planetFrom, ftp.planetTo))
                .then(fleetRepository.save(ftp.fleet));
    }

    @AllArgsConstructor
    private static class FleetTargetPlanets {
        Planet planetFrom;
        Planet planetTo;
        Fleet fleet;
    }
}
