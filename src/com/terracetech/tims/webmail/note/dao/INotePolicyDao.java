package com.terracetech.tims.webmail.note.dao;

import java.util.List;

import com.terracetech.tims.webmail.note.vo.NotePolicyCondVO;
import com.terracetech.tims.webmail.note.vo.NotePolicyVO;

public interface INotePolicyDao {

	public NotePolicyVO readNotePolicy(int mailUserSeq);
	
	public List<NotePolicyCondVO> readNotePolicyCondList(int policySeq);
	
	public void saveNotePolicy(NotePolicyVO notePolicyVo);
	
	public void modifyNotePolicy(NotePolicyVO notePolicyVo);
	
	public void saveNotePolicyCond(NotePolicyCondVO notePolicyCondVo);
	
	public void deleteNotePolicyCond(int mailUserSeq);
	
	public int checkNotePolicyCondMe(int mailUserSeq, int condTarget);
}
