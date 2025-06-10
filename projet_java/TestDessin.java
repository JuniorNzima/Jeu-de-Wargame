import javax.swing.*;
import java.awt.*;

public class TestDessin extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // fond
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // cercle rouge
        g.setColor(Color.RED);
        g.fillOval(100, 100, 50, 50);

        // texte
        g.setColor(Color.BLACK);
        g.drawString("Test Unité", 95, 170);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Graphique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new TestDessin());
        frame.setVisible(true);
    }
}
