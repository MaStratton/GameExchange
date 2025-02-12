package org.GameExchange.ExchangeAPI.Model;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Entity(name="Cities")
@Table(name="Cities")
public class City implements Serializable{

    @Id
    @Column(name="cityId")
    int cityId;

    @Column(name="cityName", nullable=false)
    String cityName;
    

    public City(){}

    public City(String cityName){
        this.cityName = cityName;
    }


    @Override
    public String toString() {
        return "City [cityId=" + cityId + ", cityName=" + cityName + "]";
    }


    public int getCityId() {
        return cityId;
    }


    public String getCityName() {
        return cityName;
    };

    
}
