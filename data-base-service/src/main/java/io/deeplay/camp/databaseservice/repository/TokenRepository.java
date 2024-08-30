package io.deeplay.camp.databaseservice.repository;

import io.deeplay.camp.databaseservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> { }
