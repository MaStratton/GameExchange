package org.GameExchange.ExchangeAPI.Model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="OfferRecords")
@Table(name="OfferRecords")
public class OfferRecord {

    @Id
    @Column(name="offerRecordId")
    private int offerRecordId;

    @Column(name="offerStatus")
    private OfferStatus offerStatus;

    @Column(name="creationTime")
    private Timestamp creationTime;


    enum OfferStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
    
}
