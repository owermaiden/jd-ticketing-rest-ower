package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String projectCode);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO dto);
    ProjectDTO update(ProjectDTO dto);
    void delete(String code);
    void complete(String projectCode);
    List<ProjectDTO> getProjectsByAssignedManager();

    List<ProjectDTO> readAllByAssignedManager(User user);

    List<ProjectDTO> listAllNonCompletedProjects();



}
