package com.pws.employee.dto;

import java.util.List;

import com.pws.employee.entity.Permission;
import com.pws.employee.entity.Role;
import com.pws.employee.entity.SkillMastertable;
import com.pws.employee.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Vinayak M
 * @Date 16/01/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDetailsDTO {


    private User user;

    private List<Role> roleList;


    private List<Permission> permissionList;
    
    private List<SkillMastertable> skillist;

}
