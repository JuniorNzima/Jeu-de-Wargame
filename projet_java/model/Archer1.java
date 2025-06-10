package model;

import java.io.Serializable;

public class Archer1 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public Archer1 (int x, int y, int joueur) {
        super(x, y, joueur,
              3, 1,
              3, 3, 3,
              "Archer J1", "archer1.png");
    }

    @Override
    public boolean attaqueADistance() {
        return true;
    }
}
