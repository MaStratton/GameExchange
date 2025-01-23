package org.GameExchange.ExchangeAPI.Model;

import org.GameExchange.ExchangeAPI.Controller.ProtectionController;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
//import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name="People")
@Table(name="People")
public class Person implements Serializable{

    @Id
    @Column(name="personId")
    private int personId;


    @Column(name="firstName", nullable=false)
    private String firstName;

    @Column(name="lastName", nullable=true)
    private String lastName;

    @Column(name="emailAddr", nullable=false)
    private String emailAddr;

    @Column(name="password", nullable=false)
    private String password;

    @ManyToOne
    @JoinColumn(name="addressId")
    private Address address;

    // @Transient
    // private int addressId;

    public String getUri(){
        return "localhost:8080/User/" + personId;
    }

    public Person (String firstName, String lastName, String emailAddr, String password, Address address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddr = emailAddr;
        this.password = ProtectionController.hash(password);
        this.address = address;
    }


    public Person(){}

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddr="
                + emailAddr + ", address=" + address + "]";
        }

        public void setAddress(Address address){
            this.address = address;
        }


    public int getPersonId() {
        return personId;
    }



    public String getFirstName() {
        return firstName;
    }



    public String getLastName() {
        return lastName;
    }



    public String getEmailAddr() {
        return emailAddr;
    }



    public String getPassword() {
        return password;
    }



    public Address getAddress() {
        return address;
    }
    
}
