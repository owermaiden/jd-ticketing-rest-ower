package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.exeption.TicketingProjectExeption;
import com.cybertek.util.MapperUtil;
import com.cybertek.repository.ProjectRepository;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUtil mapperUtil;

    public ProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, UserService userService, TaskService taskService, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return mapperUtil.convert(project,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(obj -> mapperUtil.convert(obj,new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO save(ProjectDTO dto) throws TicketingProjectExeption {

        Project foundProject = projectRepository.findByProjectCode(dto.getProjectCode());

        if(foundProject != null){
            throw new TicketingProjectExeption("Project with this code already existing");
        }

        Project obj = mapperUtil.convert(dto,new Project());
        Project createdProject = projectRepository.save(obj);
        return mapperUtil.convert(createdProject,new ProjectDTO());

    }

    @Override
    public ProjectDTO update(ProjectDTO dto) throws TicketingProjectExeption {

        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        if(project == null){
            throw new TicketingProjectExeption("Project does not exist");
        }

        Project convertedProject = mapperUtil.convert(dto,new Project());
        Project updatedProject = projectRepository.save(convertedProject);
        return mapperUtil.convert(updatedProject,new ProjectDTO());

    }

    @Override
    public void delete(String code) throws TicketingProjectExeption {

        Project project = projectRepository.findByProjectCode(code);

        if(project == null){
            throw new TicketingProjectExeption("Project does not exist");
        }

        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() +  "-" + project.getId());
        projectRepository.save(project);
        taskService.deleteByProject(mapperUtil.convert(project,new ProjectDTO()));
    }

    @Override
    public ProjectDTO complete(String projectCode) throws TicketingProjectExeption {

        Project project = projectRepository.findByProjectCode(projectCode);

        if(project == null){
            throw new TicketingProjectExeption("Project does not exist");
        }

        project.setProjectStatus(Status.COMPLETE);
        Project completedProject = projectRepository.save(project);
        return mapperUtil.convert(completedProject,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() throws TicketingProjectExeption {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentId = Long.parseLong(id);
        User user = userRepository.findById(currentId).orElseThrow(() -> new TicketingProjectExeption("This manager does not exists"));
        List<Project> list = projectRepository.findAllByAssignedManager(user);

        if(list.size() == 0 ){
            throw new TicketingProjectExeption("This manager does not have any project assigned");
        }

        return list.stream().map(project -> {
            ProjectDTO obj = mapperUtil.convert(project,new ProjectDTO());
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTasks(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTasks(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }


    @Override
    public List<ProjectDTO> readAllByAssignedManager(User user) {
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(obj ->mapperUtil.convert(obj,new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedProjects() {

        return projectRepository.findAllByProjectStatusIsNot(Status.COMPLETE)
                .stream()
                .map(project -> mapperUtil.convert(project,new ProjectDTO()))
                .collect(Collectors.toList());
    }
}

