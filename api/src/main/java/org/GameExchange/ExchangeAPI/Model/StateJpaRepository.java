package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StateJpaRepository extends JpaRepository<State, Integer>{
    @Query("SELECT S FROM States S WHERE stateAbbr = :stateAbbr")
    List<State> findByAbbr(@Param("stateAbbr") String stateAbbr);

    
}
