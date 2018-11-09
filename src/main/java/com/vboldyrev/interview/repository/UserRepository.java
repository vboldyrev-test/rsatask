package com.vboldyrev.interview.repository;

import com.vboldyrev.interview.dto.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.vboldyrev.interview.repository")
public interface UserRepository extends CassandraRepository<User, String> {
}
