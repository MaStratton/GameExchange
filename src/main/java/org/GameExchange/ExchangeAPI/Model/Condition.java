package org.GameExchange.ExchangeAPI.Model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Entity(name="Conditions")
@Table(name="Conditions")
public class Condition implements Serializable{

    @Id
    @Column(name="conditionId")
    private int conditionId;

    @Column(name="conditionName", nullable=false)
    private String conditionName;

    public Condition() {
    }

    public Condition(int conditionId, String conditionName) {
        this.conditionId = conditionId;
        this.conditionName = conditionName;
    }

 

    public int getConditionId() {
        return conditionId;
    }

    public String getConditionName() {
        return conditionName;
    }
}


