package amey.project.usersAndClientManagement.controller;

import amey.project.usersAndClientManagement.models.UserDTO;
import amey.project.usersAndClientManagement.service.UserAndClientDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class UserAndClientDetails {

    @Autowired
    UserAndClientDetailsServices userAndClientDetailsServices;

    @GetMapping("/userDetails")
    public UserDTO getUserDetails(){
        return userAndClientDetailsServices.getUserDetails();
    }
}
