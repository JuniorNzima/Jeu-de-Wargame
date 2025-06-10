package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Sauvegarde implements Serializable {

    private static final long serialVersionUID = 1L;

    public Case[][] plateau;
    public ArrayList<Unite> unites;
    public int joueurActif;
    public int numeroTour;
    public boolean contreIA;
    public String nomJoueur1;
    public String nomJoueur2;
}
