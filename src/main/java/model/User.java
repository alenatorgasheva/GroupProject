package model;

import lombok.Data;

@Data
public class User {

    private int id;

    private String login;

    private String password;

    private ROLE role;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password, ROLE role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public enum ROLE {
        USER, ADMIN, UNKNOWN
    }
}
