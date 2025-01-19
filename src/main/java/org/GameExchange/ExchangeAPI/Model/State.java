package org.GameExchange.ExchangeAPI.Model;

import org.GameExchange.ExchangeAPI.Controller.ProtectionController;
import org.springframework.lang.NonNull;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name="States")
@Table(name="States")
public class State implements Serializable{

    @Id
    @Column(name="StateId")
    int stateId;

    @Column(name="stateName", nullable=false)
    String stateName;

    @Column(name="stateAbbr", nullable=false)
    String stateAbbr;

    public State(int stateId, String stateName, String stateAbbr) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.stateAbbr = stateAbbr;
    }

    public State(String stateName, String stateAbbr){
        this.stateName = stateName;
        this.stateAbbr = stateAbbr;
    }
    
    public State(){}

    @Override
    public String toString() {
        return "State [stateId=" + stateId + ", stateName=" + stateName + ", stateAbbr=" + stateAbbr + "]";
    }

    public int getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    
}
