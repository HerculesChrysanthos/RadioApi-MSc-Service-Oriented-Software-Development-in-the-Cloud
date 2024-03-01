package gr.aueb.radio.user.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name="password", nullable = false, length = 20)
    private String password;

    @Column(name="email",unique = true, nullable = false, length = 20)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    public User() {
    }


    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }


    public Integer getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null){
            return false;
        }
        if (object == this){
            return true;
        }
        if (!(object instanceof User)) {
            return false;
        }
        User userEntity = (User) object;
        return this.username.equals(userEntity.username) && this.email.equals(userEntity.email);
    }

}
