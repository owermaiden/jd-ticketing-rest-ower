package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.entity.User;
import com.cybertek.exeption.TicketingProjectExeption;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();

    ProjectDTO save(ProjectDTO dto) throws TicketingProjectExeption;

    ProjectDTO update(ProjectDTO dto) throws TicketingProjectExeption;

    void delete(String code) throws TicketingProjectExeption;

    ProjectDTO complete(String projectCode) throws TicketingProjectExeption;

    List<ProjectDTO> listAllProjectDetails() throws TicketingProjectExeption;

    List<ProjectDTO> readAllByAssignedManager(User user);

    List<ProjectDTO> listAllNonCompletedProjects();



}
