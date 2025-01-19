package org.GameExchange.ExchangeAPI.Model;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Entity(name="Zips")
@Table(name="Zips")
public class Zip implements Serializable{

    @Id
    @Column(name="zipId")
    int zipId;

    @Column(name="zipCode", nullable=false)
    String zipCode;

    public Zip(String zipCode){
        this.zipCode = zipCode;
    }

    public Zip(){}

    public int getZipId() {
        return zipId;
    }

    public String getZipCode() {
        return zipCode;
    }
    
    
}
