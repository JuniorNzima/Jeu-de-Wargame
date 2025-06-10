package util;

import gui.InfoPanel;
import gui.ScenarioPanel;

public class ConsoleLogger {
    private static InfoPanel infoPanel;
    private static ScenarioPanel scenarioPanel;

    public static void initialiser(InfoPanel info) {
        infoPanel = info;
    }

    public static void initialiserScenario(ScenarioPanel scenario) {
        scenarioPanel = scenario;
    }

    public static void log(String message) {
        if (infoPanel != null) {
            infoPanel.ajouterLog(message);
        } else {
            System.out.println("[INFO] " + message);
        }
    }

    public static void narrer(String message) {
        if (scenarioPanel != null) {
            scenarioPanel.ajouterTexte(message);
        } else {
            System.out.println("[SCENARIO] " + message);
        }
    }
}
