package com.lievasoft;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {

    private static PostgreSQLContainer<?> POSTGRES;

    @Override
    public Map<String, String> start() {
        var dockerImageName = DockerImageName.parse("postgres:17-alpine");
        POSTGRES = new PostgreSQLContainer<>(dockerImageName)
                .withDatabaseName("test_nursery_db")
                .withUsername("test_user")
                .withPassword("test_password")
                .withReuse(true);

        POSTGRES.start();
        return Map.of(
                "quarkus.datasource.jdbc.url", POSTGRES.getJdbcUrl(),
                "quarkus.datasource.username", POSTGRES.getUsername(),
                "quarkus.datasource.password", POSTGRES.getPassword(),
                "quarkus.hibernate-orm.schema-management.strategy", "drop-and-create"
        );
    }

    @Override
    public void stop() {
        IO.println("⚠️ Contenedor NO fue eliminado. Limpiar manualmente:");
        IO.println("docker stop " + POSTGRES.getContainerId());
        IO.println("docker rm " + POSTGRES.getContainerId());
//        if (POSTGRES.isCreated())
//            POSTGRES.stop();
    }
}
