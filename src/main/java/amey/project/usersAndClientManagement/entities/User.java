package amey.project.usersAndClientManagement.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

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

    //Common Columns
    @Column(name ="CREATED_DATE")
    @Nonnull
    private LocalDate createdDate;
    @Column(name ="MODIFIED_DATE")
    @Nonnull
    private LocalDate modifiedDate;
    @Column(name ="CREATED_BY")
    private String createdBy;
    @Column(name ="MODIFIED_BY")
    private String modifiedBy;
    @Column(name="IS_ACTIVE")
    private Boolean isActive;

//    private List<String> roles;
}
