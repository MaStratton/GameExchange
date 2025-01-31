package org.GameExchange.ExchangeAPI.Model;

import java.util.LinkedHashMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name="Games")
@Table(name="Games")
public class Game {

    @Id
    @Column(name="gameId")
    private int gameId;

    @Column(name="gameTitle", nullable=false)
    private String gameTitle;

    @Column(name="publicationYear", nullable=false)
    private String publicationYear;

    @ManyToOne
    @JoinColumn(name="publisherId")
    Publisher publisher;

    public Game(String gameTitile, String publicationYear) {
        this.gameTitle = gameTitile;
        this.publicationYear = publicationYear;
    }
    
public LinkedHashMap<String, String> toMap(){
    LinkedHashMap<String, String> mapReturn = new LinkedHashMap<>();
    mapReturn.put("Id", String.valueOf(gameId));
    mapReturn.put("Title", gameTitle);
    mapReturn.put("Publisher", publisher.getUri());
    mapReturn.put("Publication Year", publicationYear);
    return mapReturn;
}

    public String getUri(){
        return "localhost:8080/Game/" + gameId;
    }

    public Game() {}

    public int getGameId() {
        return gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

}
