package amey.project.usersAndClientManagement.securityServicesConfiguration;

import amey.project.usersAndClientManagement.entities.Client;
import amey.project.usersAndClientManagement.models.ClientDTO;
import amey.project.usersAndClientManagement.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
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
import java.time.LocalDate;

@Service
public class WebClientRepository implements RegisteredClientRepository {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    PasswordEncoder encoder;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    @Override
    public void save(RegisteredClient registeredClient){
        // using saveClient instead.
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        Assert.notNull(registeredClient,"Null Client Details cannot be saved");
        try{
            this.findByClientId(registeredClient.getClientId());
            throw new RuntimeException("Client with clientId : " + registeredClient.getClientId() + "exists");
        }catch(RuntimeException exception){
            // exception caused because client does not exist;
        }
        Client client = prepareEntityFromRegisteredClient(registeredClient, currentUser.getName());
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



    private Client prepareEntityFromRegisteredClient(RegisteredClient registeredClient,String username){

        Client entity = new Client();
        entity.setClientName(registeredClient.getClientName());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientSecret(encoder.encode(registeredClient.getClientSecret()));
        entity.setRedirectUri(registeredClient.getRedirectUris().stream().toList().get(0)); // saving only one redirectUris
        entity.setCreatedBy(username);
        entity.setCreatedDate(LocalDate.now());
        entity.setModifiedBy(username);
        entity.setModifiedDate(LocalDate.now());
        entity.setIsActive(Boolean.TRUE);

        return entity;
    }
    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

}
