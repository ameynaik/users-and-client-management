package amey.project.usersAndClientManagement.securityServicesConfiguration;

import amey.project.usersAndClientManagement.entities.Client;
import amey.project.usersAndClientManagement.models.ClientDTO;
import amey.project.usersAndClientManagement.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;

@Service
public class WebClientRepository implements RegisteredClientRepository {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    PasswordEncoder encoder;


    @Override
    public void save(RegisteredClient registeredClient) {
        // using saveClient instead.
    }
    public void saveClient(ClientDTO clientDto){
        Assert.notNull(clientDto,"Null Client Details cannot be saved");
        Client client = getClientFromDTO(clientDto);
        clientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        var client = clientRepository.findById(Long.parseLong(id));
        return getRegisteredClient(client.orElseThrow(() ->
                new RuntimeException("Client with uniqueId : " + id + " not found.")));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client  = clientRepository.findByClientId(clientId);
        return getRegisteredClient(client.orElseThrow(() ->
                new RuntimeException("Client with clientId : " + clientId + " not found.")));
    }
    private RegisteredClient getRegisteredClient(Client client){
        var registeredClient  = RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientName(client.getClientName())
                .redirectUri(client.getRedirectUri())//below settings will remain same for all clients.
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope(OidcScopes.OPENID)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .refreshTokenTimeToLive(Duration.ofHours(2))
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .build())
                .build();
        return registeredClient;
    }



    private Client getClientFromDTO(ClientDTO clientDTO){
        Client client = Client.builder()
                .clientName(clientDTO.getClientName())
                .clientId(clientDTO.getClientId())
                .clientSecret(encoder.encode(clientDTO.getClientSecret()))
                .redirectUri(clientDTO.getRedirectUri())
                .build();
        client.setCreatedBy(clientDTO.getCreatedBy());
        client.setIsActive(clientDTO.getIsActive());
        client.setModifiedBy(clientDTO.getModifiedBy());
        client.setModifiedDate(clientDTO.getModifiedDate());
        return client;
    }

}
