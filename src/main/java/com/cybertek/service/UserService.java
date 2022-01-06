package com.cybertek.service;

import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exeption.TicketingProjectExeption;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO dto);
    UserDTO update(UserDTO dto);
    void delete(String username) throws TicketingProjectExeption;
    List<UserDTO> listAllByRole(String role);

    void deleteByUserName(String username);

    boolean checkIfUserCanBeDeleted(User user);

}
