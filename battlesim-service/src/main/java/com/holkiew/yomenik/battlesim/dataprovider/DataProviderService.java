package com.holkiew.yomenik.battlesim.dataprovider;

import com.holkiew.yomenik.battlesim.dataprovider.model.TemplateOption;
import com.holkiew.yomenik.battlesim.dataprovider.model.TemplateOptionConfiguration;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.hull.HullType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.KineticWeaponType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.LaserWeaponType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Service
public class DataProviderService {

    public Mono<TemplateOptionConfiguration> getPossibleTemplateOptions() {
        return Mono.just(new TemplateOptionConfiguration())
                .map(templateOptionConfiguration -> {
                    var hullOptions = new HashMap<HullType, TemplateOption>();
                    templateOptionConfiguration.setHullOptions(hullOptions);
                    hullOptions.put(HullType.SHIP_LEVEL1, TemplateOption.builder()
                            .weapons(List.of(LaserWeaponType.LASER1, KineticWeaponType.KINETIC1)).build());
                    hullOptions.put(HullType.SHIP_LEVEL2, TemplateOption.builder()
                            .weapons(List.of(LaserWeaponType.LASER2, KineticWeaponType.KINETIC1)).build());
                    return templateOptionConfiguration;
                });
    }
}
