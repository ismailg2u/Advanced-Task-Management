package dev.definex.finalproject.repository;

import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.isDeleted = true WHERE p.id = :projectId")
    void softDeleteById(UUID projectId);

    List<Project> findByIsDeletedFalse();
}
