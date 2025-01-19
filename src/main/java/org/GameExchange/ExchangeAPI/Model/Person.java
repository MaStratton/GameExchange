package org.GameExchange.ExchangeAPI.Model;

import org.GameExchange.ExchangeAPI.Controller.ProtectionController;
import org.springframework.lang.NonNull;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name="People")
@Table(name="People")
public class Person implements Serializable{

    @Id
    @Column(name="personId")
    private String personId;


    @Column(name="firstName", nullable=false)
    private String firstName;

    @Column(name="lastName", nullable=false)
    private String lastName;

    @Column(name="emailAddr", nullable=false)
    private String emailAddr;

    @Column(name="password", nullable=false)
    private String password;

    @ManyToOne
    @JoinColumn(name="addressId")
    private Address address;

    @Transient
    private String preHashedPasswd;

    public Person (String firstName, String lastName, String emailAddr, String password, int addressId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddr = emailAddr;
        this.password = ProtectionController.hash(password);
        //this.addressId = addressId;
        this.preHashedPasswd = password;
    }

    public Person(){}

    public String getEmailAddr(){
        return emailAddr;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddr="
                + emailAddr + ", password=" + password + ", address=" + address + ", preHashedPasswd=" + preHashedPasswd
                + "]";
    }



    
    
}
