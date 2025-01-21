package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameJpaRepository extends JpaRepository<Game, Integer>{

    @Query("SELECT G FROM Games G WHERE G.gameTitle LIKE :gameTitle")
    List<Game> findByTitle(@Param("gameTitle") String gameTitle);
    
}
