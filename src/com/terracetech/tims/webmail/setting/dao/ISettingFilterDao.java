package com.terracetech.tims.webmail.setting.dao;

import java.util.List;

import com.terracetech.tims.webmail.setting.vo.FilterCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterSubCondVO;
import com.terracetech.tims.webmail.setting.vo.FilterVO;

public interface ISettingFilterDao {

	public List<FilterSubCondVO> readFilterSubcondList(int mailUserSeq, int condSeq);

	public List<FilterCondVO> readFilterCondList(int mailUserSeq);

	public FilterVO readFilter(int mailUserSeq);

	public boolean saveFilterSubcond(FilterSubCondVO vo);

	public boolean saveFilterCond(FilterCondVO vo);

	public void saveFilter(FilterVO vo);

	public int deleteFilterSubcond(int mailUserSeq, int condSeq);
	
	public int deleteFilterSubcond(int mailUserSeq, int condSeq, int[] subcondSeqs);

	public int deleteFilterCond(int mailUserSeq, int[] condSeqs);

	public int deleteFilter(int mailUserSeq);

	public boolean modifyFilterSubcond(FilterSubCondVO vo);

	public boolean modifyFilterCond(FilterCondVO vo);

	public boolean modifyFilter(FilterVO vo);

	public int readMaxFilterCondSeq(int mailUserSeq);

}