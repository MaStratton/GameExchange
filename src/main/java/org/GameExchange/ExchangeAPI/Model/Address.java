package org.GameExchange.ExchangeAPI.Model;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name="Addresses")
@Table(name="Addresses")
public class Address implements Serializable{

    @Autowired
    
    @Id
    @Column(name="addressId")
    int addressId;

    @Column(name="addressLine1", nullable=false)
    String addressLine1;

    @Column(name="addressLine2")
    String addressLine2;

    @ManyToOne
    @JoinColumn(name="stateId")
    State state;

    @ManyToOne 
    @JoinColumn(name="cityId")
    City city;

    @ManyToOne
    @JoinColumn(name="ZipId")
    Zip zip;

    

    public Address(String addressLine1, String addressLine2, State state, City city, Zip zip) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state = state;
        this.city = city;
        this.zip = zip;
    }

    public Address(){}

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
                + ", state=" + state + ", city=" + city + ", zip=" + zip + "]";
    }

    public int getAddressId() {
        return addressId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public State getState() {
        return state;
    }

    public City getCity() {
        return city;
    }

    public Zip getZip() {
        return zip;
    }


}
