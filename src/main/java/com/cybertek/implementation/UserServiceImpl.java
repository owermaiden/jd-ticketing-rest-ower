package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exeption.TicketingProjectExeption;
import com.cybertek.mapper.UserMapper;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;
                                                                // circular dependency problem solve....@Lazy
    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, @Lazy ProjectService projectService, TaskService taskService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> users = userRepository.findAll(Sort.by("firstName"));
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDto(user) ;
    }

    @Override
    public void save(UserDTO dto) {

        User foundUser = userRepository.findByUserName(dto.getUserName());
        dto.setEnabled(true);
        User user = userMapper.convertToEntity(dto);
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO dto) {
        //Find current user from database
        User user = userRepository.findByUserName(dto.getUserName());
        // Map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(dto);
        //set id to the converted object
        convertedUser.setId(user.getId());
        //set password
        convertedUser.setPassWord(passwordEncoder.encode(convertedUser.getPassWord()));
        // set isenabled
        convertedUser.setEnabled(true);
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void delete(String username) throws TicketingProjectExeption {
        User user = userRepository.findByUserName(username);

        if (user == null) {
            throw new TicketingProjectExeption("User Does Not Exist");
        }

        if (!checkIfUserCanBeDeleted(user)){
            throw new TicketingProjectExeption("User can not be deleted... It is linked by a project or tasks");
        }

        user.setUserName(user.getUserName() + "-" + user.getId());  // to re-assign the prev username again later..we are changing the deleted one to another username actually

        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
       List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
       return  users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    // hard delete -- not good
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    @Override
    public boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()){
            case "Manager" :
                List<ProjectDTO> projectList = projectService.readAllByAssignedManager(user);
                return projectList.size() == 0;
            case "Employee" :
                List<TaskDTO> taskList = taskService.readAllByEmployee(user);
                return taskList.size() == 0;
            default:
                return true;
        }
    }
}
