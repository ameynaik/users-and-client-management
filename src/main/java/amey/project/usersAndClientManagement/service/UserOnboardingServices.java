package amey.project.usersAndClientManagement.service;

import amey.project.usersAndClientManagement.models.DraftClientDTO;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface UserOnboardingServices {
    public DraftClientDTO initiateClientCreation() throws Exception;
    public DraftClientDTO updateClientDraftStatus(DraftClientDTO dto) throws Exception;

    public RegisteredClient prepareClient(DraftClientDTO dto) throws Exception;
}
