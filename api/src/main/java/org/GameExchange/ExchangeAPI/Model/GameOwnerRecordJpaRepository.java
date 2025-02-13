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

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerStatus LIKE :status
            AND GOR.owner.personId = GOR.offerSender.personId
        """)
    List<GameOwnerRecord> findOffersByGameOwnerSent(@Param("ownerId") int ownerId); 
    
    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerStatus LIKE :status
            AND GOR.owner.personId != GOR.offerSender.personId
        """)
    List<GameOwnerRecord> findOffersByGameOwnerReceived(@Param("ownerId") int ownerId);

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerStatus LIKE :status
            AND GOR.owner.personId = GOR.offerSender.personId
        """)
    List<GameOwnerRecord> findOffersByGameOwnerSent(@Param("ownerId") int ownerId, @Param("status") String status); 
    
    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerStatus LIKE :status
            AND GOR.owner.personId != GOR.offerSender.personId
        """)
    List<GameOwnerRecord> findOffersByGameOwnerReceived(@Param("ownerId") int ownerId, @Param("status") String status);

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.owner.personId = :ownerId
            AND GOR.isInOffer = TRUE
            AND GOR.offerRecord.offerRecordId = :recordId
        """)
    List<GameOwnerRecord> findOfferByOwnerAndOfferId(@Param("ownerId") int ownerId, @Param("recordId") int recordID);

    @Query("""
        SELECT GOR FROM GameOwnerRecords GOR 
            CROSS JOIN People P
            WHERE GOR.isInOffer = TRUE
            AND GOR.offerSender.personId = :senderId
        """)
    List<GameOwnerRecord> findOfferBySender(@Param("senderId") int senderId);


    @Query("""
            SELECT P FROM GameOwnerRecords GOR
                CROSS JOIN People P
                WHERE GOR.offerRecord.offerRecordId = :offerRecordId
                AND GOR.owner.personId != :senderId
            """)
    Person findPersonByOfferIdAndOtherPersonId(@Param("offerRecordId") int offerRecordId, @Param("senderId") int senderId);

    @Query("""
            SELECT GOR FROM GameOwnerRecords GOR
                CROSS JOIN People P
                WHERE GOR.gameOwnerRecordId = :GORID
                AND GOR.owner.personId = :ownerId
            """)
    GameOwnerRecord findByRecordIdAndOwnerId(@Param("GORID") int GORID, @Param("ownerId") int ownerId);

}