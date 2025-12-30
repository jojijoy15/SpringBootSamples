package com.usermanagement.domain.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.domain.model.User;

@RestController
@RequestMapping(path = "/v1/users")
public class UserController {
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable Integer id) {
		
		User user = new User();
		user.setId(id);
		user.setName("Anonymous");
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

}
