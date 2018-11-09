package com.vboldyrev.interview.config;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    final Environment env;

    @Autowired
    public CassandraConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("cassandra.keySpace");
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(env.getProperty("cassandra.contactPoint"));
        cluster.setPort(Integer.valueOf(Objects.requireNonNull(env.getProperty("cassandra.port"))));
        cluster.setJmxReportingEnabled(false);
        cluster.setKeyspaceCreations(getKeyspaceCreations());
        cluster.setAuthProvider(getAuthProvider());
        return cluster;
    }
    @Override
    protected AuthProvider getAuthProvider() {
        return new PlainTextAuthProvider(env.getProperty("cassandra.username"),
                env.getProperty("cassandra.password"));
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(
                CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
                        .ifNotExists()
                        .withSimpleReplication(1)
        );
    }

    @Bean
    public CassandraMappingContext cassandraMapping() {
        return new CassandraMappingContext();
    }

    @Override
    protected List<String> getStartupScripts() {
        return Collections.singletonList(
                "CREATE TABLE IF NOT EXISTS " + getKeyspaceName() + ".user"
                + " (name text PRIMARY KEY, date timestamp);");
    }
}
