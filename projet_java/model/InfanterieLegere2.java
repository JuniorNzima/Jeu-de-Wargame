package model;

import java.io.Serializable;

public class InfanterieLegere2 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public InfanterieLegere2 (int x, int y, int joueur) {
        super(x, y, joueur,
              3, 2,     // attaque, défense
              3, 2, 1,  // déplacement, vision, portée
              "Infanterie légère J2", "infanterie_legere2.png");
    }
}
