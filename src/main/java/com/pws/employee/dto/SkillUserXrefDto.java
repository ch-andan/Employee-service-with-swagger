package com.pws.employee.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillUserXrefDto {
	
	
	private int id;
	
	private Integer userId;
	
	private Integer skillId;
	
	private String proficiencyLevel;
	
	private Boolean isActive;
	
}
