package edu.school21.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

public class EmbeddedDataSourceTest {

    private Connection connection;

    @BeforeEach
    public void init() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        embeddedDatabaseBuilder
                .setType(EmbeddedDatabaseType.HSQL)
                .addScripts("schema.sql", "data.sql");
        EmbeddedDatabase embeddedDatabase = embeddedDatabaseBuilder.build();

        try {
            connection = embeddedDatabase.getConnection();
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void connectionShouldNotBeNull() {
        Assertions.assertNotNull(connection, "The database connection is null");
    }

    @AfterEach
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Assertions.fail(e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
