// PlateauPanel.java (version avec grille hexagonale + textures terrain)
package gui;

import model.*;

import util.ConsoleLogger;


import java.applet.AudioClip;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class PlateauPanel extends JPanel {

    private int numeroTour = 1;
    private final int tourLimite = 50; // Par exemple
    private final int joueurDefenseur = 1; // Celui qui doit survivre jusqu’au bout



    private final List<String> messagesScenario = Arrays.asList(
        "Le camp J1 , bien retranché dans une forteresse, doit défendre son territoire contre l'armée du camp J2, qui cherche à conquérir cette position stratégique...\n" ,
        "L'aube se lève sur un champ de bataille encore silencieux...\n",
        "Des éclaireurs rapportent des mouvements ennemis au nord...\n",
        "Le vent porte l'écho des tambours de guerre...\n",
        "Une troupe ennemie s'approche du flanc est...\n",
        "Un cor retentit dans les collines. Les renforts sont-ils là ?\n",
        "Deux armées rivales se rencontrent à la jonction de deux rivières. Le contrôle de cette zone est crucial pour les ressources...\n" ,
        "Le sol tremble. Une unité massive approche du sud...\n",
        "Des cris s'élèvent. L'assaut est imminent...\n",
        "Le sang a coulé. Mais l'espoir subsiste...\n",
        "Le camp J1 prépare une invasion de la plaine contrôlée par le camp J2, qui est en infériorité numérique...\n" ,
        "Camp J1, en difficulté, doit se retirer vers des collines tout en préservant ses forces face à un ennemi supérieur en nombre (Camp J2)...\n" ,
        "Le sort de la bataille se joue maintenant...\n",
        "Tenez bon ! La victoire est à portée de main...\n"
    );
    private int indexMessageScenario = 0;



    private int offsetX = 0;
    private int offsetY = 0;


    //private final int RAYON = 30; // distance du centre à un sommet
    private final int RAYON = 35; // Distance du centre à un coin
    private final double WIDTH = 2 * RAYON;
    private final double HEIGHT = Math.sqrt(3) * RAYON;
    private final double VERT_SPACING = HEIGHT;        // Espacement vertical entre lignes
    private final double HORZ_SPACING = 0.75 * WIDTH;  // Espacement horizontal entre colonnes

    private final double HAUTEUR = Math.sqrt(3) * RAYON;
    private final double LARGEUR = 2 * RAYON;
    private final double ESPACEMENT_X = 0.75 * LARGEUR;

    private final int TAILLE_HEX = 40;
    private final int NB_LIGNES = 15;
    private final int NB_COLONNES = 7;



    private final Case[][] plateau = new Case[NB_LIGNES][NB_COLONNES];
    private final ArrayList<Unite> unites = new ArrayList<>();
    private Unite uniteSelectionnee = null;
    private final InfoPanel infoPanel;
    private final String nomJoueur1;
    private final String nomJoueur2;
    private final boolean contreIA;
    private final GameLauncher gameLauncher;
    private boolean partieTerminee = false;
    private int joueurActif = 1;

    // Pour effets visuels
    private final ArrayList<Unite> unitesTouchees = new ArrayList<>();
    private final ArrayList<Unite> unitesEnMort = new ArrayList<>();
    private final Map<Unite, Integer> transparences = new HashMap<>();
    private Unite derniereUniteDeplacee = null;
    private int effetDeplacementCompteur = 0;
    private int pulse = 0;
    private boolean pulseGrowing = true;

    public PlateauPanel(InfoPanel infoPanel, String nomJ1, String nomJ2, boolean contreIA, GameLauncher launcher) {
        this.infoPanel = infoPanel;
        this.nomJoueur1 = nomJ1;
        this.nomJoueur2 = nomJ2;
        this.contreIA = contreIA;
        this.gameLauncher = launcher;

        setBackground(new Color(55, 20, 153));        // ✅ le fond du panneau est noir
        setOpaque(true);                   // ✅ important si le panneau est transparent

        setPreferredSize(new Dimension(
            (int)(NB_COLONNES * ESPACEMENT_X + RAYON),
            (int)(NB_LIGNES * HAUTEUR + HAUTEUR / 2)
        ));


        // Init plateau avec types de terrain aléatoires
        Terrain[] types = Terrain.values();
        for (int i = 0; i < NB_LIGNES; i++) {
            for (int j = 0; j < NB_COLONNES; j++) {
                Terrain t = types[(int) (Math.random() * types.length)];
                plateau[i][j] = new Case(t);
            }
        }

        // Unités joueur 1
        unites.add(new InfanterieLegere1(1, 4, 1));
        unites.add(new Archer1(2, 4, 1));
        unites.add(new Mage1(3, 4, 1));
        unites.add(new InfanterieLourde1(4, 4, 1));
        unites.add(new Cavalerie1(5, 4, 1));

        // Unités ennemies supplémentaires (joueur 2)
        unites.add(new InfanterieLegere2(1, 10, 2));
        unites.add(new InfanterieLourde2(2, 10, 2));
        unites.add(new Cavalerie2(3, 10, 2));
        unites.add(new Archer2(4, 10, 2));
        unites.add(new Mage2(5, 10, 2));


        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (partieTerminee) return;

                Point pos = getHexClicked(e.getX(), e.getY());
                if (pos == null) return;

                int col = pos.x;
                int lig = pos.y;

                handleClick(pos.x, pos.y);


                // Mise à jour des infos
                if (infoPanel != null) {
                    Unite selectionnee = uniteSelectionnee; // peut être null après action
                    Terrain terrain = (selectionnee != null)
                        ? plateau[selectionnee.getY()][selectionnee.getX()].getTerrain()
                        : plateau[lig][col].getTerrain();

                    infoPanel.afficherInfos(selectionnee, terrain);
                    infoPanel.afficherJoueurActif(joueurActif);
                }

                if (!partieTerminee && contreIA && joueurActif == 2) {
                    jouerTourIA();
                }

                repaint();
            }
        });


        new Timer(100, e -> {
            pulse = pulseGrowing ? pulse + 1 : pulse - 1;
            if (pulse >= 5) pulseGrowing = false;
            if (pulse <= 0) pulseGrowing = true;
            repaint();
        }).start();

        new Timer(300, e -> {
            if (!unitesTouchees.isEmpty()) {
                unitesTouchees.clear();
                repaint();
            }
        }).start();

        new Timer(100, e -> {
            if (!unitesEnMort.isEmpty()) {
                for (Unite u : new ArrayList<>(unitesEnMort)) {
                    int alpha = transparences.getOrDefault(u, 255) - 40;
                    if (alpha <= 0) {
                        unites.remove(u);
                        unitesEnMort.remove(u);
                        transparences.remove(u);
                    } else {
                        transparences.put(u, alpha);
                    }
                }
                repaint();
            }
        }).start();

        new Timer(150, e -> {
            if (effetDeplacementCompteur > 0) {
                effetDeplacementCompteur--;
                repaint();
            }
        }).start();


        new Timer(10000, e -> {
            if (!partieTerminee && indexMessageScenario < messagesScenario.size()) {
                ConsoleLogger.narrer(messagesScenario.get(indexMessageScenario));
                indexMessageScenario++;
            }
        }).start();


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Taille réelle du plateau en pixels
        double totalWidth = (NB_LIGNES - 1) * HORZ_SPACING + WIDTH;
        double totalHeight = (NB_COLONNES - 1) * VERT_SPACING + HEIGHT;

        // Centrage horizontal + vertical
        offsetX = (int) ((getWidth() - totalWidth) / 2.0);
        offsetY = (int) ((getHeight() - totalHeight) / 2.0);

        for (int lig = 0; lig < NB_LIGNES; lig++) {
            for (int col = 0; col < NB_COLONNES; col++) {
                Polygon hex = createHex(col, lig);
                Image img = plateau[lig][col].getTerrain().getImage();
                g2.setClip(hex);
                g2.drawImage(img, hex.getBounds().x, hex.getBounds().y, hex.getBounds().width, hex.getBounds().height, this);
                g2.setClip(null);
                g2.setColor(Color.WHITE);
                g2.drawPolygon(hex);
            }

        }



        for (Unite u : unites) {
            Polygon hex = createHex(u.getX(), u.getY());
            Point center = getHexCenter(hex);
            int x = center.x - 25;
            int y = center.y - 25;

            if (unitesTouchees.contains(u)) {
                g.setColor(Color.RED);
                g.fillRect(x + 10, y + 10, 30, 30);
            }

            Graphics2D g2d = (Graphics2D) g;
            if (transparences.containsKey(u)) {
                int alpha = transparences.get(u);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
            } else {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }


            if (uniteSelectionnee != null) {
                List<Point> zoneDeplacement = casesAccessibles(uniteSelectionnee);
                List<Point> zonePortee = casesAPortee(uniteSelectionnee);

                // Ne garder en rouge que les cases de portée qui ne sont pas déjà dans la zone de déplacement
                List<Point> porteeHorsDeplacement = zonePortee.stream()
                    .filter(p -> !zoneDeplacement.contains(p))
                    .toList();

                // 🔵 Zone de déplacement
                g2.setColor(new Color(0, 0, 255, 40)); // bleu transparent
                for (Point p : zoneDeplacement) {
                    Polygon hex2 = createHex(p.y, p.x); // y = ligne, x = colonne
                    g2.fillPolygon(hex2);
                }

                // 🔴 Zone de portée hors déplacement
                g2.setColor(new Color(255, 0, 0, 40)); // rouge transparent
                for (Point p : porteeHorsDeplacement) {
                    Polygon hex2 = createHex(p.y, p.x);
                    g2.fillPolygon(hex2);
                }
            }


            g2d.drawImage(u.getImage(), x + 2, y + 2, 48, 48, this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // PV sous forme de barre verte
            int pvMax = u.getPvMax();
            int pvActuel = u.getPv();
            int barWidth = 30;
            int filled = (int) ((pvActuel / (double) pvMax) * barWidth);

            g.setColor(Color.GREEN);
            g.fillRect(x + 10, y - 5, filled, 4);

            g.setColor(Color.RED);
            g.fillRect(x + 10 + filled, y - 5, barWidth - filled, 4);

            g.setColor(Color.BLACK);
            g.drawRect(x + 10, y - 5, barWidth, 4);

        }
    }


    private Polygon createHex(int row, int col) {
        double x0 = col * HORZ_SPACING + offsetX;
        double y0 = row * HEIGHT + (col % 2) * (HEIGHT / 2.0) + offsetY;

        int[] x = new int[6];
        int[] y = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 180 * (60 * i);
            x[i] = (int) (x0 + RAYON * Math.cos(angle));
            y[i] = (int) (y0 + RAYON * Math.sin(angle));
        }

        return new Polygon(x, y, 6);
    }





    private Point getHexCenter(Polygon hex) {
        int sumX = 0, sumY = 0;
        for (int i = 0; i < hex.npoints; i++) {
            sumX += hex.xpoints[i];
            sumY += hex.ypoints[i];
        }
        return new Point(sumX / 6, sumY / 6);
    }

    private Point getHexClicked(int px, int py) {
        for (int i = 0; i < NB_LIGNES; i++) {
            for (int j = 0; j < NB_COLONNES; j++) {
                Polygon hex = createHex(j, i);
                if (hex.contains(px, py)) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }


    private Unite getUniteAt(int x, int y) {
        for (Unite u : unites) {
            if (u.getX() == x && u.getY() == y) return u;
        }
        return null;
    }


    private boolean estOccupee(int x, int y) {
        return getUniteAt(x, y) != null;
    }

    private boolean estAdjacent(Unite a, Unite b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy == 1);
    }

    private boolean estAPortee(Unite a, Unite b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return dx + dy <= a.getPortee();
    }

    private void jouerSon(String chemin) {
        try {
            java.applet.AudioClip clip = java.applet.Applet.newAudioClip(new java.io.File(chemin).toURI().toURL());
            clip.play();
        } catch (Exception e) {
            ConsoleLogger.log("Erreur lecture son : " + e.getMessage());
        }
    }


    private void jouerTourIA() {
        ConsoleLogger.log("Tour de l'IA...");

        ArrayList<Unite> iaUnites = new ArrayList<>();
        for (Unite u : unites) {
            if (u.getJoueur() == 2) iaUnites.add(u);
        }

        new Thread(() -> {
            for (Unite ia : iaUnites) {
                if (partieTerminee) break;

                try {
                    Thread.sleep(500); // Pause entre les actions
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Étape 1 : attaquer si possible
                Unite cible = trouverCibleAPortee(ia);
                if (cible != null) {
                    ConsoleLogger.log("IA attaque avec " + ia.getNom());

                    int defense = plateau[cible.getY()][cible.getX()].getTerrain().getDefense();
                    cible.subirDegats(ia.getAttaque(), defense);

                    if (cible.estMort()) {
                        unites.remove(cible);
                        transparences.put(cible, 255);
                        unitesEnMort.add(cible);
                        jouerSon("sounds/death.wav");
                    } else {
                        unitesTouchees.add(cible);
                        jouerSon("sounds/hit.wav");
                    }

                    repaint();
                    continue;
                }

                // Étape 2 : se rapprocher de l’unité ennemie la plus proche
                Unite plusProche = trouverPlusProcheEnnemi(ia);
                if (plusProche != null) {
                    int dx = Integer.compare(plusProche.getX(), ia.getX());
                    int dy = Integer.compare(plusProche.getY(), ia.getY());

                    int newX = ia.getX() + dx;
                    int newY = ia.getY() + dy;

                    // Vérifie que la case est dans la grille et vide
                    if (estDansGrille(newX, newY) && getUniteAt(newX, newY) == null) {
                        ia.setPosition(newX, newY);
                        derniereUniteDeplacee = ia;
                        effetDeplacementCompteur = 3;
                        ConsoleLogger.log("IA déplace " + ia.getNom() + " vers (" + newX + "," + newY + ")");
                        repaint();
                    }
                }

            }

            // 🛏️ Récupération des PV pour les unités IA (joueur 2)
            for (Unite u : unites) {
                if (u.getJoueur() == 2 && !u.aBougeCeTour() && !u.aEteAttaquee()) {
                    u.recupererPv();
                }
                u.resetTour(); // pour tous
            }

            // Fin du tour IA
            joueurActif = 1;
            numeroTour++;

            try {
                Thread.sleep(500); // ✅ petite pause finale pour laisser les logs apparaître
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // ✅ Maintenant on affiche le nouveau tour
            SwingUtilities.invokeLater(() -> {
                infoPanel.afficherTourEtJoueur(numeroTour, joueurActif);
            });

        }).start();

    }



    private Unite trouverCibleAPortee(Unite ia) {
        for (Unite u : unites) {
            if (u.getJoueur() == 1 && estAPortee(ia, u)) return u;
        }
        return null;
    }

    private Unite trouverPlusProcheEnnemi(Unite ia) {
        Unite plusProche = null;
        int distanceMin = Integer.MAX_VALUE;

        for (Unite u : unites) {
            if (u.getJoueur() == 1) {
                int dist = Math.abs(u.getX() - ia.getX()) + Math.abs(u.getY() - ia.getY());
                if (dist < distanceMin) {
                    distanceMin = dist;
                    plusProche = u;
                }
            }
        }
        return plusProche;
    }

    private boolean estDansGrille(int x, int y) {
        return x >= 0 && x < plateau[0].length && y >= 0 && y < plateau.length;
    }


    public void finTourManuel() {
        if (partieTerminee) return;

        // Passer au joueur suivant
        joueurActif = (joueurActif == 1) ? 2 : 1;

        // Incrémenter le numéro de tour quand on revient à J1
        if (joueurActif == 1) {
            numeroTour++;
        }

        // Mettre à jour l'affichage
        infoPanel.afficherTourEtJoueur(numeroTour, joueurActif);

        // Réinitialiser les unités du joueur actif
        for (Unite u : unites) {
            if (u.getJoueur() == joueurActif) {
                u.debutTour();
            }
        }

        uniteSelectionnee = null;

        // ✅ Condition 1 : Victoire par survie
        if (numeroTour >= tourLimite) {
            boolean defenseurEnVie = unites.stream().anyMatch(u -> u.getJoueur() == joueurDefenseur);
            if (defenseurEnVie) {
                afficherFinDePartie("🎉 Victoire du défenseur (joueur " + joueurDefenseur + ")");
            } else {
                afficherFinDePartie("🏴 Victoire de l'attaquant !");
            }
            return;
        }


        // ✅ Condition 2 : Victoire par élimination
        boolean j1Vivant = unites.stream().anyMatch(u -> u.getJoueur() == 1);
        boolean j2Vivant = unites.stream().anyMatch(u -> u.getJoueur() == 2);

        if (!j1Vivant || !j2Vivant) {
            int gagnant = j1Vivant ? 1 : 2;
            String nomGagnant = (gagnant == 1 ? nomJoueur1 : nomJoueur2);
            afficherFinDePartie("🏆 Victoire du joueur " + gagnant + " (" + nomGagnant + ") !");
            return;
        }


        // ✅ Tour IA si nécessaire
        if (contreIA && joueurActif == 2) {
            jouerTourIA();
        }

        repaint();

        //afficherFinDePartie("Test de victoire fictive");

    }


    private void afficherFinDePartie(String message) {
        partieTerminee = true;

        int reponse = JOptionPane.showOptionDialog(
            this,
            message + "\nSouhaitez-vous rejouer ?",
            "🎮 Partie terminée",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Rejouer", "Quitter"},
            "Rejouer"
        );

        if (reponse == JOptionPane.YES_OPTION) {
            FenetreJeu fenetre = (FenetreJeu) SwingUtilities.getWindowAncestor(this);
            fenetre.dispose();
            gameLauncher.relancerPartie();  // ✅ Doit exister dans FenetreJeu
        } else {
            System.exit(0);
        }
    }





    private List<Point> casesAccessibles(Unite unite) {
        List<Point> accessibles = new ArrayList<>();
        int[][] couts = new int[NB_LIGNES][NB_COLONNES];
        for (int[] row : couts) Arrays.fill(row, Integer.MAX_VALUE);

        Queue<Point> file = new LinkedList<>();
        int ux = unite.getX(), uy = unite.getY();
        file.add(new Point(ux, uy));
        couts[uy][ux] = 0;

        while (!file.isEmpty()) {
            Point p = file.poll();
            int x = p.x, y = p.y;
            int coutActuel = couts[y][x];

            for (int[] dir : new int[][] { {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,1}, {1,-1} }) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (estDansGrille(nx, ny)) {
                    int cout = plateau[ny][nx].getTerrain().getCoutDeplacement();
                    int total = coutActuel + cout;
                    if (total <= unite.getDeplacement() && total < couts[ny][nx]) {
                        couts[ny][nx] = total;
                        file.add(new Point(nx, ny));
                        if (!(nx == ux && ny == uy)) {
                            accessibles.add(new Point(nx, ny));
                        }
                    }
                }
            }
        }

        return accessibles;
    }


    private List<Point> casesAPortee(Unite unite) {
        List<Point> resultat = new ArrayList<>();
        int portee = unite.getPortee();

        for (int dx = -portee; dx <= portee; dx++) {
            for (int dy = -portee; dy <= portee; dy++) {
                int tx = unite.getX() + dx;
                int ty = unite.getY() + dy;
                if (estDansGrille(tx, ty)) {
                    int distance = Math.abs(dx) + Math.abs(dy);
                    if (distance <= portee && distance > 0) {
                        resultat.add(new Point(tx, ty));
                    }
                }
            }
        }

        return resultat;
    }


    private void handleClick(int col, int lig) {
        Unite cliquee = getUniteAt(col, lig);

        if (uniteSelectionnee == null && cliquee != null && cliquee.getJoueur() == joueurActif) {
            uniteSelectionnee = cliquee;
            ConsoleLogger.log("Unité sélectionnée : " + cliquee.getNom());
            return;
        }

        if (uniteSelectionnee != null) {
            if (cliquee != null && cliquee.getJoueur() == joueurActif) {
                uniteSelectionnee = cliquee;
                ConsoleLogger.log("Nouvelle unité sélectionnée : " + cliquee.getNom());
                repaint();
                return;
            }

            List<Point> accessibles = casesAccessibles(uniteSelectionnee);
            boolean accessible = accessibles.stream().anyMatch(p -> p.x == col && p.y == lig);
            if (!accessible) {
                ConsoleLogger.log("Case trop loin ou terrain trop difficile !");
                return;
            }

            Unite cible = getUniteAt(col, lig);
            if (cible != null && cible.getJoueur() != uniteSelectionnee.getJoueur()) {
                int defense = plateau[lig][col].getTerrain().getDefense();
                cible.subirDegats(uniteSelectionnee.getAttaque(), defense);
                cible.setAEteAttaquee(true);

                if (cible.estMort()) {
                    ConsoleLogger.log(cible.getNom() + " est mort !");
                    jouerSon("sounds/death.wav");
                    transparences.put(cible, 255);
                    unitesEnMort.add(cible);
                    uniteSelectionnee.setPosition(col, lig);
                } else {
                    ConsoleLogger.log(cible.getNom() + " subit une attaque.");
                    jouerSon("sounds/hit.wav");
                    unitesTouchees.add(cible);
                }

                uniteSelectionnee = null;
            } 
            
            else if (cible == null) {
                int cout = plateau[lig][col].getTerrain().getCoutDeplacement();

                if (uniteSelectionnee.getDeplacementRestant() < cout) {
                    ConsoleLogger.log("Cette unité n'a plus assez de points de déplacement.");
                    return;
                }

                uniteSelectionnee.consommerDeplacement(cout);
                uniteSelectionnee.setPosition(col, lig);
                ConsoleLogger.log("Déplacement vers (" + col + "," + lig + ") — PD restants : " + uniteSelectionnee.getDeplacementRestant());
                uniteSelectionnee = null;
            }

        }
    }



    public void sauvegarderPartie(String chemin) {
        Sauvegarde s = new Sauvegarde();
        s.plateau = plateau;
        s.unites = unites;
        s.joueurActif = joueurActif;
        s.numeroTour = numeroTour;
        s.contreIA = contreIA;
        s.nomJoueur1 = nomJoueur1;
        s.nomJoueur2 = nomJoueur2;

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(chemin))) {
            out.writeObject(s);
            ConsoleLogger.log("✅ Partie sauvegardée !");
        } catch (Exception e) {
            ConsoleLogger.log("❌ Erreur de sauvegarde : " + e.getMessage());
        }
    }


    public void chargerEtat(Sauvegarde s) {
        // Écrase les données
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                plateau[i][j] = s.plateau[i][j];
            }
        }

        this.unites.clear();
        this.unites.addAll(s.unites);

        // Recharger les images des unités
        for (Unite u : this.unites) {
            u.getImage(); // recharge l'image si elle est null
        }

        // Recharger les images de terrain
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                plateau[i][j].getTerrain().getImage();
            }
        }

        this.joueurActif = s.joueurActif;
        this.numeroTour = s.numeroTour;
    }





    
}
