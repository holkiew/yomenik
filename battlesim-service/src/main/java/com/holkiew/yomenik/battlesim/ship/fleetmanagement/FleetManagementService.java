package com.holkiew.yomenik.battlesim.ship.fleetmanagement;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.planet.entity.Planet;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull.Hull;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.ShipGroupTemplate;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.exception.ShipGroupTemplateAlreadyExistsException;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.exception.ShipGroupTemplateIsInUse;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.exception.ShipGroupTemplateNotExists;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.DeleteShipGroupTemplateRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.ModifyShipGroupTemplateRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.request.NewShipGroupTemplateRequest;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetManagementConfigRepository;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetPort;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.PlanetPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Log4j2
public class FleetManagementService {

    private final FleetManagementConfigRepository managementRepository;
    private final PlanetPort planetPort;
    private final FleetPort fleetPort;

//    public Mono<FleetManagementConfig> modifyConfig(ModifyFleetManagementRequest request) {
//        return managementRepository.findById(request.getUserId())
//                .map(config -> {
//                    updateValue(config::setFireMode, request.getFireMode());
//                    return config;
//                }).flatMap(managementRepository::save);
//    }
//
//    public Mono<FleetManagementConfig> newConfig(NewFleetManagementRequest request) {
//        var newConfig = FleetManagementConfig.builder()
//                .id(request.getUserId()).fireMode(request.getFireMode()).build();
//        return managementRepository.save(newConfig);
//    }

    public Mono<FleetManagementConfig> getManagementConfig(Principal principal) {
        return managementRepository.findById(principal.getId());
    }

    public Mono<FleetManagementConfig> newShipGroupTemplate(Principal principal, NewShipGroupTemplateRequest request) {
        return getFleetManagementConfigById(principal, checkIfTemplateNotExists(request.getTemplateName()))
                .map(addNewTemplateToManagementConfig(request))
                .flatMap(managementRepository::save);
    }

    public Mono<FleetManagementConfig> deleteShipGroupTemplate(Principal principal, DeleteShipGroupTemplateRequest request) {
        return getFleetManagementConfigById(principal, checkIfTemplateExists(request.getTemplateName()))
                .zipWith(planetPort.findAllByUserId(principal.getId()).collectList())
                .flatMap(isShipGroupTemplateDeletable(request))
                .flatMap(deleteTemplateAndSave(request));
    }

    public Mono<FleetManagementConfig> modifyShipGroupTemplate(Principal principal, ModifyShipGroupTemplateRequest request) {
        return getFleetManagementConfigById(principal, checkIfTemplateExists(request.getTemplateName()))
                .map(modifyShipTemplate(request))
                .flatMap(managementRepository::save);
    }

    private Mono<FleetManagementConfig> getFleetManagementConfigById(Principal principal, Function<FleetManagementConfig, Object> checkingFunction) {
        return managementRepository.findById(principal.getId())
                .doOnError(log::error)
                .map(checkingFunction)
                .cast(FleetManagementConfig.class);
    }

    private Function<FleetManagementConfig, Object> checkIfTemplateExists(String templateName) {
        return fleetManagementConfig -> fleetManagementConfig.getShipGroupTemplates().containsKey(templateName)
                ? fleetManagementConfig
                : Mono.error(new ShipGroupTemplateNotExists());
    }

    private Function<FleetManagementConfig, Object> checkIfTemplateNotExists(String templateName) {
        return fleetManagementConfig -> !fleetManagementConfig.getShipGroupTemplates().containsKey(templateName)
                ? fleetManagementConfig
                : Mono.error(new ShipGroupTemplateAlreadyExistsException());
    }

    private Function<FleetManagementConfig, FleetManagementConfig> addNewTemplateToManagementConfig(NewShipGroupTemplateRequest request) {
        return fleetManagementConfig -> {
            var newShipGroupTemplate = ShipGroupTemplate.builder()
                    .name(request.getTemplateName())
                    .shipClassType(request.getHullType().getShipClassType())
                    .fireMode(request.getFireMode())
                    .hull(new Hull(request.getHullType(), request.getWeaponSlots()))
                    .build();
            fleetManagementConfig.getShipGroupTemplates().put(newShipGroupTemplate.getName(), newShipGroupTemplate);
            return fleetManagementConfig;
        };
    }

    private Function<Tuple2<FleetManagementConfig, List<Planet>>, Mono<? extends Tuple2<FleetManagementConfig, Boolean>>> isShipGroupTemplateDeletable(DeleteShipGroupTemplateRequest request) {
        return tuple -> {
            var amountToMissionIds = countFleetsOnPlanetsAndFindMissions(tuple.getT2(), request);
            if (amountToMissionIds.getT1() != 0) {
                return Mono.just(Tuples.of(tuple.getT1(), false));
            } else {
                return anyMissionContainsRequestedShipGroupTemplate(request, tuple, amountToMissionIds.getT2());
            }
        };
    }

    private Tuple2<Long, HashSet<String>> countFleetsOnPlanetsAndFindMissions(List<Planet> planets, DeleteShipGroupTemplateRequest request) {
        var residingFleetOfTemplateAmount = 0L;
        var ongoingMissionIds = new HashSet<String>();
        for (Planet planet : planets) {
            residingFleetOfTemplateAmount += planet.getResidingFleet().getOrDefault(request.getTemplateName(), 0L);
            if (residingFleetOfTemplateAmount != 0) {
                break;
            }
            ongoingMissionIds.addAll(planet.getOnRouteFleets().values());
        }
        return Tuples.of(residingFleetOfTemplateAmount, ongoingMissionIds);
    }

    private Mono<Tuple2<FleetManagementConfig, Boolean>> anyMissionContainsRequestedShipGroupTemplate(DeleteShipGroupTemplateRequest request, Tuple2<FleetManagementConfig, List<Planet>> tuple, HashSet<String> ongoingMissionIds) {
        return fleetPort.findByIds(ongoingMissionIds).collectList().map(fleets -> fleets.stream()
                .anyMatch(fleet -> fleet.getShips().containsKey(request.getTemplateName())))
                .map(isAnyOfRequestedTemplateFound -> Tuples.of(tuple.getT1(), isAnyOfRequestedTemplateFound));
    }

    private Function<Tuple2<FleetManagementConfig, Boolean>, Mono<? extends FleetManagementConfig>> deleteTemplateAndSave(DeleteShipGroupTemplateRequest request) {
        return tuple -> {
            if (!tuple.getT2()) {
                var managmentConfig = tuple.getT1();
                managmentConfig.getShipGroupTemplates().remove(request.getTemplateName());
                return managementRepository.save(managmentConfig);
            } else {
                return Mono.error(new ShipGroupTemplateIsInUse());
            }
        };
    }

    private Function<FleetManagementConfig, FleetManagementConfig> modifyShipTemplate(ModifyShipGroupTemplateRequest request) {
        return fleetManagementConfig -> {
            var shipTemplate = fleetManagementConfig.getShipGroupTemplates().get(request.getTemplateName());
            shipTemplate.setName(request.getNewTemplateName());
            shipTemplate.setHull(new Hull(request.getHullType(), request.getWeaponSlots()));
            shipTemplate.setFireMode(request.getFireMode());
            return fleetManagementConfig;
        };
    }
}
