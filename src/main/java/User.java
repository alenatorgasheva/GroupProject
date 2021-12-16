import lombok.Data;

@Data
public class User {

    private Long id;

    private String login;

    private String password;

    private ROLE role = null;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(Long id, String login, String password, ROLE role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public enum ROLE {
        USER, ADMIN
    }
}
