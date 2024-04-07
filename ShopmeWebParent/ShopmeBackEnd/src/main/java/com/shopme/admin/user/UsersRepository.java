package com.shopme.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Integer> {
	Users findByEmail(String email);
}
