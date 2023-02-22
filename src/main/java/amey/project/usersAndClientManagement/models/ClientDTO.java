package amey.project.usersAndClientManagement.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ClientDTO extends BaseDTO{
    private Long id;
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String redirectUri;
}
