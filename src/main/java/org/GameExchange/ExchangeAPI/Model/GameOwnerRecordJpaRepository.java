package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameOwnerRecordJpaRepository extends JpaRepository<GameOwnerRecord, Integer>{

    @Query("SELECT GOR FROM GameOwnerRecords GOR WHERE GOR.isInOffer = FALSE")
    List<GameOwnerRecord> findAllAvailable();

    List<GameOwnerRecord> findByGameOwnerRecordIdAndOwnerPersonId(int gameOwnerRecordId, int ownerId); 

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
        """)
    List<GameOwnerRecord> findOffersByGameOwner(@Param("ownerId") int ownerId);

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerStatus LIKE :status
        """)
    List<GameOwnerRecord> findOffersByGameOwner(@Param("ownerId") int ownerId, @Param("status") String status);
            

}