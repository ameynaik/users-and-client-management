package amey.project.usersAndClientManagement.repository;

import amey.project.usersAndClientManagement.entities.DraftClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DraftClientRepository extends JpaRepository<DraftClient,Long> {

}
