package org.GameExchange.ExchangeAPI.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameOwnerRecordJpaRepository extends JpaRepository<GameOwnerRecord, Integer>{
    
}