package com.holkiew.yomenik.battlesim.planet.model.building.properties;

import com.holkiew.yomenik.battlesim.configuration.webflux.YamlPropertySourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = BuildingProperties.CLASSPATH + "IronMine.yml")
public class IronMineProperties extends BuildingProperties {
}
