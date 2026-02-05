package models.user;

import exceptions.DataLoadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAOFsys extends UserDAO{
    private static final String FILE_PATH = "src/main/resources/fsys/users.txt";
    private static final String DELIMITER = ";";
    private static final String HEADER = "username;password;firstname;lastname;type";
    private static final String PT_TYPE = "PT";

    public UserDAOFsys() {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();

        if(parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new DataLoadException("Unable to create the folder: " + parent.getAbsolutePath());
        }

        if(!file.exists()) {
            try {
                if(file.createNewFile()) {
                    initializeFile(file);
                }
            } catch (IOException e) {
                throw new DataLoadException("Unable to upload the persistence file: " + FILE_PATH, e);
            }
        }
    }

    @Override
    public User getUser(String usr, String psw) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(usr) && user.getPassword().equals(psw)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserByUsername(String usr) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(usr)) {
                return user;
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
            throw new DataLoadException("Error writing on users.txt file: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String username) {
        List<User> users = getAllUsers();

        List<User> usersToKeep = new ArrayList<>();
        boolean found  = false;
        for (User user : users) {
            if (!user.getUsername().equals(username)) {
                usersToKeep.add(user);
            } else {
                found = true;
            }
        }

        if(found) {
            saveAllUsers(usersToKeep);
        }
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        List<User> users = getAllUsers();
        boolean found = false;

        for (User user : users) {
            if(user.getUsername().equals(username)) {
                user.setPassword(newPassword);
                found = true;
                break;
            }
        }

        if(found) {
            saveAllUsers(users);
        } else {
            throw new DataLoadException("Error writing on users.txt file");
        }
    }

    @Override
    public void updateName(String username, String newFirstName, String newLastName) {
        List<User> users = getAllUsers();
        boolean found = false;

        for (User user : users) {
            if(user.getUsername().equals(username)) {
                user.setFirstName(newFirstName);
                user.setLastName(newLastName);
                found = true;
                break;
            }
        }

        if(found) {
            saveAllUsers(users);
        } else {
            throw new DataLoadException("Error writing on users.txt file");
        }
    }

    @Override
    public User fetchUserFromPersistence(String username, String type, Map<String, User> userCache) {
        User user = getUserByUsername(username);
        if(user != null) {
            userCache.put(username, user);
        }
        return user;
    }

    private void saveAllUsers(List<User> users) {
        File file = new File(FILE_PATH);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(HEADER);
            writer.newLine();

            for(User user : users) {
                writer.write(formatUserAsLine(user.getUsername(), user));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataLoadException("Error writing on users.txt file: " + e.getMessage());
        }
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("[DEBUG] File not found! Return empty list");
            return users;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().equalsIgnoreCase(HEADER)) {
                    String[] data = line.split(DELIMITER);

                    if (data.length >= 5) {
                        String usr = data[0];
                        String psw = data[1];
                        String fName = data[2];
                        String lName = data[3];
                        String type = data[4];

                        User user;
                        if (PT_TYPE.equalsIgnoreCase(type)) {
                            user = new PersonalTrainer(usr, psw, fName, lName, type);
                        } else {
                            user = new Athlete(usr, psw, fName, lName, type);
                        }
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            throw new DataLoadException("Error writing on users.txt file: " + e.getMessage());
        }
        return users;
    }

    private void initializeFile(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(HEADER);
            writer.flush();
        }
    }

    private String formatUserAsLine (String username, User user) {
        return String.join(DELIMITER, username, user.getPassword(), user.getFirstName(), user.getLastName(), user.getType());
    }
}
