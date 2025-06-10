package model;

import java.io.Serializable;

public class InfanterieLourde1 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public InfanterieLourde1 (int x, int y, int joueur) {
        super(x, y, joueur,
              4, 4,
              2, 2, 1,
              "Infanterie lourde J1", "infanterie_lourde1.png");
    }
}
