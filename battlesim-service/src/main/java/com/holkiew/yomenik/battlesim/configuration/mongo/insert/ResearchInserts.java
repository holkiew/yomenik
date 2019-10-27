package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.google.common.collect.Maps;
import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.planet.entity.Research;
import com.holkiew.yomenik.battlesim.planet.model.research.ResearchType;
import com.holkiew.yomenik.battlesim.planet.port.ResearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResearchInserts extends MongoInsertsLoader<Research, ResearchRepository> {

    public ResearchInserts(@Autowired ResearchRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = planet -> repository.existsById(planet.getId());
        setData();
    }

    private void setData() {
        var researches1 = Maps.<ResearchType, Integer>newHashMap();
        researches1.put(ResearchType.LASER, 1);
        var research1 = new Research("1", researches1);
        this.setData(research1);
    }
}
