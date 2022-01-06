package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.UserMapper;
import com.cybertek.repository.ProjectRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper1, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper1;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        // project.setAssignedManager(userMapper.convertToEntity(dto.getAssignedManager()));   mapper convert this also we dont need this line actually
        projectRepository.save(project);
    }

    @Override
    public ProjectDTO update(ProjectDTO dto) {
        //Find current project from database
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());  // project from database
        // Map update project dto to entity object
        Project convertedProject = projectMapper.convertToEntity(dto);                // project updated by smbdy
        //set id to the converted object
        convertedProject.setId(project.getId());                                      // get id from old version to updated one
        // update the status again...
        convertedProject.setProjectStatus(project.getProjectStatus());                // get Status from old version to updated one
        //save updated project
        projectRepository.save(convertedProject);                                     // save new version to database

        return getByProjectCode(dto.getProjectCode());
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode()+"-"+project.getId());  // we gave deleted project a different code....So you can again use this code to create this project again...
        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project));     //
    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> getProjectsByAssignedManager() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDTO currentUserDTO = userService.findByUserName(username);
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTasks(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTasks(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(User user) {
        List<Project> projects = projectRepository.findAllByAssignedManager(user);
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedProjects() {
        return projectRepository.findAllByProjectStatusIsNot(Status.COMPLETE)
                .stream()
                .map(projectMapper::convertToDto)
                .collect(Collectors.toList());
    }

}
