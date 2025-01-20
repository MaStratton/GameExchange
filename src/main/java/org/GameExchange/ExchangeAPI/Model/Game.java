package org.GameExchange.ExchangeAPI.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="Gmaes")
@Table(name="Games")
public class Game {

    @Id
    @Column(name="gameId")
    private int gameId;

    @Column(name="gameTitle", nullable=false)
    private String gameTitile;

    @Column(name="publicationYear", nullable=false)
    private String publicationYar;

    public Game(String gameTitile, String publicationYar) {
        this.gameTitile = gameTitile;
        this.publicationYar = publicationYar;
    }

    public Game() {}

    public int getGameId() {
        return gameId;
    }

    public String getGameTitile() {
        return gameTitile;
    }

    public String getPublicationYar() {
        return publicationYar;
    }

}
