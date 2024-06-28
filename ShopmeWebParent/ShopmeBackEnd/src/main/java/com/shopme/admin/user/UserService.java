package com.shopme.admin.user;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.constans.MessageContants;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;
import com.shopme.common.exception.UserNotFoundException;
import com.shopme.common.exception.UserValidationException;

import jakarta.transaction.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	MessageContants msg = new MessageContants();
	@Autowired
	private UsersRepository uRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Users> listAllUser() {
		return (List<Users>) uRepo.findAll();
	}

	public List<Role> listAllRole() {
		return (List<Role>) roleRepo.findAll();
	}

	public String findByEmail(Integer id, String email) {
		Users user = uRepo.findByEmail(email);
		if (user == null)
			return "OK";
		boolean isCreating = (id == null);
		if (isCreating) {
			if (user != null)
				return "DUPLICATED";
		} else {
			if (user.getId() != null)
				return "OK";
		}
		return "OK";
	}

	public Users saveUser(Users users) {
		boolean isUpdatingUser = (users.getId() != null);
		if (isUpdatingUser) {
			Users isUserExist = uRepo.findById(users.getId()).get();
			if (isUserExist.getPassword().isEmpty() || isUserExist.getPassword().isBlank()) {
				isUserExist.setPassword(isUserExist.getPassword());
			} else {
				passEncode(users);
			}
		} else {
			passEncode(users);
		}

		return uRepo.save(users);
	}

	private void passEncode(Users users) {
		String encodePassword = passwordEncoder.encode(users.getPassword());
		users.setPassword(encodePassword);
	}

	public Users findbyId(Integer id) throws UserNotFoundException {
		try {
			return uRepo.findById(id).get();
		} catch (UserNotFoundException ex) {
			throw new UserNotFoundException(MessageFormat.format(msg.WARUSR00002, id));
		}

	}

	public void delete(Integer id) {
		Long countById = uRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException(MessageFormat.format(msg.WARUSR00002, id));

		}
		uRepo.deleteById(id);
	}
	public void updateStatus(Integer id, boolean enabled) {
		uRepo.updateEnableStatus(id, enabled);
	}
}
