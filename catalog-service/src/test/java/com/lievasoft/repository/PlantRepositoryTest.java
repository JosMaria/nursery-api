package com.lievasoft.repository;

import com.lievasoft.PostgresTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class PlantRepositoryTest {

    @Inject
    PlantRepository underTest;

    @Test
    void testDatabaseEmpty() {
        long count = underTest.count();
        Assertions.assertEquals(0, count);
    }
}