package amey.project.usersAndClientManagement.controller;

import amey.project.usersAndClientManagement.models.AuthorityDTO;
import amey.project.usersAndClientManagement.models.ClientDTO;
import amey.project.usersAndClientManagement.models.UserDTO;
import amey.project.usersAndClientManagement.securityServicesConfiguration.UserDetailServicesImpl;
import amey.project.usersAndClientManagement.securityServicesConfiguration.WebClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("create")
public class UserAndClientCreation {

    @Autowired
    UserDetailServicesImpl userDetailsService;

    @Autowired
    WebClientRepository clientServices;

    @PostMapping("/user")
    public String user(@RequestBody UserDTO user){
        userDetailsService.createUser(user);
        return user.getUsername();
    }

    @PostMapping("/client")
    public String client(@RequestBody ClientDTO client){
        clientServices.saveClient(client);
        return client.getClientId();
    }

    @GetMapping("/get/{username}")
    public UserDetails sayHi(@PathVariable("username") String username){
        UserDetails obj = userDetailsService.loadUserByUsername(username);
        return obj;
    }
}
