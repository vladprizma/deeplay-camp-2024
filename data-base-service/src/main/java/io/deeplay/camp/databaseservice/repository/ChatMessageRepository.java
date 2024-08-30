package io.deeplay.camp.databaseservice.repository;

import io.deeplay.camp.databaseservice.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> { }
