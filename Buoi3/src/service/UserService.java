package service;
import model.User;
import java.util.ArrayList;
public class UserService {
    ArrayList<User> users;
    public UserService() {
        users = new ArrayList<>();
        users.add(new User(String.valueOf((int)(Math.random() * 1000)), "H01", "0001", "hoang1@gmail.com", "1231"));
        users.add(new User(String.valueOf((int)(Math.random() * 1000)), "H02", "0010", "hoang2@gmail.com", "1232"));
        users.add(new User(String.valueOf((int)(Math.random() * 1000)), "H03", "0011", "hoang3@gmail.com", "1233"));
        users.add(new User(String.valueOf((int)(Math.random() * 1000)), "H04", "0100", "hoang4@gmail.com", "1234"));
        users.add(new User(String.valueOf((int)(Math.random() * 1000)), "H05", "0101", "hoang5@gmail.com", "1235"));
    }

    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public void getAllUsers() {
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}