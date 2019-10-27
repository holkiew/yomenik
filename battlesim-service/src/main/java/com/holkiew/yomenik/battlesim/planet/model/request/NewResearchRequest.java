package com.holkiew.yomenik.battlesim.planet.model.request;

import com.holkiew.yomenik.battlesim.planet.model.research.ResearchType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewResearchRequest {
    @NotBlank
    private String planetId;
    @NotNull
    private ResearchType researchType;
}
