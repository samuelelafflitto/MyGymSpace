package models.user;

import exceptions.DataLoadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO_Fsys extends UserDAO{
    private static final String FILE_PATH = "/fsys/users.txt";
    private static final String DELIMITER = ";";
    private static final String HEADER = "firstname;lastname;username;password;type";
    private static final String PT_TYPE = "PT";
    private static final String ATHLETE_TYPE = "ATHLETE";

    public UserDAO_Fsys() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
    }

    @Override
    public User getUser(String usr, String psw) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(usr) && user.getPassword().equals(psw)) {
                return populateUser(user);
            }
        }
        return null;
    }

    @Override
    public User getUserByUsername(String usr) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(usr)) {
                return populateUser(user);
            }
        }
        return null;
    }

    @Override
    public void addUser(String username, User user) {
        File file = new File(FILE_PATH);
        boolean exists = file.exists();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            if (!exists) {
                bufferedWriter.write(HEADER);
                bufferedWriter.newLine();
            }

            String line = formatUserAsLine(username, user);
            bufferedWriter.write(line);
            bufferedWriter.newLine();

        }catch (IOException e) {
            throw new DataLoadException("Errore di scrittura su file users.txt: " + e.getMessage());
        }
    }

    /*@Override
        public void addUser(String fName, String lName, String usr, String psw) {
            File file = new File(FILE_PATH);
            boolean fileExists = file.exists();

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
                if (!fileExists) {
                    bufferedWriter.write(HEADER);
                    bufferedWriter.newLine();
                }

                String line = formatUserAsLine(fName, lName, usr, psw, ATHLETE_TYPE);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            } catch (IOException e) {
                throw new DataLoadException("Errore scrittura su file users.txt: " + e.getMessage());
            }
        }*/

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (file.exists()) {
            return users;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty() || line.equalsIgnoreCase(HEADER)) {
                    continue;
                }
                String[] data = line.split(DELIMITER);

                // Salta le righe che non hanno tutti i valori inseriti
                if (data.length < 5) {
                    continue;
                }

                String fName = data[0];
                String lName = data[1];
                String usr = data[2];
                String psw = data[3];
                String type = data[4];

                User user;
                if (PT_TYPE.equals(type)) {
                    user = new PersonalTrainer(fName, lName, usr, psw, type);
                } else {
                    user = new Athlete(fName, lName, usr, psw, type);
                }
                users.add(user);
            }
        } catch (IOException e) {
            throw new DataLoadException("Errore lettura su file users.txt: " + e.getMessage());
        }
        return users;
    }

    private String formatUserAsLine (String username, User user) {
        return String.join(DELIMITER, user.getFirstName(), user.getLastName(), username, user.getPassword(), user.getType());
    }

    // Nella versione su Fsys non realizziamo le relazioni complesse che includono Training o Bookings
    // Viene solo creato e popolato il file users.txt.
    private User populateUser(User user) {
        if (user instanceof PersonalTrainer) {
            ((PersonalTrainer) user).setTraining(null);
        } else if (user instanceof Athlete) {
            ((Athlete) user).setBookings(new ArrayList<>());
        }
        return user;
    }
}
