package org.GameExchange.ExchangeAPI.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="GameSystems")
@Table(name="GameSystems")
public class GameSystem {

    @Id
    @Column(name="gameSystemId")
    private int gameSystemId;

    @Column(name="systemName", nullable=false)
    private String systemName;

    public GameSystem(int gameSystemId, String systemName) {
        this.gameSystemId = gameSystemId;
        this.systemName = systemName;
    }

    public GameSystem() {}

    public int getGameSystemId() {
        return gameSystemId;
    }

    public String getSystemName() {
        return systemName;
    }

}
