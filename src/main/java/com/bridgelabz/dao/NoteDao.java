package com.bridgelabz.dao;

import java.util.Date;
import java.util.List;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

public interface NoteDao {
	
	public List<Note> getTheNotes(int userId);
	
	public List<User> getTheCollaboratedUserOfTheNotes(int noteId);
	
	public List<Note> getTheCollaboratedNotes(int userId);
	
	public int deleteTheNote(Note note);
	
	public void updateTheNote(Note note);
	
	public int saveTheNote(Note note);
	
	public void deleteNotesFromTrash(Date deleteDay);
	
	public int emptyTrash();

}
