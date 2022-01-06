package com.cybertek.repository;

import com.cybertek.entity.Project;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject(Project project);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);

    List<Task> findAllByProjectAssignedManager(User user);

    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);

    List<Task> findAllByAssignedEmployee(User user);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE' AND t.isDeleted = false ")
    int totalNonCompleteTask(@Param("projectCode") String projectCode);


    @Query(value = "SELECT COUNT(*) FROM tasks t JOIN projects p on p.id = t.project_id WHERE p.project_code = :projectCode AND t.task_status = 'COMPLETE' AND t.is_deleted = false", nativeQuery = true)
    int totalCompleteTask(@Param("projectCode") String projectCode);

}
