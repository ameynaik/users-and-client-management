package amey.project.usersAndClientManagement.models;

import amey.project.usersAndClientManagement.entities.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;

@Setter
@Getter
public class AuthorityDTO implements GrantedAuthority {

    private String authority;
    private LocalDate createdDate;
    private String createdBy;
    private LocalDate modifiedDate;
    private String modifiedBy;
    private Boolean isActive;
    @Override
    public String getAuthority() {
        return authority;
    }

    public AuthorityDTO(String authority) {
        this.authority = authority;
    }
    public AuthorityDTO(Authority authority){
        this.authority = authority.getAuthority();
        this.createdBy = authority.getCreatedBy();
        this.createdDate = authority.getCreatedDate();
        this.modifiedBy = authority.getModifiedBy();
        this.modifiedDate = authority.getModifiedDate();
        this.isActive = authority.getIsActive();
    }
}
