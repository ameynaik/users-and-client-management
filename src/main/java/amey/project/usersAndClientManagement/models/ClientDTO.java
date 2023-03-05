package amey.project.usersAndClientManagement.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.LocalDate;
@Getter
@Setter
public class ClientDTO extends RegisteredClient {
    private LocalDate createdDate;
    private String createdBy;
    private LocalDate modifiedDate;
    private String modifiedBy;
    private Boolean isActive;
}
