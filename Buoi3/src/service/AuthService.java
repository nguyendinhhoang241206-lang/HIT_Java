package service;
import model.User;
public class AuthService {
    UserService userService = new UserService();
    public boolean login(String username, String password) {
        for(User user : userService.users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password, String email, String phoneNumber) {
        for(User user : userService.users) {
            if(username.equals(user.getUsername()) || email.equals(user.getEmail()) || phoneNumber.equals(user.getPhoneNumber())) {
                return false;
            }
        }
        userService.users.add(new User(String.valueOf((int)(Math.random() * 1000)), username, password, email, phoneNumber));
        return true;
    }
    public boolean changePassword(String username, String newPassword, String confirmNewPassword) {
        for(User user : userService.users) {
            if(username.equals(user.getUsername()) && newPassword.equals(confirmNewPassword)) {
                user.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }
}
