package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.entity.User;
import com.cybertek.entity.common.AuthenticationRequest;
import com.cybertek.exeption.TicketingProjectExeption;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.service.UserService;
import com.cybertek.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Authentication Controler", description = "Authenticate API")
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final MapperUtil mapperUtil;
	private final JWTUtil jwtUtil;

	public LoginController(AuthenticationManager authenticationManager, UserService userService, MapperUtil mapperUtil, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.mapperUtil = mapperUtil;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/authenticate")
	@DefaultExceptionMessage(defaultMessage = "Bad credentials")
	@Operation(summary = "Login to application")
	public ResponseEntity<ResponseWrapper> doLogin(@RequestBody AuthenticationRequest authenticationRequest) throws TicketingProjectExeption {

		String password = authenticationRequest.getPassword();
		String username = authenticationRequest.getUsername();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,password);
		authenticationManager.authenticate(authentication);

		UserDTO foundUser = userService.findByUserName(username);
		User convertedUser = mapperUtil.convert(foundUser,new User());

		if (!foundUser.isEnabled()){
			throw new TicketingProjectExeption("Please verify your user");
		}

		String jwtToken = jwtUtil.generateToken(convertedUser);

		return ResponseEntity.ok(new ResponseWrapper("Login Successful", jwtToken));
	}

}
