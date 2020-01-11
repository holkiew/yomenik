package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.planet.entity.BuildingConfiguration;
import com.holkiew.yomenik.battlesim.planet.model.building.BuildingType;
import com.holkiew.yomenik.battlesim.planet.model.building.properties.BuildingProperties;
import com.holkiew.yomenik.battlesim.planet.port.BuildingConfigurationRepository;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.holkiew.yomenik.battlesim.planet.model.building.BuildingType.*;

@Component
@DependsOn("buildingType.BuildingTypePropertiesInjector")
public class BuildingConfigurationInserts extends MongoInsertsLoader<BuildingConfiguration, BuildingConfigurationRepository> {

    public BuildingConfigurationInserts(BuildingConfigurationRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = buildingConfiguration -> repository.existsById(buildingConfiguration.getId());
        setData();
    }

    private void setData() {
        var buildingConfiguration = BuildingConfiguration.builder().id("1")
                .configuration(createBuildingConfiguration())
                .build();
        this.setData(buildingConfiguration);
    }

    public Map<BuildingType, Map<Integer, BuildingConfiguration.BuildingResources>> createBuildingConfiguration() {
        return Stream.of(IRON_MINE, CRYSTAL_MINE, CONCRETE_FACTORY)
                .map(buildingType -> {
                    BuildingProperties properties = buildingType.getProperties();
                    var levelToBuildingTypeMap = IntStream.rangeClosed(1, 10).boxed()
                            .map(i -> Map.entry(i, new BuildingConfiguration.BuildingResources(properties.getLevelCost(i), properties.getLevelOutput(i))))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    return Map.entry(buildingType, levelToBuildingTypeMap);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }
}