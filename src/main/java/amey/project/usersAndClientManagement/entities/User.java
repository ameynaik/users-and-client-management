package amey.project.usersAndClientManagement.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private List<Authority> authorities;


//    private List<String> roles;
}
