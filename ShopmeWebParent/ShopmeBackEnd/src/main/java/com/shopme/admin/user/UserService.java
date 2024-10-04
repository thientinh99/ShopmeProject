package com.shopme.admin.user;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.constans.MessageContants;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;
import com.shopme.common.exception.UserNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 5;
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

	public Page<Users> listByPage(int numPage) {
		Pageable pageable = PageRequest.of(numPage - 1, USERS_PER_PAGE);
		return  uRepo.findAll(pageable);
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
