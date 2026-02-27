package com.habittracker.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Railway note: the Postgres service exposes a DATABASE_URL like:
 *   postgresql://user:pass@host:port/dbname?sslmode=require
 *
 * Spring expects a JDBC URL, so this config converts DATABASE_URL into a JDBC datasource.
 * It only activates when DATABASE_URL is present on the application service.
 */
@Configuration
@ConditionalOnProperty(name = "DATABASE_URL")
public class RailwayDataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(Environment env) {
        // Only treat real env vars as explicit overrides.
        // Do NOT treat application.yml defaults as "explicit", otherwise they override DATABASE_URL creds.
        String explicitJdbcUrl = firstNonBlank(env.getProperty("SPRING_DATASOURCE_URL"));
        String explicitUser = firstNonBlank(env.getProperty("SPRING_DATASOURCE_USERNAME"));
        String explicitPass = firstNonBlank(env.getProperty("SPRING_DATASOURCE_PASSWORD"));

        if (explicitJdbcUrl != null && explicitJdbcUrl.startsWith("jdbc:")) {
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(explicitJdbcUrl);
            if (explicitUser != null) ds.setUsername(explicitUser);
            if (explicitPass != null) ds.setPassword(explicitPass);
            ds.setDriverClassName("org.postgresql.Driver");
            return ds;
        }

        String databaseUrl = env.getProperty("DATABASE_URL");
        ParsedDatabaseUrl parsed = parseDatabaseUrl(databaseUrl);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(parsed.jdbcUrl);
        if (explicitUser != null) {
            ds.setUsername(explicitUser);
        } else if (parsed.username != null) {
            ds.setUsername(parsed.username);
        }
        if (explicitPass != null) {
            ds.setPassword(explicitPass);
        } else if (parsed.password != null) {
            ds.setPassword(parsed.password);
        }
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    private static ParsedDatabaseUrl parseDatabaseUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("DATABASE_URL is set but empty");
        }
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "";
            if (!scheme.startsWith("postgres")) {
                throw new IllegalArgumentException("Unsupported DATABASE_URL scheme: " + scheme);
            }

            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String path = uri.getPath() != null ? uri.getPath() : "";
            String db = path.startsWith("/") ? path.substring(1) : path;

            if (host == null || host.isBlank() || db.isBlank()) {
                throw new IllegalArgumentException("Invalid DATABASE_URL (missing host or db name)");
            }

            String query = uri.getQuery();
            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + db + (query != null && !query.isBlank() ? "?" + query : "");

            String username = null;
            String password = null;
            if (uri.getUserInfo() != null && !uri.getUserInfo().isBlank()) {
                String[] parts = uri.getUserInfo().split(":", 2);
                username = parts.length > 0 ? parts[0] : null;
                password = parts.length > 1 ? parts[1] : null;
            }

            return new ParsedDatabaseUrl(jdbcUrl, username, password);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid DATABASE_URL: " + e.getMessage(), e);
        }
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private record ParsedDatabaseUrl(String jdbcUrl, String username, String password) {}
}

