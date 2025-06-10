package model;

import javax.swing.*;
import java.awt.*;

public enum Terrain {

    PLAINE1("Plaine", "img/terrain/plaine1.png", 0, 1),

    FORET1("Forêt", "img/terrain/foret1.png", 1, 2),
    FORET2("Forêt", "img/terrain/foret2.png", 1, 2),

    MONTAGNE1("Montagne", "img/terrain/montagne1.png", 2, 3),

    COLLINE1("Colline", "img/terrain/colline1.png", 1, 2),

    LAC1("Lac", "img/terrain/lac1.png", 0, 1),
    LAC2("Lac", "img/terrain/lac2.png", 0, 1),

    FORTERESSE1("Forteresse", "img/terrain/forteresse1.png", 3, 3),

    SOL1("Sol", "img/terrain/sol1.png", 0, 1),
    SOL2("Sol", "img/terrain/sol2.png", 0, 1),

    CHATEAU1("Château", "img/terrain/chateau1.png", 0, 1),
    PYRAMIDE1("Pyramide", "img/terrain/pyramide1.png", 1, 1),
    DESERT1("Desert", "img/terrain/desert1.png", 2, 2);

    private transient Image image;
    private final int defense;
    private final int coutDeplacement;
    private final String nom;

    Terrain(String nom,String chemin, int defense, int coutDeplacement) {

        this.nom = nom;
        this.image = new ImageIcon(chemin).getImage();
        this.defense = defense;
        this.coutDeplacement = coutDeplacement;
    }


    public Image getImage() {
        if (image == null) {
            image = new ImageIcon("img/terrain/" + nom.toLowerCase() + ".png").getImage();
        }
        return image;
    }


    public int getDefense() {
        return defense;
    }

    public int getCoutDeplacement() {
        return coutDeplacement;
    }

    public String getNom() {
        return this.name(); // ou un champ dédié si tu as personnalisé les noms
    }

}
