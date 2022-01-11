package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.MailDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ConfirmationToken;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.entity.User;
import com.cybertek.exeption.TicketingProjectExeption;
import com.cybertek.util.MapperUtil;
import com.cybertek.service.ConfirmationTokenService;
import com.cybertek.service.RoleService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import java.nio.file.AccessDeniedException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User Controller", description = "User API")
public class UserController {

    @Value("${app.local-url}")
    private String BASE_URL;

    private final UserService userService;
    private final MapperUtil mapperUtil;
    private final RoleService roleService;
    private final ConfirmationTokenService confirmationTokenService;

    public UserController(UserService userService, MapperUtil mapperUtil, RoleService roleService, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
        this.roleService = roleService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @PostMapping("/create-user")
    @Operation(summary = "Create new account")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectExeption {
        UserDTO createdUser = userService.save(userDTO);
        sendEmail(createEmail(createdUser));
        return ResponseEntity.ok(new ResponseWrapper("User has been created", createdUser));
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Read All Users")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<UserDTO> userDTOS = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved users", userDTOS));
    }

    @GetMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Read User")
    // Only Admin should see other profiles or current user can see his/her profile
    public ResponseEntity<ResponseWrapper> readByUsername(@PathVariable("username") String username) throws AccessDeniedException {
        UserDTO user = userService.findByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved user",user));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) throws TicketingProjectExeption, AccessDeniedException{
        UserDTO updatedUser = userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("Successfully updated",updatedUser));
    }

    @DeleteMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Delete User")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectExeption {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted"));
    }

    @GetMapping("/role")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Read by role")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readByRole(@RequestParam String role){
        List<UserDTO> userList = userService.listAllByRole(role);
        return ResponseEntity.ok(new ResponseWrapper("Successfully read users by role",userList));
    }

    // -----------------------helper methods--------------------------------------------------------------------------------------------

    private MailDTO createEmail(UserDTO userDTO){
        User user = mapperUtil.convert(userDTO,new User());

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setIsDeleted(false);
        ConfirmationToken createdConfirmationToken = confirmationTokenService.save(confirmationToken);

        return MailDTO.builder()
                .emailTo(user.getUserName())
                .token(createdConfirmationToken.getToken())
                .subject("confirm registration")
                .message("to confirm your account, please click here:")
                .url(BASE_URL + "/confirmation?token=")
                .build();
    }


    private void sendEmail(MailDTO mailDTO){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDTO.getEmailTo());
        mailMessage.setSubject(mailDTO.getSubject());
        mailMessage.setText(mailDTO.getMessage() + mailDTO.getUrl() + mailDTO.getToken());

        confirmationTokenService.sendEmail(mailMessage);
    }










}
