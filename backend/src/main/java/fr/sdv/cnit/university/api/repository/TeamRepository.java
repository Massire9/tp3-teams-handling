package fr.sdv.cnit.university.api.repository;

import fr.sdv.cnit.university.api.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
}
