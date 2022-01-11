package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.exeption.TicketingProjectExeption;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id) throws TicketingProjectExeption;

    List<TaskDTO> listAllTasks();

    TaskDTO save(TaskDTO dto);

    TaskDTO update(TaskDTO dto) throws TicketingProjectExeption;

    void delete(long id) throws TicketingProjectExeption;

    int totalNonCompletedTasks(String projectCode);
    int totalCompletedTasks(String projectCode);

    void deleteByProject(ProjectDTO project);

    List<TaskDTO> listAllByProject(ProjectDTO project);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectExeption;

    List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectExeption;

    TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectExeption;

//    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> readAllByEmployee(User assignedEmployee);

}
