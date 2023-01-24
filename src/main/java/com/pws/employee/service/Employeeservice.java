package com.pws.employee.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.SkillUserXrefDto;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.entity.SkillMastertable;
import com.pws.employee.entity.Skilluserxref;
import com.pws.employee.entity.User;
import com.pws.employee.exception.config.PWSException;
/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
public interface Employeeservice {

	void UserSignUp(SignUpDTO signupDTO) throws PWSException;
	
	public List<User> findUsersNameBySorting(String field) throws PWSException;
	
	public Page<User> findUserWithPaginationAndSorting(int offset,int pageSize,String field)throws PWSException;

	
	UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String  email) throws PWSException;
	
	List<SkillMastertable> fetchAllSkills() throws PWSException;
	
	Optional<SkillMastertable> fetchAllSkillsByid(Integer id) throws PWSException;
	
	void saveOrUpdateSkillUserXref(SkillUserXrefDto skilluserxrefdto) throws PWSException;

	List<Skilluserxref> fetchAllSkillsUserXref() throws PWSException;
	
	public Page<SkillMastertable> fetchAllUserSkills(int page,int pageSize, String sort, String order, int id)throws PWSException;
	
	public Page<Skilluserxref> findUserSkillXrefWithPaginationAndSorting(int offset,int pageSize,String field)throws PWSException;
	
	public Optional<Skilluserxref> fetchAllSkillUserXrefById(Integer id) throws PWSException ;
	
	void deactivateOrActivateSkillUserXref(Integer id, Boolean flag) throws PWSException;
	
	public List<Skilluserxref> fetchAllSkillsByActiveFlag(Boolean flag) throws PWSException;
}
