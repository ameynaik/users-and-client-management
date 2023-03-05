package amey.project.usersAndClientManagement.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client{
    @Id
    private Long id;
    @Nonnull
    private String clientId;
    @Nonnull
    private String clientSecret;
    @Nonnull
    private String clientName;
    @Nonnull
    private String redirectUri;

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

}
