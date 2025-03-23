package dev.definex.finalproject.repository;

import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.isDeleted = true WHERE t.id = :taskId")
    void softDeleteById(UUID taskId);

    List<Task> findByIsDeletedFalse();
}
