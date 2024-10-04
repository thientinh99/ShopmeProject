package com.shopme.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Users;

@Repository	
 // public interface UsersRepository extends CrudRepository<Users, Integer> {
public interface UsersRepository extends CrudRepository<Users, Integer>,  PagingAndSortingRepository<Users, Integer>{
	@Query("SELECT u FROM Users u WHERE u.email = :email")
	public Users findByEmail(String email);

	public Long countById(Integer id);
	
	@Query("UPDATE Users u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean ennabled);
}
