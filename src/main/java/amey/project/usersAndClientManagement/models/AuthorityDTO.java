package amey.project.usersAndClientManagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
@Setter
@Getter
public class AuthorityDTO extends BaseDTO implements GrantedAuthority {

    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }

    public AuthorityDTO(String authority) {
        this.authority = authority;
    }
}
