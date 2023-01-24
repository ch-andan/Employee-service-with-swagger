package com.pws.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer>{

	@Query("select o from Permission o where o.role.id= :roleId")
    List<Permission> getAllUserPermisonsByRoleId(Integer roleId);
	
   

}
