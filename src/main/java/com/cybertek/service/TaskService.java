package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);

    int totalNonCompletedTasks(String projectCode);
    int totalCompletedTasks(String projectCode);

    void deleteByProject(ProjectDTO project);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status);

    List<TaskDTO> listAllTasksByProjectManager();

    void updateStatus(TaskDTO task);

    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> readAllByEmployee(User assignedEmployee);

}
