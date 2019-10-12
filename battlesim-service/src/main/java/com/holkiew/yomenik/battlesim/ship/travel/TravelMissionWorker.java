package com.holkiew.yomenik.battlesim.ship.travel;

import com.holkiew.yomenik.battlesim.planet.PlanetFacade;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.travel.entity.Fleet;
import com.holkiew.yomenik.battlesim.ship.travel.model.exception.TravelMissonType;
import com.holkiew.yomenik.battlesim.ship.travel.port.FleetRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.holkiew.yomenik.battlesim.ship.travel.TravelService.TRAVEL_TIME_SECONDS;

@Component
@RequiredArgsConstructor
@Log4j2
public class TravelMissionWorker {

    private final FleetRepository fleetRepository;
    private final PlanetFacade planetFacade;

    @PostConstruct
    public void startWorker() {
        Flux.interval(Duration.ofSeconds(1L))
                .flatMap(l -> fleetRepository.findAllByArrivalTimeBeforeAndMissionCompletedFalse(LocalDateTime.now()))
                .flatMap(this::getFleetTargetPlanets)
                .flatMap(this::executeMission)
                .flatMap(this::cleanupExecutedMissions)
//                .doOnEach(log::info)
                .doOnError(log::error)
                .subscribe();
        log.info("Worker initialized");
    }

    private Mono<FleetTargetPlanets> getFleetTargetPlanets(Fleet fleet) {
        return planetFacade.findById(fleet.getPlanetIdFrom())
                .zipWith(planetFacade.findById(fleet.getPlanetIdTo()))
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
                throw new UnsupportedOperationException();
            case TRANSFER:
                Fleet transferFleet = setFleetOnReturnMission(ftp);
                return fleetRepository.save(transferFleet).thenReturn(ftp);
        }
        return Mono.just(ftp);
    }

    private Fleet setFleetOnReturnMission(FleetTargetPlanets ftp) {
        var transferBackFleet = new Fleet(ftp.fleet.getShips());
        transferBackFleet.setRouteOnPlanets(ftp.planetFrom, ftp.planetTo, LocalDateTime.now().plusSeconds(TRAVEL_TIME_SECONDS), TravelMissonType.TRANSFER_BACK, ftp.fleet.getId());
        return transferBackFleet;
    }

    private void addFleetToTargetPlanet(FleetTargetPlanets ftp) {
        ftp.fleet.getShips().forEach((key, value) -> ftp.planetTo.getResidingFleet().merge(key, value, Long::sum));
    }

    private Mono<Fleet> cleanupExecutedMissions(FleetTargetPlanets ftp) {
        ftp.planetFrom.getOnRouteFleets().get(ftp.fleet.getMissionType()).remove(ftp.fleet.getId());
        ftp.planetTo.getOnRouteFleets().get(ftp.fleet.getMissionType()).remove(ftp.fleet.getId());
        return planetFacade.saveAll(Flux.just(ftp.planetFrom, ftp.planetTo))
                .then(fleetRepository.save(ftp.fleet));
    }

    @AllArgsConstructor
    private static class FleetTargetPlanets {
        Planet planetFrom;
        Planet planetTo;
        Fleet fleet;
    }
}
