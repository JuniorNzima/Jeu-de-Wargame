package gui;

import model.Sauvegarde;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameLauncherImpl implements GameLauncher {
    @Override
    public void relancerPartie() {
        new MenuDemarrage(this);
    }

    @Override
    public void lancerNouvellePartie() {
        String nomJ1 = JOptionPane.showInputDialog("Nom du joueur 1");
        String nomJ2 = JOptionPane.showInputDialog("Nom du joueur 2");
        boolean contreIA = JOptionPane.showConfirmDialog(null, "Jouer contre l'IA ?") == JOptionPane.YES_OPTION;
        new FenetreJeu(nomJ1, nomJ2, contreIA);
    }

    @Override
    public void chargerPartie(String chemin) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(chemin))) {
            Sauvegarde s = (Sauvegarde) in.readObject();
            FenetreJeu fenetre = new FenetreJeu(s.nomJoueur1, s.nomJoueur2, s.contreIA);
            fenetre.chargerDepuisSauvegarde(s);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement : " + e.getMessage());
            e.printStackTrace();
            relancerPartie(); // revenir au menu
        }
    }
}
