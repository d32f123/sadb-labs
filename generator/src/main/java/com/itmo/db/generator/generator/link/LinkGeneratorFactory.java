package com.itmo.db.generator.generator.link;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.link.AbstractLink;
import com.itmo.db.generator.model.link.PersonProjectLink;

public class LinkGeneratorFactory {

    private static LinkGeneratorFactory instance;

    private LinkGeneratorFactory() {}

    public synchronized static LinkGeneratorFactory getInstance() {
        if (instance == null) {
            instance = new LinkGeneratorFactory();
        }

        return instance;
    }

    public <T extends AbstractLink> LinkGenerator getGenerator(Class<T> linkClass, Generator generator) {
        if (linkClass.equals(PersonProjectLink.class)) {
            return new
        }
    }
}
