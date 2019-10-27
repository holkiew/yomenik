package com.holkiew.yomenik.battlesim.configuration.mongo.insert;

import com.google.common.collect.Lists;
import com.holkiew.yomenik.battlesim.common.MongoInsertsLoader;
import com.holkiew.yomenik.battlesim.galaxy.entity.Galaxy;
import com.holkiew.yomenik.battlesim.galaxy.port.GalaxyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GalaxyInserts extends MongoInsertsLoader<Galaxy, GalaxyRepository> {

    public GalaxyInserts(@Autowired GalaxyRepository repository) {
        super(repository);
        this.checkIfObjectAlreadyExistsFun = galaxy -> repository.existsById(galaxy.getId());
        setData();
    }

    private void setData() {
        Galaxy galaxy = new Galaxy();
        galaxy.setId(1);
        galaxy.setSolarSystems(Lists.newArrayList());
        setData(galaxy);
    }
}
