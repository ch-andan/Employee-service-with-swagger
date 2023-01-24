package com.pws.employee.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.pws.employee.utility.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skilluserXref")
public class Skilluserxref extends AuditModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7554464134155859567L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "skill_id")
	private SkillMastertable skill;
	
	@Column(name="profciency_lvl", nullable=false)
	private String proficiencylevel;
	
	
//	@Enumerated(EnumType.STRING)
//    @NotNull
//    @Column(name="proficiency_level",nullable=false)
//    private Keyword  proficiencylevel;
//	public enum Keyword {
//        beginner, intermediate, expert
//    }

	
	@Column(name="is_Active", nullable=false)
	private Boolean isActive;

}
