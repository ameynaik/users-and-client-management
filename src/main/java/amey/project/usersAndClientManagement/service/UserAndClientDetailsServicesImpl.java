package amey.project.usersAndClientManagement.service;

import amey.project.usersAndClientManagement.entities.User;
import amey.project.usersAndClientManagement.models.UserDTO;
import amey.project.usersAndClientManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

@Service
public class UserAndClientDetailsServicesImpl implements UserAndClientDetailsServices{
    @Autowired
    UserRepository userRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    public UserDTO getUserDetails(){
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        String username = authentication.getName();
        User user  = userRepository.findByUsername(username).orElseThrow();
        UserDTO dto = UserDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return dto;
    }
}
