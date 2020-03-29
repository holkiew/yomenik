package com.holkiew.yomenik.battlesim.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.holkiew.yomenik.battlesim.configuration.jackson.serializer.generic.WeaponDeserializer;
import com.holkiew.yomenik.battlesim.ship.common.model.ship.type.weapon.Weapon;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

@Configuration
public class JacksonConfig {

    @Bean
    public CodecCustomizer jacksonLegacyJsonCustomizer(ObjectMapper mapper) {
        return (configurer) -> {
            mapper.registerModule(deserializersModule());
            CodecConfigurer.CustomCodecs customCodecs = configurer.customCodecs();
            customCodecs.encoder(new Jackson2JsonEncoder(mapper));
            customCodecs.decoder(new Jackson2JsonDecoder(mapper));
        };
    }

    private SimpleModule deserializersModule() {
        return new SimpleModule()
                .addDeserializer(Weapon.class, new WeaponDeserializer());
    }

}
