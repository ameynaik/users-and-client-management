package amey.project.usersAndClientManagement.service;

import amey.project.usersAndClientManagement.entities.DraftClient;
import amey.project.usersAndClientManagement.models.ClientDTO;
import amey.project.usersAndClientManagement.models.DraftClientDTO;
import amey.project.usersAndClientManagement.repository.DraftClientRepository;
import amey.project.usersAndClientManagement.securityServicesConfiguration.KeyManager;
import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional
public class UserOnboardingServicesImpl implements UserOnboardingServices{

    @Autowired
    KeyManager keyManager;

    @Autowired
    DraftClientRepository draftClientRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public DraftClientDTO initiateClientCreation() throws Exception {
        DraftClientDTO dto = new DraftClientDTO();
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if(currentUser == null){
            //Log
            throw new AccessDeniedException("Can't create public key");
        }
        try {
            var key = keyManager.getRSAKey();
            var publicKey = key.toKeyPair().getPublic();
            var privateKey = key.toKeyPair().getPrivate();
            DraftClient draftClient = new DraftClient();
            var pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            draftClient.setPublicKey(pub);
            var pvt = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            draftClient.setPrivateKey(pvt);
//            draftClient.setPublicKey(x509EncodedKeySpec.getEncoded().toString());
//            draftClient.setPrivateKey(pkcs8EncodedKeySpec.getEncoded().toString());
            draftClient.setIsOnboarded(Boolean.FALSE);
            draftClient.setRequestedBy(currentUser.getName());
            draftClient = draftClientRepository.save(draftClient);
            if(draftClient.getId() != null){
                dto.setId(draftClient.getId());
                dto.setPublicKey(draftClient.getPublicKey());
                dto.setRequestedBy(draftClient.getRequestedBy());
                dto.setIsOnboarded(draftClient.getIsOnboarded());
            }
            else{
                //Log error getting Id;
                throw new Exception("error getting Id;");
            }
        } catch (JOSEException e) {
            throw new Exception("Error generating key");
        }
        return dto;
    }

    @Override
    public DraftClientDTO updateClientDraftStatus(DraftClientDTO dto) throws Exception {
        return null;
    }

    @Override
    public ClientDTO prepareClient(DraftClientDTO dto) throws Exception {
        Optional<DraftClient> draft = draftClientRepository.findById(dto.getId());
        ClientDTO client = null;
        if(draft.isEmpty()){
            throw new Exception("draft State not exist");
        }else{
            var  privateKey = getPrivateKey(draft.get().getPrivateKey());
            client = (ClientDTO) RegisteredClient.withId(dto.getId().toString())
                    .clientId(decrypt(dto.getClientId(),privateKey))
                    .clientSecret(decrypt(dto.getClientSecret(),privateKey))
                    .clientName(dto.getClientName())
                    .redirectUri(dto.getRedirectUri())
                    .build();
            client.setCreatedBy(dto.getRequestedBy());
            client.setModifiedBy(dto.getRequestedBy());
            client.setCreatedDate(LocalDate.now());
            client.setIsActive(Boolean.TRUE);
            client.setModifiedDate(LocalDate.now());
        }
        return client;
    }

    private PrivateKey getPrivateKey(String encodedKey) {
        var decodedKey = Base64.getDecoder().decode(encodedKey);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedKey);
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
//            Log
        }
        return privateKey;
    }

    private String decrypt(String cipherText, PrivateKey key){
        String decryptedString = null;
        try {
            // Creating a Cipher object
            var cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            // Initializing a Cipher object with private key
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decoding from Base64
            byte[] encryptedText = Base64.getDecoder().decode(cipherText.getBytes());

            // Decrypting to plain text
            decryptedString = new String(cipher.doFinal(encryptedText));

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("Exception caught while decrypting : " + ex);
        }
        return decryptedString;
    }
}
