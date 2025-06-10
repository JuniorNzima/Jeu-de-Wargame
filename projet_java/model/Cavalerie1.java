package model;

import java.io.Serializable;

public class Cavalerie1 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public Cavalerie1 (int x, int y, int joueur) {
        super(x, y, joueur,
              4, 3,
              4, 3, 1,
              "Cavalerie J1", "cavalerie1.png");
    }
}
