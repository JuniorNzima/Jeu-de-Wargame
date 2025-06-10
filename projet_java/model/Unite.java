package model ;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.io.Serializable;

import util.ConsoleLogger;

public abstract class Unite implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int x, y;
    protected int joueur;
    protected int pv;
    protected int attaque ;
    protected int defense ;
    protected int deplacement;
    protected int deplacementRestant;
    protected int vision ;
    protected int portee ;
    protected String nom ;
    protected String cheminImage ;
    protected transient Image image ;


    private boolean aBougeCeTour = false;
    private boolean aEteAttaquee = false;


    public Unite(int x, int y, int joueur, int attaque, int defense, int deplacement, int vision, int portee, String nom, String cheminImage) {
        this.x = x;
        this.y = y;
        this.joueur = joueur;
        this.attaque = attaque;
        this.defense = defense;
        this.deplacement = deplacement;
        this.deplacementRestant = deplacement;
        this.vision = vision;
        this.portee = portee;
        this.nom = nom;
        this.cheminImage = cheminImage;
        this.pv = 10; // Valeur par défaut ou personnalisable
        ConsoleLogger.log("Unité créée : " + getNom());
    }

    

    public String getNom() { return nom; }
    public int getAttaque() { return attaque; }
    public int getDefense() { return defense; }
    public int getDeplacement() { return deplacement; }
    public int getPortee() { return portee; }
    public int getVision() { return vision; }
    public boolean attaqueADistance() { return false; }  // Par défaut (sauf pour archer/mage)

    public Image getImage() {
        if (image == null && cheminImage != null) {
            image = Toolkit.getDefaultToolkit().getImage("img/unite/" + cheminImage);
        }
        return image;
    }


    public int getPvMax() { return 10; }  // Valeur fixe ou stockée dans un champ

    public int getX() { return x; }
    public int getY() { return y; }
    public int getJoueur() { return joueur; }
    public int getPv() { return pv; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void subirDegats(int attaque, int bonusDefense) {
        int base = attaque - this.getDefense();
        int alea = (int)(Math.random() * 3) - 1;
        int total = Math.max(1, base - bonusDefense + alea);
        this.pv = Math.max(0, this.pv - total);
        ConsoleLogger.log(getNom() + " perd " + total + " PV.");
    }


    public boolean estMort() {
        return pv <= 0;
    }

    
    public boolean aBougeCeTour() { 
        return aBougeCeTour; 
    }

    public boolean aEteAttaquee() { 
        return aEteAttaquee; 
    }

    public void setABouge(boolean b) { 
        this.aBougeCeTour = b; 
    }

    public void setAEteAttaquee(boolean b) { 
        this.aEteAttaquee = b; 
    }

    public void resetTour() {
        aBougeCeTour = false;
        aEteAttaquee = false;
    }

    public void recupererPv() {
        int max = getPvMax();
        if (pv < max) {
            pv = Math.min(max, pv + 2);
            ConsoleLogger.log(getNom() + " récupère 2 PV.");
        }
    }


    public int getDeplacementRestant() {
        return deplacementRestant;
    }

    public void consommerDeplacement(int cout) {
        deplacementRestant -= cout;
    }

    public void debutTour() {
        this.deplacementRestant = this.deplacement;
    }



}










    

