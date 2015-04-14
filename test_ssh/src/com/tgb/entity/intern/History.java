package com.tgb.entity.intern;




import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tgb.entity.Student;

@Entity
@Table(name="history")
public class History {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name="history_id", length=11)
	private int history_id;
	
	@Column(name = "matched_time", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date matched_time;
	
	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean is_interviewed;
	
	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean is_hired;
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Student student;
	
	/**************************************************GETTERS AND SETTERS**********************************************************************************/

	public int getHistory_id() {
		return history_id;
	}

	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}

	public Date getMatched_time() {
		return matched_time;
	}

	public void setMatched_time(Date matched_time) {
		this.matched_time = matched_time;
	}

	public boolean isIs_interviewed() {
		return is_interviewed;
	}

	public void setIs_interviewed(boolean is_interviewed) {
		this.is_interviewed = is_interviewed;
	}

	public boolean isIs_hired() {
		return is_hired;
	}

	public void setIs_hired(boolean is_hired) {
		this.is_hired = is_hired;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	

	
}
