package org.GameExchange.ExchangeAPI.Model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name="GameOwnerRecords")
@Table(name="GameOwnerRecords")
public class GameOwnerRecord implements Serializable{
    
    @Id
    @Column(name="gameOwnerRecordId")
    private int gameOwnerRecordId;

    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="ownerId", nullable=false)
    private Person owner;

    @ManyToOne
    @JoinColumn(name="gameId", nullable=false)
    private Game game;

    @ManyToOne
    @JoinColumn(name="systemId", nullable=false)
    private System system;

    @ManyToOne
    @JoinColumn(name="conditionId", nullable=false)
    private Condition condition;

    @Column(name="isInOffer", nullable=false)
    private boolean isInOffer;

    @ManyToOne
    @JoinColumn(name="offerRecordId")
    private OfferRecord offerRecord;

    @ManyToOne
    @JoinColumn(name="OfferSenderId")
    private Person offerSender;

    

    public GameOwnerRecord(Person owner, Game game, System system, Condition condition) {
        this.owner = owner;
        this.game = game;
        this.system = system;
        this.condition = condition;
    }

    public GameOwnerRecord(int gameOwnerRecordId, Person owner, Game game, System system, Condition condition,
            boolean isInOffer, OfferRecord offerRecord, Person offerSender) {
        this.gameOwnerRecordId = gameOwnerRecordId;
        this.owner = owner;
        this.game = game;
        this.system = system;
        this.condition = condition;
        this.isInOffer = isInOffer;
        this.offerRecord = offerRecord;
        this.offerSender = offerSender;
    }

    public GameOwnerRecord() {}

    public int getGameOwnerRecordId() {
        return gameOwnerRecordId;
    }

    public Person getOwner() {
        return owner;
    }

    public Game getGame() {
        return game;
    }

    public System getSystem() {
        return system;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean isInOffer() {
        return isInOffer;
    }

    public OfferRecord getOfferRecord() {
        return offerRecord;
    }

    public Person getOfferSender() {
        return offerSender;
    }

    
}
