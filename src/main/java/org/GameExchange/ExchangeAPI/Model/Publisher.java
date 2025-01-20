package org.GameExchange.ExchangeAPI.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="Publishers")
@Table(name="Publishers")
public class Publisher implements Serializable{

    @Id
    @Column(name="publisherId")
    private int publisherId;
    
    @Column(name="publisherName", nullable=false)
    private String publiherName;

    public Publisher(int publisherId, String publiherName) {
        this.publisherId = publisherId;
        this.publiherName = publiherName;
    }

    public Publisher(){}

    @Override
    public String toString() {
        return "Publisher [publisherId=" + publisherId + ", publiherName=" + publiherName + "]";
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPubliherName() {
        return publiherName;
    }

}
