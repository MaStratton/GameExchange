package org.GameExchange.ExchangeAPI.Model;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity(name="OfferRecords")
@Table(name="OfferRecords")
public class OfferRecord {

    @Id
    @Column(name="offerRecordId")
    private int offerRecordId;

    @Column(name="offerStatus")
    private String offerStatus;

    @Column(name="offerCreationTime")
    private Timestamp creationTime;



    public OfferRecord(String status) {
        this.offerStatus = status;
    
    }


    public OfferRecord() {
        this.offerStatus = "pending";
    }


    public int getOfferRecordId() {
        return offerRecordId;
    }


    public String getOfferStatus() {
        return offerStatus;
    }


    public Timestamp getCreationTime() {
        return creationTime;
    }


    @Override
    public String toString() {
        return "OfferRecord [offerRecordId=" + offerRecordId + ", offerStatus=" + offerStatus + ", creationTime="
                + creationTime + "]";
    }

    

    
    
}
