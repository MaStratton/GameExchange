package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameOwnerRecordJpaRepository extends JpaRepository<GameOwnerRecord, Integer>{

    @Query("SELECT GOR FROM GameOwnerRecords GOR WHERE GOR.isInOffer = FALSE")
    List<GameOwnerRecord> findAllAvailable();

    List<GameOwnerRecord> findByGameOwnerRecordIdAndOwnerPersonId(int gameOwnerRecordId, int ownerId); 
            

}