package model;

import java.io.Serializable;

public class Cavalerie2 extends Unite implements Serializable {

    private static final long serialVersionUID = 1L;

    public Cavalerie2 (int x, int y, int joueur) {
        super(x, y, joueur,
              4, 3,
              4, 3, 1,
              "Cavalerie J2", "cavalerie2.png");
    }
}
