package com.pws.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {

	private Integer id;

	private Boolean isActive;

	private Boolean isView;

	private Boolean isAdd;

	private Boolean isUpdate;

	private Boolean isDelete;

	private Integer moduleId;

	private Integer roleId;

}
