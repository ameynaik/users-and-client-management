package amey.project.usersAndClientManagement.repository;

import amey.project.usersAndClientManagement.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);
}
