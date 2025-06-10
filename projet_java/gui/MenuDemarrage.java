package gui;

import javax.swing.*;
import java.awt.*;

public class MenuDemarrage extends JFrame {

    private final GameLauncher launcher;

    public MenuDemarrage(GameLauncher launcher) {

        this.launcher = launcher;
        setTitle("Menu du jeu");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton btnNouvelle = new JButton("Nouvelle Partie");
        JButton btnAide = new JButton("Aide");
        JButton btnQuitter = new JButton("Quitter");

        btnNouvelle.addActionListener(e -> {
            this.dispose(); // ferme le menu
            launcher.lancerNouvellePartie();
        });

        btnAide.addActionListener(e -> {
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

        btnQuitter.addActionListener(e -> System.exit(0));

        add(btnNouvelle);
        add(btnAide);
        add(btnQuitter);

        setVisible(true);
    }
}
