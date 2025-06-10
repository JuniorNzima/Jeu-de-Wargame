package gui ;

import model.Unite;
import model.Terrain;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final JTextArea infoText;
    private JTextArea logTextArea;


    public InfoPanel() {

        setBackground(Color.ORANGE);        // ✅ le fond du panneau est noir
        setOpaque(true);                   // ✅ important si le panneau est transparent

        //setPreferredSize(new Dimension(250, 500)); // taille fixe horizontale
        setLayout(new BorderLayout());

        infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(infoText), BorderLayout.CENTER);

        logTextArea = new JTextArea(30, 20);
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        setLayout(new BorderLayout());
        add(new JScrollPane(logTextArea), BorderLayout.SOUTH);
        setBorder(BorderFactory.createTitledBorder("▶ Barre d'infos"));


    }
    

    public void afficherInfos(Unite u, Terrain t) {
        logTextArea.append("----------\n");
        if (u != null) {
            logTextArea.append("Unité : " + u.getNom() + "\n");
            logTextArea.append("Joueur : J" + u.getJoueur() + "\n");
            logTextArea.append("PV : " + u.getPv() + "/" + u.getPvMax() + "\n");
            logTextArea.append("Attaque : " + u.getAttaque() + "\n");
            logTextArea.append("Défense : " + u.getDefense() + "\n");
            logTextArea.append("Déplacement : " + u.getDeplacement() + "\n");
            logTextArea.append("Portée : " + u.getPortee() + "\n");
        }

        if (t != null) {
            logTextArea.append("---- Terrain ----\n");
            logTextArea.append("Type : " + t.getNom() + "\n");
            logTextArea.append("Défense : " + t.getDefense() + "\n");
            logTextArea.append("Coût déplacement : " + t.getCoutDeplacement() + "\n");
        }

        logTextArea.append("----------\n");
    }


    private String nomJ1 = "Joueur 1";
    private String nomJ2 = "Joueur 2";

    public void setNomsJoueurs(String j1, String j2) {
        nomJ1 = j1;
        nomJ2 = j2;
    }

    public void afficherJoueurActif(int joueur) {
        String nom = (joueur == 1) ? nomJ1 : nomJ2;
        infoText.setText("Tour de : " + nom + "\n\n" + infoText.getText());
    }

    public void ajouterLog(String message) {
        logTextArea.append(message + "\n");
    }

    public void afficherTourEtJoueur(int numeroTour, int joueurActif) {
        ajouterLog("Tour n°" + numeroTour + " — Joueur " + joueurActif);
    }



}
