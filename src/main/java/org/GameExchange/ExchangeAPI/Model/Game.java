package org.GameExchange.ExchangeAPI.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    public Game(String gameTitile, String publicationYear) {
        this.gameTitle = gameTitile;
        this.publicationYear = publicationYear;
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
