package org.weightbridge.system.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String POSTGRESQL_IMAGE_VERSION = "postgres:16:3";
    private static PostgresContainer container;
    // singleton pattern private constructor and access through getInstance()
    // private constructor
    private PostgresContainer(){
        super(POSTGRESQL_IMAGE_VERSION);
    }

    public static PostgresContainer getInstance(){
        if(container == null){
            container = new PostgresContainer();
        }
        return container;
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void stop(){
        // do nothing, handled by JVM
    }
}
