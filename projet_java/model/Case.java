package model ;

import java.io.Serializable;

public class Case implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Terrain terrain;

    public Case(Terrain terrain) {
        this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
