package start;

import exceptions.DataLoadException;
import graphicalcontrollers.cli.HomepageCLI;
import graphicalcontrollers.gui.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.ResourceLoader;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main extends Application {
    private static final Scanner sc = new Scanner(System.in);
    private static final String SEPARATOR = "------------------------------------------------";
    private static String persistenceMode = "demo";
    private static String selectedTheme = "light";
    private static boolean isCLI = false;

    public static void main(String[] args) {

        try {
            loadPersistenceConfiguration();
            checkQueryFilesExist();
        } catch (Exception e) {
            throw new DataLoadException("Impossibile avviare MyGymSpace", e);
        }

        System.out.println("\nBENVENUTO IN MYGYMSPACE");
        System.out.println(SEPARATOR);
        System.out.println("Scegli la modalità di esecuzione:");
        System.out.println("1. GUI (Interfaccia Grafica)");
        System.out.println("2. CLI (Riga di Comando)");
        System.out.print("-> ");

        String modeChoice = sc.nextLine();

        switch (modeChoice) {
            case "1":
                System.out.println("\n[INFO] Avvio modalità GUI...\n");
                askTheme();
                launch(args);
                break;

            case "2":
                System.out.println("\n[INFO] Avvio la modalità CLI...\n");
                isCLI = true;
                startCLI();
                break;

            default:
                System.out.println("\n[INFO] Opzione selezionata non valida, avvio modalità CLI...\n");
                isCLI = true;
                startCLI();
                break;
        }
    }

    @Override
    public void start(Stage initialStage) throws IOException {
        if (isCLI())
            return;

        ViewManager.setInitialStage(initialStage);
        ViewManager.setTheme(selectedTheme);
        ViewManager.changePage("/views/GuestHomepage.fxml");
    }

    private static void startCLI() {
        HomepageCLI homeCLI = new HomepageCLI();
        homeCLI.start();
    }

    private static void askTheme() {
        System.out.println("Scegli il tema grafico:");
        System.out.println("1. Light mode");
        System.out.println("2. Dark mode");
        System.out.print("--> ");

        String themeChoice = Main.sc.nextLine();
        switch (themeChoice) {
            case "2":
                selectedTheme = "dark";
                System.out.println("[Dark Mode selezionata]");
                break;

            case "1":
            default:
                selectedTheme = "light";
                System.out.println("[Light Mode selezionata]");
                break;
        }
    }

    private static void loadPersistenceConfiguration() {
        Properties prop = ResourceLoader.loadProperties("/config/config.properties");
        persistenceMode = prop.getProperty("app.mode", "demo");
        System.out.println("[CONFIG] Modalità di persistenza selezionata: " +  persistenceMode);
    }

    private static void checkQueryFilesExist() {
        ResourceLoader.loadProperties("/queries/booking_queries.properties");
        ResourceLoader.loadProperties("/queries/training_queries.properties");
        ResourceLoader.loadProperties("/queries/user_queries.properties");
        ResourceLoader.loadProperties("/queries/schedule_queries.properties");

    }

    public static String getPersistenceMode() {
        return persistenceMode;
    }

    public static boolean isCLI() {
        return isCLI;
    }
}