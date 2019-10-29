package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.ShipClassType;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.LaserWeaponType;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.entity.FleetManagementConfig;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.FireMode;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.model.ShipClassGroupTemplate;
import com.holkiew.yomenik.battlesim.ship.fleetmanagement.port.FleetManagementConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class FleetManagementConfigInserts extends MongoInsertsLoader<FleetManagementConfig, FleetManagementConfigRepository> {

    public FleetManagementConfigInserts(@Autowired FleetManagementConfigRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = galaxy -> repository.existsById(galaxy.getId());
        setData();
    }

    private void setData() {
        var config = new FleetManagementConfig();
        config.setId("1");
        config.setShipGroupTemplates(getShipClassGroups());
        setData(config);
    }

    private Map<ShipClassType, ShipClassGroupTemplate> getShipClassGroups() {
        var class1Template = new ShipClassGroupTemplate("class1 template", ShipClassType.SHIP_LEVEL1,
                Map.ofEntries(Map.entry(1, LaserWeaponType.LASER1)), FireMode.FIXED);
        var class2Template = new ShipClassGroupTemplate("class2 template", ShipClassType.SHIP_LEVEL2,
                Map.ofEntries(Map.entry(1, LaserWeaponType.LASER1), Map.entry(2, LaserWeaponType.LASER1)), FireMode.FIXED);
        var class3Template = new ShipClassGroupTemplate("class3 template", ShipClassType.SHIP_LEVEL3,
                IntStream.range(1, 6).boxed().collect(Collectors.toMap(Function.identity(), i -> LaserWeaponType.LASER1)), FireMode.SCATTER);
        return Stream.of(class1Template, class2Template, class3Template).collect(Collectors.toMap(ShipClassGroupTemplate::getShipClassType, Function.identity()));
    }
}