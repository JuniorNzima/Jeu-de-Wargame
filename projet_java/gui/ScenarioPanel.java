package gui;

import javax.swing.*;
import java.awt.*;

public class ScenarioPanel extends JPanel {
    private JTextArea texte;

    public ScenarioPanel() {

        setBackground(Color.ORANGE);        // ✅ le fond du panneau est noir
        setOpaque(true);                   // ✅ important si le panneau est transparent

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 250));

        texte = new JTextArea();
        texte.setEditable(false);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        texte.setFont(new Font("Helvetica Neue", Font.ITALIC, 14));

        add(new JScrollPane(texte), BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("📜 Scénario"));


    }

    public void afficherTexte(String message) {
        texte.setText(message);
    }

    public void ajouterTexte(String message) {
        texte.append(message + "\n");
    }
}
