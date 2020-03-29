package com.holkiew.yomenik.battlesim.dataprovider;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import com.holkiew.yomenik.battlesim.dataprovider.model.TemplateOptionConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("data_provider")
@RequiredArgsConstructor
public class DataProviderController {

    private final DataProviderService dataProviderService;

    @GetMapping("template_options")
    public Mono<TemplateOptionConfiguration> getPossibleTemplateOptions(Principal principal) {
        return dataProviderService.getPossibleTemplateOptions();
    }
}
