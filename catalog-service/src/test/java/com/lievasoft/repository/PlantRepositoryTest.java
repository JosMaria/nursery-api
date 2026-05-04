package com.lievasoft.repository;

import com.lievasoft.PostgresTestResource;
import com.lievasoft.dto.response.plant.PlantSummaryResponse;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class PlantRepositoryTest {

    @Inject
    PlantRepository underTest;

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        var path = Paths.get("src/test/resources/plants-data.sql");
        String statement = new String(Files.readAllBytes(path));
        underTest.getEntityManager()
                .createNativeQuery(statement)
                .executeUpdate();
    }

    @Test
    void shouldReturnAllPlantsSummary() {
        List<PlantSummaryResponse> response = underTest.obtainPlantsSummary();

        assertThat(response)
                .isNotNull()
                .hasSize(2);

        assertThat(response)
                .extracting("scientificName", String.class)
                .contains("rosa", "aloe vera");

        assertThat(response)
                .extracting("favorite", Boolean.class)
                .doesNotContain(false);
    }
}