package org.GameExchange.ExchangeAPI.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="Systems")
@Table(name="Systems")
public class System {

    @Id
    @Column(name="systemId")
    private int systemId;

    @Column(name="systemName", nullable=false)
    private String systemName;

    public System(int systemId, String systemName) {
        this.systemId = systemId;
        this.systemName = systemName;
    }

    public System() {}

    public int getGameSystemId() {
        return systemId;
    }

    public String getSystemName() {
        return systemName;
    }

}
