package start;

import exceptions.DataLoadException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ResourceLoader;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main extends Application {
    private static String persistenceMode = "demo";
    private static String selectedTheme = "light";
    public static boolean isCLI = false;

    public static void main(String[] args) {

        try {
            loadPersistenceConfiguration();
            checkQueryFilesExist();
        } catch (DataLoadException e) {
            System.err.println("\n [FATAL ERROR] Impossibile avviare MyGymSpace.");
            System.err.println("Causa: " + e.getMessage());

            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("\nBENVENUTO IN MYGYMSPACE");
        System.out.println("------------------------");
        System.out.println("Scegli la modalità di esecuzione:");
        System.out.println("1. GUI (Interfaccia Grafica)");
        System.out.println("2. CLI (Riga di Comando)");
        System.out.println("-> ");

        String modeChoice = sc.nextLine();

        switch (modeChoice) {
            case "1":
                System.out.println("[INFO] Avvio modalità GUI...");
                askTheme(sc);
                launch(args);
                break;

            case "2":
                System.out.println("[INFO] Avvio la modalità CLI...");
                isCLI = true;
                startCLI();
                break;

            default:
                System.out.println("[INFO] Opzione selezionata non valida, avvio modalità CLI...");
                isCLI = true;
                startCLI();
                break;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        if (isCLI)
            return;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/GuestHomepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        String cssPath = "/css/" + selectedTheme + ".css";
        try {
            String cssUrl = Main.class.getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(cssUrl);
        } catch (NullPointerException e) {
            System.out.println("[WARNING] File CSS non trovato: " + cssPath);
        }

        stage.setTitle("MyGymSpace - Mode: " + persistenceMode.toUpperCase());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static void startCLI() {
        GuestHomepageCLI gHomeCLI = new GuestHomepageCLI();
        gHomeCLI.start();
    }

    private static void askTheme(Scanner sc) {
        System.out.println("\nScegli il tema grafico:");
        System.out.println("1. Light mode");
        System.out.println("2. Dark mode");
        System.out.println("-> ");

        String themeChoice = sc.nextLine();
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
        System.out.println("[CONFIG] Modalita di persistenza selezionata: " +  persistenceMode);
    }

    private static void checkQueryFilesExist() {
        ResourceLoader.loadProperties("/queries/booking_queries.properties");
        ResourceLoader.loadProperties("/queries/training_queries.properties");
    }

    public static String getPersistenceMode() {
        return persistenceMode;
    }
}