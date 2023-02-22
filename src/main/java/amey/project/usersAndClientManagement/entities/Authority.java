package amey.project.usersAndClientManagement.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "AUTHORITIES")
public class Authority extends BaseEntity {
    @Id
    private Long id;
    @Nonnull
    private Long userId;
    @Nonnull
    private String authority;


    public Authority(String authority, BaseEntity baseEntity) {
        setCreatedBy(baseEntity.getCreatedBy());
        setCreatedDate(baseEntity.getCreatedDate());
        setModifiedBy(baseEntity.getModifiedBy());
        setModifiedDate(baseEntity.getModifiedDate());
        setIsActive(baseEntity.getIsActive());
        setAuthority(authority);

    }
}
