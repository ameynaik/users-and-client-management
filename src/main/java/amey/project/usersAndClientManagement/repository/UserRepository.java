package amey.project.usersAndClientManagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import amey.project.usersAndClientManagement.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
    User findByUsername(String username);
}