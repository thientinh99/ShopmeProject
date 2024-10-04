package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.Users;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	UsersRepository usersRepository;
	
	@Test
	public void testCreateFirstUsers() {
		Users users = new Users("nguyencaothientinh99@gmail.com", "Tinh", "Nguyen Cao Thien","");
		// Users saveUsers = usersRepository.save(users);
		
		// assertThat(saveUsers.getId()).isGreaterThan(0);;
	}

	@Test
	public void testCreateUsersWithRole() {
		Users users = new Users("nguyencaothientinh99@gmail.com", "Tinh", "Nguyen Cao Thien","");
		// Users saveUsers = usersRepository.save(users);
		
		Role roleAssistant = new Role(3);
		
		users.addRole(roleAssistant);
		
		// assertThat(saveUsers.getId()).isGreaterThan(0);;
	}
	
	@Test
	public boolean testCheckUniqueEmail(String email) {
		Users saveUsers = usersRepository.findByEmail(email);
		return saveUsers == null;
		
	}
	@Test
	public void testListPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Users> page = usersRepository.findAll(pageable);
		List<Users> lstUser = page.getContent();
		lstUser.forEach(user -> System.out.println(user));
		assertThat(lstUser.size()).isEqualTo(pageSize);
	}
}
