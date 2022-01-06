package com.cybertek.controller;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.enums.Status;
import com.cybertek.service.ProjectService;
import com.cybertek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

//    @Autowired
//    TaskService taskService;



    @GetMapping("/create")
    public String createProject(Model model){

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectService.listAllProjects());
        model.addAttribute("managers", userService.listAllByRole("Manager"));
        //model.addAttribute("managers", userService.findAll().stream().filter(user -> user.getRole().getDescription().equals("manager")).collect(Collectors.toList()));

        return "/project/create";
    }

    @PostMapping("/create")
    // public String saveUser(@ModelAttribute("user") UserDTO user, Model model){   // we dont need to use @ModelAttribute anymore......
    public String saveProject(ProjectDTO project){

        projectService.save(project);
        project.setProjectStatus(Status.OPEN);

        return "redirect:/project/create";  // redirect calls the GetMapping instead of view...
    }

    @GetMapping("/delete/{projectCode}")
    public String deleteProjectById(@PathVariable("projectCode") String projectcode){

        projectService.delete(projectcode);
        return "redirect:/project/create";  // redirect calls the GetMapping instead of view...

    }

    @GetMapping("/complete/{projectcode}")
    public String copleteProject(@PathVariable("projectcode") String projectCode){

        projectService.complete(projectCode);
        return "redirect:/project/create";
    }


    @GetMapping("/update/{projectCode}")
    public String editProject(@PathVariable("projectCode") String projectCode, Model model){

        model.addAttribute("project", projectService.getByProjectCode(projectCode));
        model.addAttribute("projects", projectService.listAllProjects());
        model.addAttribute("managers", userService.listAllByRole("manager"));

        return "/project/update";

    }

    @PostMapping("/update/{projectCode}")
    public String updateProject(ProjectDTO project){

        projectService.update(project);
        return "redirect:/project/create";  // redirect calls the GetMapping instead of view...
    }



    @GetMapping("/manager/complete")
    public String getProjectsByManager(Model model){

        List<ProjectDTO> projects = projectService.getProjectsByAssignedManager();
        model.addAttribute("projects" , projects);

        return "/manager/project-status";
    }


    @GetMapping("/manager/complete/{projectCode}")
    public String manager_completed(@PathVariable("projectCode") String projectcode){

        projectService.complete(projectcode);

        return "redirect:/project/manager/complete";
    }

}
