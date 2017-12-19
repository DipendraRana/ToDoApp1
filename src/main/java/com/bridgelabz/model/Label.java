package com.bridgelabz.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="label")
public class Label {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Label_ID", nullable=false)
	private int labelId;
	
	@Column(name="Label_Name" , nullable=false)
	private String labelName;
	
	@ManyToMany(mappedBy="labels" ,cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Note> notes;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "User_ID")
	@JsonIgnore
	private User user;

	public int getLabelId() {
		return labelId;
	}
	
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
