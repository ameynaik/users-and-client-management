package amey.project.usersAndClientManagement.securityServicesConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import amey.project.usersAndClientManagement.entities.Authority;
import amey.project.usersAndClientManagement.entities.BaseEntity;
import amey.project.usersAndClientManagement.models.AuthorityDTO;
import amey.project.usersAndClientManagement.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import amey.project.usersAndClientManagement.entities.User;
import amey.project.usersAndClientManagement.repository.UserRepository;

@Service
public class UserDetailServicesImpl implements UserDetailsManager, UserDetailsPasswordService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    private final static String SYSTEM = "System";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            final Optional<User> entity=  Optional.of(userRepository.findByUsername(username));
            if(entity.isEmpty()){
                throw new UsernameNotFoundException(username);
            }
//            String[] roles = new String[entity.get().getRoles().size()];
             UserDTO user = UserDTO.builder()
            .username(entity.get().getUsername())
            .password(entity.get().getPassword())
            .firstName(entity.get().getFirstName())
            .lastName(entity.get().getLastName())
            .build();
            user.setAuthorities(entity.get().getAuthorities().stream().map(
                    auth -> new AuthorityDTO(auth.getAuthority())
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
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails user) {
        UserDTO userCast = (UserDTO)user;
        User entity = new User();
        var baseEntity = getCurrentBaseEntity(SYSTEM);

        entity.setAuthorities(user.getAuthorities()
                .stream()
                .map(authority -> new Authority(authority.getAuthority(),baseEntity))
                .collect(Collectors.toList())
        );

        entity.setCreatedBy(baseEntity.getCreatedBy());
        entity.setCreatedDate(baseEntity.getCreatedDate());
        entity.setModifiedBy(baseEntity.getModifiedBy());
        entity.setModifiedDate(baseEntity.getModifiedDate());
        entity.setIsActive(baseEntity.getIsActive());

        entity.setFirstName(userCast.getFirstName());
        entity.setLastName(userCast.getLastName());
        entity.setUsername(userCast.getUsername());
        entity.setPassword(encoder.encode(userCast.getPassword()));
        entity.setId(1l);
        userRepository.save(entity);
    }

    @Override
    public void updateUser(UserDetails user) {
        var userCast = (UserDTO)user;
        User userEntity = userRepository.findByUsername(userCast.getUsername());
        userEntity.setFirstName(userCast.getFirstName());
        userEntity.setLastName(userCast.getLastName());
        List<Authority> authorities = user.getAuthorities()
                .stream()
                .map(auth -> new Authority(auth.getAuthority(),getCurrentBaseEntity(userCast.getModifiedBy())))
                .collect(Collectors.toList());
        userEntity.getAuthorities().addAll(authorities);
        userEntity.setPassword(encoder.encode(userCast.getPassword()));

    }

    @Override
    public void deleteUser(String username) {
        try{
            userRepository.delete(userRepository.findByUsername(username));
        }catch(Exception e){
            //Log and throw Application Exception()
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    private static BaseEntity getCurrentBaseEntity(String by){
        BaseEntity baseEntity= new BaseEntity();

        baseEntity.setCreatedBy(SYSTEM);
        baseEntity.setModifiedBy(by);
        baseEntity.setCreatedDate(LocalDate.now());
        baseEntity.setModifiedDate(LocalDate.now());
        baseEntity.setIsActive(Boolean.TRUE);
        return baseEntity;
    }

}
