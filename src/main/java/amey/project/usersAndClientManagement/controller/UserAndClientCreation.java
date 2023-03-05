package amey.project.usersAndClientManagement.controller;

import amey.project.usersAndClientManagement.entities.Client;
import amey.project.usersAndClientManagement.models.ClientDTO;
import amey.project.usersAndClientManagement.models.DraftClientDTO;
import amey.project.usersAndClientManagement.models.UserDTO;
import amey.project.usersAndClientManagement.securityServicesConfiguration.UserDetailServicesImpl;
import amey.project.usersAndClientManagement.securityServicesConfiguration.WebClientRepository;
import amey.project.usersAndClientManagement.service.UserOnboardingServices;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("create")
public class UserAndClientCreation {

    @Autowired
    UserDetailServicesImpl userDetailsService;

    @Autowired
    WebClientRepository clientServices;

    @Autowired
    UserOnboardingServices userOnboardingServices;

    @PostMapping("/user")
    public String user(@RequestBody UserDTO user, @PathParam("verificationCode") String verificationCode){
        userDetailsService.createUser(user);
        return user.getUsername();
    }

    @PostMapping("/client")
    public String client(@RequestBody DraftClientDTO dto){
        RegisteredClient client = null;
        try {
            client = userOnboardingServices.prepareClient(dto);
            userOnboardingServices.updateClientDraftStatus(dto);
            clientServices.save(client);
            return client.getClientId();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e);
        }
    }

    @GetMapping("/get/{username}")
    public UserDetails sayHi(@PathVariable("username") String username){
        UserDetails obj = userDetailsService.loadUserByUsername(username);
        return obj;
    }
    @GetMapping("/initiateClientCreation")
    public DraftClientDTO initiateUserCreation(){
        try{
            return userOnboardingServices.initiateClientCreation();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e);
        }
    }

}
