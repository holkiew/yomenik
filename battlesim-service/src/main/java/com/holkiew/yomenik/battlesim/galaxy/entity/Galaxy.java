package com.holkiew.yomenik.battlesim.galaxy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
public class Galaxy {
    @Id
    private int id;
    private List<String> solarSystems;
}
