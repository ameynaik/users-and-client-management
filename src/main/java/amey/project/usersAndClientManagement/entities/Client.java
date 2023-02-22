package amey.project.usersAndClientManagement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Client extends BaseEntity {
    @Id
    private Long id;
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String redirectUri;

}
