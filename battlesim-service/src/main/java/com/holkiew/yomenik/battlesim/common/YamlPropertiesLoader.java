package com.holkiew.yomenik.battlesim.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class YamlPropertiesLoader {

    private static final Map<String, Object> LOADED_YAMLS;
    private static final boolean FAIL_ON_UNKNOWN_PROPERTIES = false;

    static {
        String yamlConfigDir = "config_values/";
        var loadedYamls = getLoadedYamls(yamlConfigDir);
        LOADED_YAMLS = Collections.unmodifiableMap(loadedYamls.orElseThrow(() -> new NullPointerException("YAML files not loaded")));
        log.info("YAMLs loaded");
    }

    public static <T> Optional<T> load(Class<T> toValueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FAIL_ON_UNKNOWN_PROPERTIES);
        Map specificObjectMap = (Map) LOADED_YAMLS.get(toValueType.getSimpleName());
        return Optional.ofNullable(mapper.convertValue(specificObjectMap, toValueType));
    }

    private static Optional<Map<String, Object>> getLoadedYamls(String yamlConfigDir) {
        Optional<Map<String, Object>> loadedYamls = Optional.empty();
        try {
            loadedYamls = IOUtils.readLines(Thread.currentThread().getContextClassLoader().getResourceAsStream(yamlConfigDir), Charsets.UTF_8)
                    .stream().filter(filename -> filename.endsWith(".yml"))
                    .map(filename -> loadYaml("/" + yamlConfigDir + filename))
                    .reduce((map1, map2) -> {
                        map1.putAll(map2);
                        return map1;
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedYamls;
    }

    private static Map<String, Object> loadYaml(String resourcePath) {
        try (InputStream is = YamlPropertiesLoader.class.getResourceAsStream(resourcePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(is);
        } catch (IOException e) {
            return null;
        }
    }

}
