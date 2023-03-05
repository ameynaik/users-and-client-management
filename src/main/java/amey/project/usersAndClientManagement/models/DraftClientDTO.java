package amey.project.usersAndClientManagement.models;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class DraftClientDTO {
    private Long id;
    private String clientName;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String publicKey;
    private String requestedBy;
    private Boolean isOnboarded;
}
