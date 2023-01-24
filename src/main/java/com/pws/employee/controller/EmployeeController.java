package com.pws.employee.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pws.employee.ApiSuccess;
import com.pws.employee.dto.APIResponse;
import com.pws.employee.dto.LoginDTO;
import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.SkillUserXrefDto;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.entity.SkillMastertable;
import com.pws.employee.entity.Skilluserxref;
import com.pws.employee.entity.User;
import com.pws.employee.exception.config.PWSException;
import com.pws.employee.service.Employeeservice;
import com.pws.employee.utility.CommonUtils;
import com.pws.employee.utility.JwtUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@RestController
@RequestMapping("/")
public class EmployeeController {

	Logger log = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private Employeeservice empService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@ApiOperation(value = "User sign in")
	@PostMapping("public/signup")
	public ResponseEntity<Object> signup(@RequestBody SignUpDTO signUpDTO) throws PWSException {
		empService.UserSignUp(signUpDTO);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.CREATED));
	}
	@ApiOperation(value = "Get user details by sorting")
	@GetMapping("user/{field}")
	private APIResponse<List<User>> getUserNameWithSort(@PathVariable String field) throws PWSException {
		List<User> allusers = empService.findUsersNameBySorting(field);
		return new APIResponse<>(allusers.size(), allusers);
	}
	
	@GetMapping("user/paginationAndSort/{offset}/{pageSize}/{field}")
	private APIResponse<Page<User>> getUserWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,
			@PathVariable String field) throws PWSException {
		Page<User> productsWithPagination = empService.findUserWithPaginationAndSorting(offset, pageSize, field);
		return new APIResponse<>(productsWithPagination.getSize(), productsWithPagination);
	}
	@ApiOperation(value = "user sign in get access token")
	@PostMapping("/authenticate")
	public String generateToken(@RequestBody LoginDTO loginDTO) throws Exception {
		log.info("authentication succesfull");
		try {

			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		return jwtUtil.generateToken(loginDTO.getEmail());
	}
	@ApiOperation(value = "refresh token")
	@GetMapping("token/refresh")
	public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		String token = authorizationHeader.substring(7);
		Boolean isexp = jwtUtil.isTokenExpired(token);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && isexp != false) {
			return jwtUtil.refreshToken(token);
		} else {
			throw new PWSException("enter a expired valid token");
		}

	}
	@ApiOperation(value = "Log out api")
	@DeleteMapping("logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		String authHeader = request.getHeader(AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwttoken = authHeader.substring(7);
			jwtUtil.invalidateToken(jwttoken);
			request.getSession().removeAttribute("userDetails");
			return ResponseEntity.ok("Successfully logged out.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
		}
	}
	@ApiOperation(value = "fetch  all user basic information by email")
//	@Operation(summary = "Get a users information  by its email")
//	@ApiResponse(value = { 
//			  @ApiResponse(responseCode = "200", description = "Found the user by mail", 
//			    content = { @Content(mediaType = "application/json", 
//			      schema = @Schema(implementation =User.class)) }),
//			  @ApiResponse(responseCode = "400", description = "Invalid email supplied", 
//			    content = @Content), 
//			  @ApiResponse(responseCode = "404", description = "User not found", 
//			    content = @Content) })
	@GetMapping("private/fetch/user/basic/info")
	public ResponseEntity<Object> getUserBasicInfoAfterLoginSuccess(@RequestParam String email) throws PWSException {
		UserBasicDetailsDTO userBasicDetailsDTO = empService.getUserBasicInfoAfterLoginSuccess(email);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, userBasicDetailsDTO));
	}
	
	@ApiOperation(value = "fetch skills by id")
	@GetMapping("private/fetch/all/skills/id")
	public ResponseEntity<Object> fetchAllSkillsbyid(@RequestParam Integer id) throws Exception {
		Optional<SkillMastertable> skilllist = empService.fetchAllSkillsByid(id);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
	}
	@ApiOperation(value = "Update skill user xref details")
	@PostMapping("private/skilluserxref/save/update")
	public ResponseEntity<Object> saveorupdateuserskillxref(@RequestBody SkillUserXrefDto skilluserxrefdto)
			throws PWSException {
		empService.saveOrUpdateSkillUserXref(skilluserxrefdto);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
	}
	
	@ApiOperation(value = "pagination")
	@GetMapping("private/user/fetchall/skill")
    public ResponseEntity<Object> fetchAllUserSkills(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "DESCENDING") String order,
            @RequestParam int id) throws PWSException{
        Page<SkillMastertable> skilllist= empService.fetchAllUserSkills(page,size,sort,order,id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK,skilllist));
    }
	@ApiOperation(value = "fetch all skilluserxref details by id")
	@GetMapping("private/fetch/all/skillsxref/id")
	public ResponseEntity<Object> fetchAllSkillsuserxref(@RequestParam Integer id) throws Exception {
		Optional<Skilluserxref> skilllist = empService.fetchAllSkillUserXrefById(id);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
	}
	@ApiOperation(value = "fetch all user details")
	@GetMapping("private/fetch/all/skillsxref")
	public ResponseEntity<Object> fetchAllSkills() throws Exception {
		List<Skilluserxref> skilluserxref = empService.fetchAllSkillsUserXref();
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilluserxref));
	}
	@ApiOperation(value = "Pagination skilluserxref by offset,pagesize,field")
	@GetMapping("userxref/paginationAndSort/{offset}/{pageSize}/{field}")
	private APIResponse<Page<Skilluserxref>> getUserWithPaginationAndSortu(@PathVariable int offset, @PathVariable int pageSize,
			@PathVariable String field) throws PWSException {
		Page<Skilluserxref> productsWithPagination = empService.findUserSkillXrefWithPaginationAndSorting(offset, pageSize, field);
		return new APIResponse<>(productsWithPagination.getSize(), productsWithPagination);
	}
	@ApiOperation(value = "Activate or deactivate by skilluserxref by id")
	@PutMapping("private/skillXref/activate/deactivate")
	public ResponseEntity<Object> deactivateOrActivateAssignedRoleToUser(@RequestParam Integer id,
			@RequestParam Boolean flag) throws PWSException {
		empService.deactivateOrActivateSkillUserXref(id, flag);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
	}
	@ApiOperation(value = "Activate or deactivate skilluserxref by flag")
	@GetMapping("private/fetch/all/skillsxref/active")
	public ResponseEntity<Object> fetchAllSkillsisactive(@RequestParam Boolean flag) throws Exception {
		List<Skilluserxref> skilluserxref = empService.fetchAllSkillsByActiveFlag(flag);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilluserxref));
	}

}
