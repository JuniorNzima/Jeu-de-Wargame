package model;

import java.io.Serializable;

public class Archer2 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public Archer2 (int x, int y, int joueur) {
        super(x, y, joueur,
              3, 1,
              3, 3, 3,
              "Archer J2", "archer2.png");
    }

    @Override
    public boolean attaqueADistance() {
        return true;
    }
}
