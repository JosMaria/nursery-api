package com.lievasoft;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer<?> postgreSQLContainer;

    @Override
    public Map<String, String> start() {
        var dockerImageName = DockerImageName.parse("postgres:17-alpine");
        postgreSQLContainer = new PostgreSQLContainer<>(dockerImageName)
                .withDatabaseName("test_nursery_db")
                .withUsername("test_user")
                .withPassword("test_password");

        postgreSQLContainer.start();
        return Map.of(
                "quarkus.datasource.jdbc.url", postgreSQLContainer.getJdbcUrl(),
                "quarkus.datasource.username", postgreSQLContainer.getUsername(),
                "quarkus.datasource.password", postgreSQLContainer.getPassword(),
                "quarkus.hibernate-orm.schema-management.strategy", "drop-and-create"
        );
    }

    @Override
    public void stop() {
        if (postgreSQLContainer.isCreated())
            postgreSQLContainer.stop();
    }
}
