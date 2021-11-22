package com.alvarolongueira.managerservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alvarolongueira.managerservice.exception.user.UserNotFoundException;
import com.alvarolongueira.managerservice.security.UserApplication;
import com.alvarolongueira.managerservice.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping
	public UserApplication login(@RequestParam("user") String userName, @RequestParam("pass") String pass) throws UserNotFoundException {
		return this.loginService.login(userName, pass);
	}

}
