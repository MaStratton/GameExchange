package org.GameExchange.ExchangeAPI.Model;

import java.sql.Timestamp;



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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="offerRecordId")
    private int offerRecordId;

    @Column(name="offerStatus")
    private String offerStatus;

    @Column(name="offerCreationTime", insertable=false)
    private Timestamp creationTime;



    public OfferRecord(String status) {
        this.offerStatus = status;
    
    }


    public OfferRecord() {
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


    public void setOfferRecordId(int offerRecordId) {
        this.offerRecordId = offerRecordId;
    }


    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }


    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    

    
    
}
