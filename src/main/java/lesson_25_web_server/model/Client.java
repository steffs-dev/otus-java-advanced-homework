package lesson_25_web_server.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "web_server_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    private Long id;
    private String name;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    public Client(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }
}
