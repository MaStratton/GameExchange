package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSystemJpaRepository extends JpaRepository<GameSystem, Integer> {
    @Query("SELECT S FROM Systems S WHERE systemName LIKE :systemName")
    List<GameSystem> findByName(@Param("systemName") String systemName);
    
}
