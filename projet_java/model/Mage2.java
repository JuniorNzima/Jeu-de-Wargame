package model;

import java.io.Serializable;

public class Mage2 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public Mage2 (int x, int y, int joueur) {
        super(x, y, joueur,
              6, 2,     // attaque, défense
              3, 4, 2,  // déplacement, vision, portée
              "Mage J2", "mage2.png");
    }

    @Override
    public boolean attaqueADistance() {
        return true;
    }
}
