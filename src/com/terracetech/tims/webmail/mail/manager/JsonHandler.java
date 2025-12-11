/**
 * JsonFolderHandler.java 2008. 11. 28.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.manager;

import java.util.Arrays;
import java.util.List;

import jakarta.mail.MessagingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.terracetech.tims.mail.TMailUtility;
import com.terracetech.tims.mail.search.TMailSearchQuery;
import com.terracetech.tims.mail.tag.TMailTag;
import com.terracetech.tims.webmail.mail.ibean.MailFolderBean;
import com.terracetech.tims.webmail.mail.ibean.MailMessageBean;
import com.terracetech.tims.webmail.mail.vo.SharedFolderUserVO;
import com.terracetech.tims.webmail.mail.vo.SharedFolderVO;

/**
 * <p>
 * <strong>JsonFolderHandler.java</strong> Class Description
 * </p>
 * <p>
 * �ֿ伳��
 * </p>
 * <ul>
 * <li>������ ����Ʈ�� ���� �޼��� ����Ʈ�� JSONList  �� ��ȯ �Ͽ� �ִ� �ڵ鷯 Ŭ����</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("all")
public class JsonHandler {

	/**
	 * <p>������ ���� Tree �� ���� ������ ������ JSON���� �����ѵ� �̸� Tree ������ JSON ����Ʈ�� ��ȯ</p>
	 *
	 * @param beanArray			JSON List�� ���� Array. JSON Object�� ����.
	 * @param folderBeans			������ �迭
	 * @param startPos				���� ���� 
	 * @param size					�迭 ������
	 * @param preDepth				���� Depth ����
	 * @param parentName			�θ� ������
	 * @param parentId				�θ� ���̵��
	 * @return int						��ȯ ���� ����� �������� ��ȯ.
	 */
	public int parseFolderList(JSONArray beanArray, MailFolderBean[] folderBeans,
			int startPos, int size, int preDepth, String parentName,
			String parentId) {

		JSONObject jObj = null;

		int idx = startPos;
		int listSize = 0;
		String pName = null;
		String cid = null;
		for (; idx < size;) {
			folderBeans[idx].setParentId(parentId);
			pName = folderBeans[idx].getParentFullName();
			cid = folderBeans[idx].getId();

			JSONArray subList = null;
			int depth = folderBeans[idx].getDepth();

			if (preDepth > depth) {
				break;
			}
			jObj = folderBeans[idx].toJson();

			if (preDepth < depth) {
				JSONObject preObj = null;
				subList = new JSONArray();
				idx = parseFolderList(subList, folderBeans, idx++, size, depth,
						pName, cid);
				
				if(beanArray.size() > 0){
					preObj = (JSONObject) beanArray.get(listSize - 1);
					preObj.put("child", subList);
					beanArray.set(listSize - 1, preObj);
				}				

			} else {
				beanArray.add(jObj);
				listSize++;
			}

			idx++;
		}

		jObj = null;
		pName = null;

		return --idx;
	}

	/**
	 * <p>������ �迭 ������ ������ JSONArray�� �����Ͽ� ��ȯ. Tree ������ ���� ��ȯ.</p>
	 *
	 * @param folderBeans			��ȯ�� ������ �迭
	 * @return JSONArray			�����Կ� ���� JSONArray
	 */
	public JSONArray getJsonFolderList(MailFolderBean[] folderBeans){
		JSONArray beanArray = new JSONArray();	
		parseFolderList(beanArray,folderBeans,0,folderBeans.length,0,null,null);		
		return beanArray;
	}	
	
	/**
	 * <p>�޼��� ����Ʈ�� JSON ���� ��ȯ �Ͽ� List �� ��ȯ</p>
	 *
	 * @param msgBeans				��ȯ �޼��� �迭
	 * @throws MessagingException
	 * @return JSONArray				�޼��� JSON List 
	 */
	public JSONArray getJsonMessageList(MailMessageBean[] msgBeans)
			throws MessagingException {
		JSONArray list = new JSONArray();
		
		int size = msgBeans.length;
		for (int i = 0; i < size; i++) {
			list.add(msgBeans[i].toJsonForList());
		}		
		return list;		
	}
	
	public JSONArray getJsonTagList(TMailTag[] tags) {
		JSONArray list = new JSONArray();
		int size = 0;
		if(tags != null){
			Arrays.sort(tags);
			size = tags.length;
		}
		JSONObject tempObj = null;
		for (int i = 0; i < size; i++) {
			tempObj = new JSONObject();
			tempObj.put("id", tags[i].getId());
			tempObj.put("name", tags[i].getName());
			tempObj.put("color", tags[i].getColor());
			list.add(tempObj);
		}
		
		return list;
	}
	
	public JSONArray getJsonSearchFolderList(TMailSearchQuery[] queries) {
		JSONArray list = new JSONArray();
		int size = 0;
		if(queries != null){
			Arrays.sort(queries);
			size = queries.length;
		}
		JSONObject tempObj = null;
		for (int i = 0; i < size; i++) {
			tempObj = new JSONObject();
			tempObj.put("id", queries[i].getId());
			tempObj.put("name", queries[i].getName());
			tempObj.put("query", queries[i].getQuery());
			list.add(tempObj);
		}
		
		return list;
	}
	
	public JSONArray getJsonSharedFolderList(List<SharedFolderVO> sfolderList) {
		JSONArray list = new JSONArray();
		JSONObject tempObj = null;
		if(sfolderList != null && sfolderList.size() > 0){
			for (SharedFolderVO vo : sfolderList) {
				tempObj = new JSONObject();
				tempObj.put("encname", vo.getFolderName());
				tempObj.put("name", vo.getPrintFolderName());
				tempObj.put("id", vo.getFolderUid());
				tempObj.put("userid", vo.getSharedUserSeq());
				tempObj.put("username", vo.getSharedUserName());
				tempObj.put("useruid", vo.getSharedUserId());
				list.add(tempObj);
			}			
		}		
		return list;
	}
	
	public JSONArray getJsonSharedFolderReaders(List<SharedFolderUserVO> readers) {
		JSONArray list = new JSONArray();
		JSONObject tempObj = null;
		if(readers != null && readers.size() > 0){
			for (SharedFolderUserVO vo : readers) {
				tempObj = new JSONObject();				
				tempObj.put("readerUserSeq",vo.getUserSeq());
				tempObj.put("readerId", vo.getUserId());
				tempObj.put("readerName", vo.getUserName());
				list.add(tempObj);
			}			
		}		
		return list;
	}
	
}
