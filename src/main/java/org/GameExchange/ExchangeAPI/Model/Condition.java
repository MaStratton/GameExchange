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

    @Column(name="conditionLabel", nullable=false)
    private String conditionLabel;

    public Condition() {
    }

    public Condition(int conditionId, String conditionLabel) {
        this.conditionId = conditionId;
        this.conditionLabel = conditionLabel;
    }

 

    public int getConditionId() {
        return conditionId;
    }

    public String getConditionLabel() {
        return conditionLabel;
    }
}


