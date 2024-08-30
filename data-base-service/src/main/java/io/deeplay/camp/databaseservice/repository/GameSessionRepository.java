package io.deeplay.camp.databaseservice.repository;

import io.deeplay.camp.databaseservice.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer> { }
