package amey.project.usersAndClientManagement.securityServicesConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import amey.project.usersAndClientManagement.entities.Authority;
import amey.project.usersAndClientManagement.entities.BaseEntity;
import amey.project.usersAndClientManagement.models.AuthorityDTO;
import amey.project.usersAndClientManagement.models.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import amey.project.usersAndClientManagement.entities.User;
import amey.project.usersAndClientManagement.repository.UserRepository;

@Service
@Transactional
public class UserDetailServicesImpl implements UserDetailsManager {

    UserRepository userRepository;
    PasswordEncoder encoder;
    private final static String SYSTEM = "System";
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            final Optional<User> entity=  userRepository.findByUsername(username);
            if(entity.isEmpty()){
                throw new UsernameNotFoundException(username);
            }
             UserDTO user = UserDTO.builder()
            .username(entity.get().getUsername())
            .password(entity.get().getPassword())
            .firstName(entity.get().getFirstName())
            .lastName(entity.get().getLastName())
            .build();
            user.setAuthorities(entity.get().getAuthorities().stream().map(
                    auth -> new AuthorityDTO(auth)
            ).collect(Collectors.toList()));
            user.setIsActive(entity.get().getIsActive());
            user.setCreatedBy(entity.get().getCreatedBy());
            user.setCreatedDate(entity.get().getCreatedDate());
            user.setModifiedDate(entity.get().getModifiedDate());
            user.setModifiedBy(entity.get().getModifiedBy());
            return user;
        }catch(UsernameNotFoundException e1){
            throw e1;
        }catch(Exception e){
            //logger
            System.out.println(e);
        }
        return null;
    }


    @Override
    public void createUser(UserDetails user) {
        UserDTO userCast = (UserDTO)user;
        User entity = new User();

        entity.setAuthorities(user.getAuthorities()
                .stream()
                .map(authority -> new Authority(authority.getAuthority(),SYSTEM))
                .collect(Collectors.toList())
        );

        entity.setCreatedBy(SYSTEM);
        entity.setCreatedDate(LocalDate.now());
        entity.setModifiedBy(SYSTEM);
        entity.setModifiedDate(LocalDate.now());
        entity.setIsActive(Boolean.TRUE);

        entity.setFirstName(userCast.getFirstName());
        entity.setLastName(userCast.getLastName());
        entity.setUsername(userCast.getUsername());
        entity.setPassword(encoder.encode(userCast.getPassword()));
        entity.setId(1L); // correction
        userRepository.save(entity);
    }

    @Override
    public void updateUser(UserDetails user) {
        var userCast = (UserDTO)user;
        User userEntity = userRepository.findByUsername(userCast.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        userEntity.setFirstName(userCast.getFirstName());
        userEntity.setLastName(userCast.getLastName());
        List<Authority> authorities = user.getAuthorities()
                .stream()
                .map(auth -> new Authority(auth.getAuthority(),userCast.getModifiedBy()))
                .toList();
        userEntity.getAuthorities().addAll(authorities);
        userEntity.setPassword(encoder.encode(userCast.getPassword()));

    }

    @Override
    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
//            this.logger.debug("Changing password for user '" + username + "'");
            userRepository.updateUserPassword(username,newPassword,LocalDate.now());
            Authentication authentication = this.createNewAuthentication(currentUser);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            this.securityContextHolderStrategy.setContext(context);
        }
    }
    private Authentication createNewAuthentication(Authentication currentAuth) {
        UserDetails user = this.loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user, (Object)null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

}
