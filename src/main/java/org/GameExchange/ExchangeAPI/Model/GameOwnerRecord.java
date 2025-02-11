package org.GameExchange.ExchangeAPI.Model;

import java.io.Serializable;
import java.util.LinkedHashMap;

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
    private GameSystem gameSystem;

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

    public String getUri(){
        return "localhost:8080/Game/Record/" + gameOwnerRecordId;
    }
    

    public GameOwnerRecord(Person owner, Game game, GameSystem system, Condition condition) {
        this.owner = owner;
        this.game = game;
        this.gameSystem = system;
        this.condition = condition;
    }

    public GameOwnerRecord(int gameOwnerRecordId, Person owner, Game game, GameSystem system, Condition condition,
            boolean isInOffer, OfferRecord offerRecord, Person offerSender) {
        this.gameOwnerRecordId = gameOwnerRecordId;
        this.owner = owner;
        this.game = game;
        this.gameSystem = system;
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

    public GameSystem getSystem() {
        return gameSystem;
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

    public void setGameSystem(GameSystem gameSystem){
        this.gameSystem = gameSystem;
    }

    public void setCondition(Condition condition){
        this.condition = condition;
    }


    
    public LinkedHashMap<String, String> toMap(){
        LinkedHashMap<String, String> mapReturn = new LinkedHashMap<String, String>();
        mapReturn.put("Record Id", String.valueOf(gameOwnerRecordId));
        mapReturn.put("Game", game.getUri());
        mapReturn.put("System", gameSystem.getUri());
        mapReturn.put("Owner", String.valueOf(owner.getUri()));
        return mapReturn;
    }


    public void setGameOwnerRecordId(int gameOwnerRecordId) {
        this.gameOwnerRecordId = gameOwnerRecordId;
    }


    public void setOwner(Person owner) {
        this.owner = owner;
    }


    public void setGame(Game game) {
        this.game = game;
    }


    public void setInOffer(boolean isInOffer) {
        this.isInOffer = isInOffer;
    }


    public void setOfferRecord(OfferRecord offerRecord) {
        this.offerRecord = offerRecord;
    }


    public void setOfferSender(Person offerSender) {
        this.offerSender = offerSender;
    }


    @Override
    public String toString(){
        String strReturn = String.format("""
            {
                "Record Id": "%d"
                "Game": "%s"
                "System": "%s"
                "Condition": "%s"
                "Owner": %s
            }
            """
            , gameOwnerRecordId, 
                game.getGameTitle(),
                gameSystem.getSystemName(),
                condition.getConditionLabel(),
                (owner.getFirstName() + " " + owner.getLastName()));

        return strReturn;
    }

    public String toFullString(){
        String strReturn = String.format("""
            {
                Record Id: %d
                Game: %s
                System: %s
                Condition: %s
                Owner: %s
                In Offer: %s
                Offer Record Id; %d
                Offer sender: %s
            }
            """
            , gameOwnerRecordId, 
                game.getGameTitle(),
                gameSystem.getSystemName(),
                condition.getConditionLabel(),
                owner.getFirstName(),
                isInOffer,
                offerRecord.toString(),
                offerSender.toString()
                );

        return strReturn;
    }


}
