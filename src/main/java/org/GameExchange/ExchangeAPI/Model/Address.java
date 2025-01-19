package org.GameExchange.ExchangeAPI.Model;

import org.GameExchange.ExchangeAPI.Controller.ProtectionController;
import org.springframework.lang.NonNull;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity(name="Addresses")
@Table(name="Addresses")
public class Address {
    
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

    public Address(){}

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
                + ", state=" + state + ", city=" + city + ", zip=" + zip + "]";
    }

    
}
