package io.deeplay.camp.databaseservice.repository;

import io.deeplay.camp.databaseservice.model.Token;
import io.deeplay.camp.databaseservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> { 
    void deleteByUserId(int userId);
    Token findByUserId(int userId);
    void deleteByUser(User user);
}
