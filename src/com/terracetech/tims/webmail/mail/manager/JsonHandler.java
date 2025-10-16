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

import javax.mail.MessagingException;

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
 * 주요설명
 * </p>
 * <ul>
 * <li>메일함 리스트와 메일 메세지 리스트를 JSONList  로 변환 하여 주는 핸들러 클래스</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
@SuppressWarnings("all")
public class JsonHandler {

	/**
	 * <p>메일함 폴더 Tree 를 위해 메일함 정보를 JSON으로 추출한뒤 이를 Tree 형태의 JSON 리스트로 변환</p>
	 *
	 * @param beanArray			JSON List를 위한 Array. JSON Object를 저장.
	 * @param folderBeans			메일함 배열
	 * @param startPos				시작 지점 
	 * @param size					배열 사이즈
	 * @param preDepth				이전 Depth 정보
	 * @param parentName			부모 폴더명
	 * @param parentId				부모 아이디명
	 * @return int						변환 후의 진행된 지점값을 반환.
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
	 * <p>메일함 배열 정보를 가지고 JSONArray를 생성하여 반환. Tree 형태의 구조 반환.</p>
	 *
	 * @param folderBeans			변환할 메일함 배열
	 * @return JSONArray			메일함에 대한 JSONArray
	 */
	public JSONArray getJsonFolderList(MailFolderBean[] folderBeans){
		JSONArray beanArray = new JSONArray();	
		parseFolderList(beanArray,folderBeans,0,folderBeans.length,0,null,null);		
		return beanArray;
	}	
	
	/**
	 * <p>메세지 리스트를 JSON 으로 변환 하여 List 를 반환</p>
	 *
	 * @param msgBeans				변환 메세지 배열
	 * @throws MessagingException
	 * @return JSONArray				메세지 JSON List 
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
