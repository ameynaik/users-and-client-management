package amey.project.usersAndClientManagement.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class BaseEntity {

    @Column(name ="CREATED_DATE")
    @Nonnull
    private LocalDate createdDate;

    @Column(name ="CREATED_BY")
    private String createdBy;

    @Column(name ="MODIFIED_DATE")
    @Nonnull
    private LocalDate modifiedDate;

    @Column(name ="MODIFIED_DATE")
    private String modifiedBy;

    @Column(name="IS_ACTIVE")
    private Boolean isActive;

}
