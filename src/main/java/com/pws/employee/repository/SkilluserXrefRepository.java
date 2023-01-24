package com.pws.employee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.SkillMastertable;
import com.pws.employee.entity.Skilluserxref;

@Repository
public interface SkilluserXrefRepository extends JpaRepository<Skilluserxref, Integer> {

	@Query("select o from Skilluserxref o where o.isActive=:flag")
	List<Skilluserxref> fetchAllActiveSkills(Boolean flag);
	
	@Query("select o.skill from Skilluserxref o where o.user.id= :id")
    List<SkillMastertable> fetchuserSkillsByid(int id);
	
	@Query("select o.skill from Skilluserxref o where o.user.id= :id")
    Page<SkillMastertable> fetchAllUserSkills(Pageable pageable, int id);
}
