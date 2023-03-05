package amey.project.usersAndClientManagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import amey.project.usersAndClientManagement.entities.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE amey.project.usersAndClientManagement.entities.User u SET u.password = :newPassword, u.modifiedBy = :username, u.modifiedDate = :date WHERE u.username = :username")
    void updateUserPassword(@Param("username") String username, @Param("newPassword")String newPassword, @Param("date")LocalDate modifiedDate);
}