package gui;

public interface GameLauncher {
    void relancerPartie();           // pour relancer après une victoire
    void lancerNouvellePartie();     // depuis le menu principal
    void chargerPartie(String chemin);
}

