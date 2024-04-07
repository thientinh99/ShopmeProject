package com.shopme.admin.user;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;
import com.shopme.common.exception.UserValidationException;
import java.util.regex.Pattern;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UsersRepository uRepo;

	@Autowired
	private RoleRepository roleRepo;

	public List<Users> listAllUser() {
		return (List<Users>) uRepo.findAll();
	}

	public List<Role> listAllRole() {
		return (List<Role>) roleRepo.findAll();
	}
	
	public boolean findByEmail(String email) {
		Users user = uRepo.findByEmail(email);
		return (user !=null) ? true : false;
	}
	
	public Users saveUser(Users users) {
		return uRepo.save(users);
	}
}
