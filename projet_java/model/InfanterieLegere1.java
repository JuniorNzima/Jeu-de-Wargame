package model;

import java.io.Serializable;

public class InfanterieLegere1 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public InfanterieLegere1 (int x, int y, int joueur) {
        super(x, y, joueur,
              3, 2,     // attaque, défense
              3, 2, 1,  // déplacement, vision, portée
              "Infanterie légère J1", "infanterie_legere1.png");
    }
}
