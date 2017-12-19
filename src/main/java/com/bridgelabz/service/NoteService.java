package com.bridgelabz.service;

import java.util.List;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

public interface NoteService {
	
	public List<Note> getTheNotes(int userId);
	
	public List<User> getAllCollaboratedUserOfNote(int noteId);
	
	public List<Note> getTheCollaboratedNotes(int userId);
	
	public int deleteTheNote(Note note);
	
	public void updateTheNote(Note note);
	
	public int saveTheNote(Note note);
	
	public void trashCleaner();
	
	public int emptyTrash();

}
