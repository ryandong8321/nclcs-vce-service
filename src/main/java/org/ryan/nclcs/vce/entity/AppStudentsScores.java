package org.ryan.nclcs.vce.entity;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@SuppressWarnings("serial")
@Entity
@Table(name = "app_students_scores", catalog = "nclcs_vce_management")
@DynamicInsert(true)
public class AppStudentsScores implements Serializable{
	
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "app_students_scores_id", unique = true, nullable = false)
	private Integer id;
	
	/**
	 * app_students_scores_user_id
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="app_students_scores_user_id")
	private SysUsers appScoreUser;
	
	/**
	 * app_students_scores_sys_property_id
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="app_students_scores_sys_property_id")
	private SysProperties appScoreProperty;
	
	@Column(name = "app_students_scores_value", nullable = false)
	private Integer scoreValue;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public SysUsers getAppScoreUser() {
		return appScoreUser;
	}


	public void setAppScoreUser(SysUsers appScoreUser) {
		this.appScoreUser = appScoreUser;
	}


	public SysProperties getAppScoreProperty() {
		return appScoreProperty;
	}


	public void setAppScoreProperty(SysProperties appScoreProperty) {
		this.appScoreProperty = appScoreProperty;
	}


	public Integer getScoreValue() {
		return scoreValue;
	}


	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}
	
}
