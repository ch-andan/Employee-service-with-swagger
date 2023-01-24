package com.pws.employee.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pws.employee.utility.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SkillMaster_table")
public class SkillMastertable extends AuditModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1148840981176363739L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_id")
	private Integer id;

	@Column(name = "skillname", length = 50, nullable = false, unique = true)
	private String skillname;

	@Column(name = "is_Active", length = 50, nullable = false)
	private Boolean isActive;

}
