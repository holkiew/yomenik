package com.holkiew.yomenik.user.domian.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Player extends User {
    private String researchId;
    private String fleetConfigId;
}
