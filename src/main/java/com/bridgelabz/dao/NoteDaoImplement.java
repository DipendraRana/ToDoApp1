package com.bridgelabz.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;

@Repository
public class NoteDaoImplement implements NoteDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Date date = new Date();

	@Override
	public List<Note> getTheNotes(int userId) {
		User user = (User) sessionFactory.getCurrentSession().createQuery("from User where id=:User_Id")
				.setParameter("User_Id", userId).uniqueResult();
		user.getNotes().size();
		return user.getNotes();
	}

	@Override
	public List<User> getTheCollaboratedUserOfTheNotes(int noteId) {
		Note note = (Note) sessionFactory.getCurrentSession().createQuery("from Note where id=:Note_Id")
				.setParameter("Note_Id", noteId).uniqueResult();
		note.getCollaboratedUser().size();
		return note.getCollaboratedUser();
	}

	@Override
	public List<Note> getTheCollaboratedNotes(int userId) {
		@SuppressWarnings("unchecked")
		List<Note> notes = sessionFactory.getCurrentSession().createQuery(
				"select note from Note as note join note.collaboratedUser as collaboration where collaboration.id=:User_Id")
				.setParameter("User_Id", userId).list();
		notes.size();
		return notes;
	}

	@Override
	public int deleteTheNote(Note note) {
		return sessionFactory.getCurrentSession().createQuery("delete from Note where noteId=:Note_Id")
				.setParameter("Note_Id", note.getNoteId()).executeUpdate();
	}

	@Override
	public void updateTheNote(Note note) {
		note.setEditedDate(date);
		sessionFactory.getCurrentSession().update(note);
	}

	@Override
	public int saveTheNote(Note note) {
		note.setEditedDate(date);
		return (int) sessionFactory.getCurrentSession().save(note);
	}

	@Override
	public void deleteNotesFromTrash(Date deleteDay) {
		sessionFactory.getCurrentSession()
				.createQuery("delete from Note where editedDate<:Delete_Day and trashed = true")
				.setParameter("Delete_Day", deleteDay).executeUpdate();
	}

	public int emptyTrash() {
		return sessionFactory.getCurrentSession().createQuery("delete from Note where trashed = true").executeUpdate();
	}

}
