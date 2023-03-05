package amey.project.usersAndClientManagement.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Authority{
    @Id
    private Long id;
    @Nonnull
    private Long userId;
    @Nonnull
    private String authority;
    @Column(name ="CREATED_DATE")
    @Nonnull
    private LocalDate createdDate;
    @Column(name ="MODIFIED_DATE")
    @Nonnull
    private LocalDate modifiedDate;
    @Column(name ="CREATED_BY")
    private String createdBy;
    @Column(name ="MODIFIED_BY")
    private String modifiedBy;
    @Column(name="IS_ACTIVE")
    private Boolean isActive;

    public Authority(String authority, String createdBy) {
        this.authority = authority;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
        this.isActive = Boolean.TRUE;

    }
}
