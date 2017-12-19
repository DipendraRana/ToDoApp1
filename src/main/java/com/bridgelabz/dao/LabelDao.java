package com.bridgelabz.dao;

import java.util.List;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;

public interface LabelDao {

	public List<Label> getAllLabels(int userId);

	public List<Note> getAllNotesOfThisLabel(Label label, int userId);

	public void deleteLabel(Label label);

	public void createLabel(Label label);

	public void updateLabel(Label label);

}
