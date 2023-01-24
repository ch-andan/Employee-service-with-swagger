package com.pws.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.SkillUserXrefDto;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.entity.Permission;
import com.pws.employee.entity.Role;
import com.pws.employee.entity.SkillMastertable;
import com.pws.employee.entity.Skilluserxref;
import com.pws.employee.entity.User;
import com.pws.employee.entity.UserRoleXref;
import com.pws.employee.exception.config.PWSException;
import com.pws.employee.repository.PermissionRepository;
import com.pws.employee.repository.RoleRepository;
import com.pws.employee.repository.SkillMasterRepository;
import com.pws.employee.repository.SkilluserXrefRepository;
import com.pws.employee.repository.UserRepository;
import com.pws.employee.repository.UserRoleXrefRepository;
import com.pws.employee.utility.CommonUtils;
import com.pws.employee.utility.DateUtils;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@Service
public class EmployeeServiceImpl implements Employeeservice {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleXrefRepository userRoleXrefRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private SkillMasterRepository skillMasterRepository;

	@Autowired
	private SkilluserXrefRepository skilluserXrefRepository;
	
	@Autowired
	private RoleRepository rolerepository;

	@Override
	public void UserSignUp(SignUpDTO signupDTO) throws PWSException {

		Optional<User> optionalUser = userRepository.findUserByEmail(signupDTO.getEmail());
		if (optionalUser.isPresent())
			throw new PWSException("User Already Exist with Email : " + signupDTO.getEmail());
		User user = new User();
		user.setDateOfBirth(DateUtils.getUtilDateFromString(signupDTO.getDateOfBirth()));
		user.setFirstName(signupDTO.getFirstName());
		user.setIsActive(true);
		user.setLastName(signupDTO.getLastName());
		user.setEmail(signupDTO.getEmail());
		user.setPhoneNumber(signupDTO.getPhoneNumber());
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		// Set new password
		user.setPassword(encoder.encode(signupDTO.getPassword()));

		userRepository.save(user);
		Optional<Role> optionalRole = rolerepository.findByRoleName(signupDTO.getRoleName());
        Role role = optionalRole.get();
        UserRoleXref userRole= new UserRoleXref();
        userRole.setRole(role);
        userRole.setUser(user);
        userRole.setIsActive(true);
        userRoleXrefRepository.save(userRole);
		
		
		
	}
	@Override
	 public List<User> findUsersNameBySorting(String field){
	        return  userRepository.findAll(Sort.by(Sort.Direction.ASC,field));
	    }
	@Override
	public Page<User> findUserWithPaginationAndSorting(int offset,int pageSize,String field){
        Page<User> users = userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        return  users;
    }


	@Override
	public UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String email) throws PWSException {
		Optional<User> optionalUser = userRepository.findUserByEmail(email);
		if (!optionalUser.isPresent())
			throw new PWSException("User Already Exist with Email : " + email);

		User user = optionalUser.get();
		UserBasicDetailsDTO userBasicDetailsDTO = new UserBasicDetailsDTO();
		userBasicDetailsDTO.setUser(user);

		List<Role> roleList = userRoleXrefRepository.findAllUserRoleByUserId(user.getId());
		userBasicDetailsDTO.setRoleList(roleList);
		List<Permission> permissionList = null;
		if (roleList.size() > 0)
			permissionList = permissionRepository.getAllUserPermisonsByRoleId(roleList.get(0).getId());

		userBasicDetailsDTO.setPermissionList(permissionList);
		List<SkillMastertable> skilllist = skilluserXrefRepository.fetchuserSkillsByid(user.getId());
        userBasicDetailsDTO.setSkillist(skilllist);
		return userBasicDetailsDTO;
	}

	@Override
	public List<SkillMastertable> fetchAllSkills() throws PWSException {
		return skillMasterRepository.findAll();
	}

	@Override
	public Optional<SkillMastertable> fetchAllSkillsByid(Integer id) throws PWSException {
		Optional<SkillMastertable> skillbyid = skillMasterRepository.findById(id);
		if (skillbyid.isPresent()) {
			return skillMasterRepository.findById(id);
		} else {
			throw new PWSException("Id not found");
		}
	}

	@Override
	public void saveOrUpdateSkillUserXref(SkillUserXrefDto skilluserxrefdto) throws PWSException {
		Optional<Skilluserxref> optskill = skilluserXrefRepository.findById(skilluserxrefdto.getId());
		Skilluserxref skilluserxref = null;
		if (optskill.isPresent()) {
			skilluserxref = optskill.get();
		} else {
			skilluserxref = new Skilluserxref();
		}
		Optional<User> optionalUser = userRepository.findById(skilluserxrefdto.getUserId());
		if (optionalUser.isPresent()) {
			skilluserxref.setUser(optionalUser.get());
		} else {
			throw new PWSException("User Doest Exist");
		}
		Optional<SkillMastertable> optionalskillMas = skillMasterRepository.findById(skilluserxrefdto.getSkillId());
		if (optionalskillMas.isPresent()) {
			skilluserxref.setSkill(optionalskillMas.get());
		} else {
			throw new PWSException("skill Doest Exist");
		}
		skilluserxref.setIsActive(skilluserxrefdto.getIsActive());
		
		
		skilluserxref.setProficiencylevel(skilluserxrefdto.getProficiencyLevel());

		skilluserXrefRepository.save(skilluserxref);
	}

	@Override
	public List<Skilluserxref> fetchAllSkillsUserXref() throws PWSException {
		return skilluserXrefRepository.findAll();
	}
	
	@Override
	public Page<Skilluserxref> findUserSkillXrefWithPaginationAndSorting(int offset, int pageSize, String field)
			throws PWSException {
		 Page<Skilluserxref> usersxref = skilluserXrefRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
	        return  usersxref;
		
	}
	
	public Page<SkillMastertable> fetchAllUserSkills(int page,int pageSize, String sort, String order, int id)throws PWSException {
        Pageable pageable=CommonUtils.getPageable(page, pageSize, sort, order);
        return skilluserXrefRepository.fetchAllUserSkills(pageable,id);
    }
	
	

	@Override
	public Optional<Skilluserxref> fetchAllSkillUserXrefById(Integer id) throws PWSException {
		Optional<Skilluserxref> skilluserxrefbyid = skilluserXrefRepository.findById(id);
		if (skilluserxrefbyid.isPresent()) {
			return skilluserXrefRepository.findById(id);
		} else {
			throw new PWSException("Id not found");
		}

	}

	@Override
	public void deactivateOrActivateSkillUserXref(Integer id, Boolean flag) throws PWSException {
		Optional<Skilluserxref> optionalUserRoleXref = skilluserXrefRepository.findById(id);
		Skilluserxref skilluserxref = optionalUserRoleXref.get();
		if (optionalUserRoleXref.isPresent()) {
			skilluserxref.setIsActive(flag);
			skilluserXrefRepository.save(skilluserxref);
		} else
			throw new PWSException("data Doest Exist");

	}

	@Override
    public List<Skilluserxref> fetchAllSkillsByActiveFlag(Boolean flag) throws PWSException {
        return  skilluserXrefRepository.fetchAllActiveSkills(flag);
        }
	
	
	}




