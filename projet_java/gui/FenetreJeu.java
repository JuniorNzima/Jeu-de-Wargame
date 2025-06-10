package gui;

import util.ConsoleLogger;

import javax.swing.*;         
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import gui.InfoPanel;         
import gui.PlateauPanel;
import model.Sauvegarde;
import gui.GameLauncher;      



public class FenetreJeu extends JFrame implements GameLauncher {
    public FenetreJeu(String nomJ1, String nomJ2, boolean contreIA) {

        setTitle("Wargame Tactique");
        setSize(900, 500); // plus large
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        //getContentPane().setBackground(Color.BLACK);


        ScenarioPanel scenarioPanel = new ScenarioPanel();
        ConsoleLogger.initialiserScenario(scenarioPanel);

        InfoPanel infoPanel = new InfoPanel();
        ConsoleLogger.initialiser(infoPanel);
        infoPanel.setNomsJoueurs(nomJ1, nomJ2);

        PlateauPanel plateau = new PlateauPanel(infoPanel, nomJ1, nomJ2, contreIA, this);
        JButton boutonFinTour = new JButton("Fin du tour");

        JPanel panneauGauche = new JPanel();
        panneauGauche.setLayout(new BorderLayout());
        panneauGauche.add(infoPanel, BorderLayout.CENTER);
        panneauGauche.add(scenarioPanel, BorderLayout.SOUTH);

        //panneauGauche.setPreferredSize(new Dimension(250, 600));
        add(panneauGauche, BorderLayout.WEST);

        // ✅ Plateau au centre
        add(plateau, BorderLayout.CENTER);

        // ✅ Panneau bas avec le bouton
        
        JPanel bas = new JPanel();
        bas.add(boutonFinTour);
        add(bas, BorderLayout.SOUTH);


        JButton boutonNouvellePartie = new JButton("Nouvelle partie");
        JButton boutonAide = new JButton("Aide");
        JButton boutonQuitter = new JButton("Quitter") ;

        bas.add(boutonNouvellePartie);
        bas.add(boutonAide);
        bas.add(boutonQuitter) ;


        boutonNouvellePartie.addActionListener(e -> {
            FenetreJeu.this.dispose();
            relancerPartie(); // appel la méthode de GameLauncher
        });


        boutonQuitter.addActionListener(e -> {
            System.exit(0);
        }) ;


        boutonAide.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "🎮 Aide du jeu :\n" +
                "- La compagnie du joueur 1 est à gauche et celle du joueur 2 est à droite.\n" +
                "- Sélectionnez une unité avec un clic.\n" +
                "- Lorsque vous cliquez sur une case, toutes les informations relatives à l'unité présente sur cette case\n et le terrain sur lequel elle se trouve sont données dans la barre d'infos située à gauche du plateau.\n" +
                "- Lorsque vous sélectionnez une unité, deux zones de surbrillance apparaîssent. La bleue représente la \n zone de déplacement de l'unité et la rouge représente sa zone de portée.\n " +
                "- Cliquez sur une case pour vous déplacer ou attaquer.\n" +
                "- Lorsqu'un déplacement est impossible, on vous le fait savoir par un message dans la barre d'infos.\n" +
                "- En dessous de la barre d'infos, vous avez la barre de scénario qui vous relate le conflit qui a mené à la\n bataille.\n" +
                "- Appuyez sur 'Fin tour' quand vous avez terminé.\n" +
                "- Pour charger une partie, créez une nouvelle partie puis cliquer sur 'charger partie'.\n" +
                "- Le but est de vaincre l'adversaire ou de survivre jusqu'à un certain tour.",
                "Aide",
                JOptionPane.INFORMATION_MESSAGE
            );
        });



        boutonFinTour.addActionListener(e -> {
            plateau.finTourManuel();
        });

        JButton btnSauvegarder = new JButton("💾 Sauvegarder");
        btnSauvegarder.addActionListener(e -> {
            plateau.sauvegarderPartie("sauvegarde.sav");
        });

        JButton btnCharger = new JButton("📂 Charger partie");
        btnCharger.addActionListener(e -> {
            chargerPartie("sauvegarde.sav");
        });

        JPanel actions = new JPanel();
        actions.add(boutonFinTour);
        actions.add(btnSauvegarder);
        actions.add(btnCharger);
        actions.add(boutonNouvellePartie) ;
        actions.add(boutonAide) ;
        actions.add(boutonQuitter) ;

        bas.add(actions, BorderLayout.EAST);



        setVisible(true);
    }


    @Override
    public void relancerPartie() {
        new MenuDemarrage(this); // ou comme tu veux redémarrer
    }



    @Override
    public void chargerPartie(String chemin) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(chemin))) {
            Sauvegarde s = (Sauvegarde) in.readObject();
            chargerDepuisSauvegarde(s);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void chargerDepuisSauvegarde(Sauvegarde s) {
        getContentPane().removeAll(); // 🔄 vider l'ancienne fenêtre

        InfoPanel infoPanel = new InfoPanel();
        ScenarioPanel scenarioPanel = new ScenarioPanel();

        ConsoleLogger.initialiser(infoPanel);
        ConsoleLogger.initialiserScenario(scenarioPanel);

        infoPanel.setNomsJoueurs(s.nomJoueur1, s.nomJoueur2);

        PlateauPanel plateau = new PlateauPanel(infoPanel, s.nomJoueur1, s.nomJoueur2, s.contreIA, this);
        plateau.chargerEtat(s); // cette méthode met à jour unites, plateau, etc.

        // ✅ Partie gauche (infos + scénario)
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.add(infoPanel, BorderLayout.CENTER);
        panneauGauche.add(scenarioPanel, BorderLayout.SOUTH);

        // ✅ Partie basse (boutons)
        JButton boutonFinTour = new JButton("Fin du tour");
        boutonFinTour.addActionListener(e -> plateau.finTourManuel());

        JButton boutonSauvegarder = new JButton("💾 Sauvegarder");
        boutonSauvegarder.addActionListener(e -> plateau.sauvegarderPartie("sauvegarde.sav"));

        JButton boutonCharger = new JButton("📂 Charger partie");
        boutonCharger.addActionListener(e -> chargerPartie("sauvegarde.sav"));

        JButton boutonNouvellePartie = new JButton("Nouvelle partie");
        JButton boutonAide = new JButton("Aide");
        JButton boutonQuitter = new JButton("Quitter") ;

        boutonNouvellePartie.addActionListener(e -> {
            FenetreJeu.this.dispose();
            relancerPartie(); // appel la méthode de GameLauncher
        });


        boutonQuitter.addActionListener(e -> {
            System.exit(0);
        }) ;


        boutonAide.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "🎮 Aide du jeu :\n" +
                "- La compagnie du joueur 1 est à gauche et celle du joueur 2 est à droite.\n" +
                "- Sélectionnez une unité avec un clic.\n" +
                "- Lorsque vous cliquez sur une case, toutes les informations relatives à l'unité présente sur cette case\n et le terrain sur lequel elle se trouve sont données dans la barre d'infos située à gauche du plateau.\n" +
                "- Lorsque vous sélectionnez une unité, deux zones de surbrillance apparaîssent. La bleue représente la \n zone de déplacement de l'unité et la rouge représente sa zone de portée.\n " +
                "- Cliquez sur une case pour vous déplacer ou attaquer.\n" +
                "- Lorsqu'un déplacement est impossible, on vous le fait savoir par un message dans la barre d'infos.\n" +
                "- En dessous de la barre d'infos, vous avez la barre de scénario qui vous relate le conflit qui a mené à la\n bataille.\n" +
                "- Appuyez sur 'Fin tour' quand vous avez terminé.\n" +
                "- Pour charger une partie, créez une nouvelle partie puis cliquer sur 'charger partie'.\n" +
                "- Le but est de vaincre l'adversaire ou de survivre jusqu'à un certain tour.",
                "Aide",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bas.add(boutonFinTour);
        bas.add(boutonSauvegarder);
        bas.add(boutonCharger);
        bas.add(boutonNouvellePartie) ;
        bas.add(boutonAide) ;
        bas.add(boutonQuitter) ;

        // ✅ Placement dans la fenêtre
        add(panneauGauche, BorderLayout.WEST);
        add(plateau, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }



    @Override
    public void lancerNouvellePartie() {
        String nomJ1 = JOptionPane.showInputDialog("Nom du joueur 1");
        String nomJ2 = JOptionPane.showInputDialog("Nom du joueur 2");
        boolean contreIA = JOptionPane.showConfirmDialog(null, "Jouer contre l'IA ?") == JOptionPane.YES_OPTION;
        new FenetreJeu(nomJ1, nomJ2, contreIA);
    }


}

