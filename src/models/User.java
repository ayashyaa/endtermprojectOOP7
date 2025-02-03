package models;

public class User {
    private int id;
    private String username;
    private String password;
    private static int _id = 0;


    public User( String username, String password) {
        this.id = ++_id;
        setUsername(username);
        setPassword(password);
    }
    public User(int id, String username, String password) {
        this.id = id;
        setUsername(username);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
