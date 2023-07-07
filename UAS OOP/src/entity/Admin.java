package entity;

public class Admin {

    String username;
    String password;



    public String getUsername(String text) {
        return username;
    }

    public String setUsername(String username) {
        this.username = username;
        return username;
    }

    public String getPassword(String password) {
        return this.password;
    }

    public String setPassword(String password) {
        this.password = password;
        return password;
    }
}
