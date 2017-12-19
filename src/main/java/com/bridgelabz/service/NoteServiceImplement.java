package com.bridgelabz.service;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bridgelabz.dao.NoteDao;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

@Service()
@Transactional
public class NoteServiceImplement implements NoteService {

	@Autowired
	private NoteDao noteDao;

	@Override
	public List<Note> getTheNotes(int userId) {
		List<Note> list = noteDao.getTheNotes(userId);
		List<Note> collaboratedNotes = noteDao.getTheCollaboratedNotes(userId);
		for(Note note:collaboratedNotes) {
			if(!list.contains(note))
				list.add(note);
		}	
		return list;
	}
	
	@Override
	public List<User> getAllCollaboratedUserOfNote(int noteId) {
		return noteDao.getTheCollaboratedUserOfTheNotes(noteId);
	}

	@Override
	public int deleteTheNote(Note note) {
		return noteDao.deleteTheNote(note);
	}

	@Override
	public void updateTheNote(Note note) {
		noteDao.updateTheNote(note);
	}

	@Override
	public int saveTheNote(Note note) {
		return noteDao.saveTheNote(note);
	}

	@Override
	public List<Note> getTheCollaboratedNotes(int userId) {
		return noteDao.getTheCollaboratedNotes(userId);
	}
	
	@Scheduled(fixedDelay= 30*60*1000)
	@Override
	public void trashCleaner() {
		Date deleteDay=new Date(System.currentTimeMillis()-7*24*60*60*1000);
		noteDao.deleteNotesFromTrash(deleteDay);
	}
	
	@Override
	public int emptyTrash() {
		return noteDao.emptyTrash();
	}
}
