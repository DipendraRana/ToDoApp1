package com.bridgelabz.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "notes")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Note_id")
	private int noteId;

	@Column(name = "Note_Title", nullable = false)
	private String noteTitle;

	@Column(name = "Note_Description", nullable = false)
	private String noteDescription;

	@Column(name = "Edited_Date", nullable = false)
	private Date editedDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Note_Label", joinColumns = { @JoinColumn(name = "Note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "Label_ID") })
	@Fetch(FetchMode.SELECT)
	private List<Label> labels;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "User_ID")
	private User user;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Collaborated", joinColumns = { @JoinColumn(name = "Note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "User_ID") })
	@Fetch(FetchMode.SELECT)
	private List<User> collaboratedUser;

	@Column(name = "Trashed", nullable = false)
	private boolean trashed;

	@Column(name = "Archived", nullable = false)
	private boolean archived;

	@Column(name = "Pinned", nullable = false)
	private boolean pinned;

	@Column(name = "Is_Labeled", nullable = false)
	private boolean labeled;

	@Column(name = "Is_Reminded", nullable = false)
	private boolean reminder;

	@Column(name = "Reminder_Date", nullable = true)
	private Date reminderDate;

	@Column(name = "Reminder_Time", nullable = true)
	private String reminderTime;

	@Column(name = "Color", nullable = true)
	private String color;

	@Column(name = "Image", columnDefinition = "LONGBLOB")
	private String image;

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteDescription() {
		return noteDescription;
	}

	public void setNoteDescription(String noteDescription) {
		this.noteDescription = noteDescription;
	}

	public Date getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public boolean isLabeled() {
		return labeled;
	}

	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	public boolean isReminder() {
		return reminder;
	}

	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<User> getCollaboratedUser() {
		return collaboratedUser;
	}

	public void setCollaboratedUser(List<User> collaboratedUser) {
		this.collaboratedUser = collaboratedUser;
	}

}
