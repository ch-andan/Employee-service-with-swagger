package com.pws.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.SkillMastertable;

@Repository
public interface SkillMasterRepository extends JpaRepository<SkillMastertable, Integer> {

   
    
   
}