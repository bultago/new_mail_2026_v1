/**
 * MessageSortInfoBean.java 2008. 11. 28.
 * 
 * Copyright 2008-2009 Daou tech Inc.
 *
 * Tims7 Project Source File
 * Development by Terrace Dev. WEB Dev.
 * 
 */
package com.terracetech.tims.webmail.mail.ibean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.mail.Flags;
import jakarta.mail.Message;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.BodyTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.RecipientStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

import com.terracetech.tims.mail.AttachTerm;
import com.terracetech.tims.mail.MyselfSearchTerm;
import com.terracetech.tims.mail.TMailFolder;
import com.terracetech.tims.webmail.mail.manager.MessageHandler;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.Validation;

/**
 * <p>
 * <strong>MessageSortInfoBean.java</strong> Class Description
 * </p>
 * <p>
 * 주요설명
 * </p>
 * <ul>
 * <li>메세지 리스트를 Sort할 정보 저장 클래스</li>
 * <li>각 조건을 가지고 Sort 하기위한 SearchTerm 객체 반환</li>
 * </ul>
 * 
 * @author sshyun
 * @since Tims7
 * @version 7.0
 */
public class MessageSortInfoBean {

    private String pattern = null;

    private boolean advanceMode = false;
    private String operation = "and";
    private String adFromEmailPattern = null;
    private String adToEmailPattern = null;
    private String adSearchPattern = null;
    private String adSearchCategory = null;
    private String userEmail = null;
    private String fromDate = null;
    private String toDate = null;
    private String date = null;
    private String sortBy = null;
    private String sortDir = null;
    private String pageBase = null;
    private String page = null;

    private boolean seenFlag = false;
    private boolean unseenFlag = false;
    private boolean flagedFlag = false;
    private boolean attachFlag = false;
    private boolean replyFlag = false;
    private boolean myselfFlag = false;
    private boolean isNote = false;

    private SearchTerm searchTerm = null;

    /**
     * @return pattern 값 반환
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern
     *            파라미터를 pattern값에 설정
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return advanceMode 값 반환
     */
    public boolean isAdvanceMode() {
        return advanceMode;
    }

    /**
     * @param advanceMode
     *            파라미터를 advanceMode값에 설정
     */
    public void setAdvanceMode(boolean advanceMode) {
        this.advanceMode = advanceMode;
    }
    
    public boolean isNote() {
        return isNote;
    }
    
    public void setIsNote(boolean isNote) {
        this.isNote = isNote;
    }

    /**
     * @return operation 값 반환
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation
     *            파라미터를 operation값에 설정
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return fromEmailPattern 값 반환
     */
    public String getAdFromEmailPattern() {
        return adFromEmailPattern;
    }

    /**
     * @param fromEmailPattern
     *            파라미터를 fromEmailPattern값에 설정
     */
    public void setAdFromEmailPattern(String adFromEmailPattern) {
        this.adFromEmailPattern = adFromEmailPattern;
    }

    /**
     * @return toEmailPattern 값 반환
     */
    public String getAdToEmailPattern() {
        return adToEmailPattern;
    }

    /**
     * @param toEmailPattern
     *            파라미터를 toEmailPattern값에 설정
     */
    public void setAdToEmailPattern(String adToEmailPattern) {
        this.adToEmailPattern = adToEmailPattern;
    }

    /**
     * @return searchPattern 값 반환
     */
    public String getAdSearchPattern() {
        return adSearchPattern;
    }

    /**
     * @param searchPattern
     *            파라미터를 searchPattern값에 설정
     */
    public void setAdSearchPattern(String adSearchPattern) {
        this.adSearchPattern = adSearchPattern;
    }

    /**
     * @return searchCategory 값 반환
     */
    public String getAdSearchCategory() {
        return adSearchCategory;
    }

    /**
     * @param searchCategory
     *            파라미터를 searchCategory값에 설정
     */
    public void setAdSearchCategory(String adSearchCategory) {
        this.adSearchCategory = adSearchCategory;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return fromDate 값 반환
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate
     *            파라미터를 fromDate값에 설정
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return toDate 값 반환
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate
     *            파라미터를 toDate값에 설정
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * @return sortBy 값 반환
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * <p>
     * from, size, subj, to, 디폴트는 받은 날짜
     * </p>
     * 
     * @param sortBy
     *            파라미터를 sortBy값에 설정
     * 
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * @return sortDir 값 반환
     */
    public String getSortDir() {
        return sortDir;
    }

    /**
     * <p>
     * asce 오름차순, desc 내림차순
     * </p>
     * 
     * @param sortDir
     *            파라미터를 sortDir값에 설정
     */
    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    /**
     * @return page 값 반환
     */
    public String getPage() {
        return page;
    }

    /**
     * @return page 값에 설정
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @return pageBase 값 반환
     */
    public void setPageBase(String pageBase) {
        this.pageBase = pageBase;
    }

    public int getPageBaseSize() {
        return Integer.parseInt(pageBase);
    }

    public int getStartPos() {
        int pos = Integer.parseInt(page) - 1;
        pos = (pos * Integer.parseInt(pageBase));
        return pos;
    }

    /**
     * @return date 값 반환
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            파라미터를 date값에 설정
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * <p>
     * Sort 할 플래그 정보 설정
     * </p>
     * 
     * @param flagType
     * @return void
     */
    public void setSearchFlag(char flagType) {
        unseenFlag = false;
        seenFlag = false;
        flagedFlag = false;
        attachFlag = false;
        replyFlag = false;
        myselfFlag = false;

        switch (flagType) {
        case MessageHandler.UNSEEN_FLAG:
            unseenFlag = true;
            break;
        case MessageHandler.SEEN_FLAG:
            seenFlag = true;
            break;
        case MessageHandler.FLAGED_FLAG:
            flagedFlag = true;
            break;
        case MessageHandler.ATTACH_FLAG:
            attachFlag = true;
            break;
        case MessageHandler.REPLY_FLAG:
            replyFlag = true;
            break;
        case MessageHandler.MYSELF_FLAG:
            myselfFlag = true;
            break;
        default:
            break;
        }
    }

    public boolean isUnseenFlag() {
        return unseenFlag;
    }

    /**
     * @return searchTerm 값 반환
     */
    public SearchTerm getSearchTerm() {
        parseSortInfo();
        return searchTerm;
    }

    /**
     * <p>
     * 메세지 Sort시 Sort Key 반환
     * </p>
     * <p>
     * from, size, subj, to, 디폴트는 받은 날짜
     * </p>
     * 
     * @return int Sort Key 값
     */
    public int getMessageSortBy() {
        int msgSort = -1;
        if ("from".equals(sortBy)) {
            msgSort = TMailFolder.SORT_FROM;
        } else if ("size".equals(sortBy)) {
            msgSort = TMailFolder.SORT_SIZE;
        } else if ("subj".equals(sortBy)) {
            msgSort = TMailFolder.SORT_SUBJECT;
        } else if ("to".equals(sortBy)) {
            msgSort = TMailFolder.SORT_TO;
        } else if ("date".equals(sortBy)) {
            msgSort = TMailFolder.SORT_DATE;
        } else {
            msgSort = TMailFolder.SORT_ARRIVAL;
        }

        return msgSort;
    }

    /**
     * <p>
     * 메세지 Sort 방향 반환
     * </p>
     * <p>
     * asce 오름차순, desc 내림차순
     * </p>
     * 
     * @return int
     */
    public int getMessageSortDir() {
        int msgDir = -1;
        if ("asce".equals(sortDir)) {
            msgDir = TMailFolder.SORT_ASCENDING;
        } else {
            msgDir = TMailFolder.SORT_DESCENDING;
        }

        return msgDir;
    }

    /**
     * <p>
     * 해당 Sort정보를 가지고 SearchTerm 을 생성
     * </p>
     * 
     * @return void
     */
    public void parseSortInfo() {
        searchTerm = null;
        List<SearchTerm> searchList = new ArrayList<SearchTerm>();
        // 안읽은 메일
        if (unseenFlag) {
            searchList.add(new FlagTerm(new Flags("UNSEEN"), true));
        }

        // 읽은 메일
        if (seenFlag) {
            searchList.add(new FlagTerm(new Flags("SEEN"), true));
        }

        // 깃발 메일
        if (flagedFlag) {
            searchList.add(new FlagTerm(new Flags("FLAGGED"), true));
        }

        // 첨부 메일
        if (attachFlag) {
            searchList.add(new FlagTerm(new Flags("ATTACH"), true));
        }

        // 답장 메일
        if (replyFlag) {
            searchList.add(new FlagTerm(new Flags("ANSWERED"), true));
        }

        if (myselfFlag) {
            searchList.add(new MyselfSearchTerm(userEmail));
        }

        if (!Validation.isNull(pattern) && !advanceMode) {
            searchList.add(new SubjectTerm(pattern));
            searchList.add(new FromStringTerm(pattern));
            searchList.add(new RecipientStringTerm(Message.RecipientType.TO, pattern));
            operation = "or";
        } else if (advanceMode) {
            if (!Validation.isNull(adFromEmailPattern)) {
                searchList.add(new FromStringTerm(adFromEmailPattern));
            }

            if (!Validation.isNull(adToEmailPattern)) {
                if (isNote) {
                    searchList.add(new RecipientStringTerm(Message.RecipientType.TO,adToEmailPattern));
                } else {
                    searchList.add(new OrTerm(new RecipientStringTerm(Message.RecipientType.TO, adToEmailPattern),
                            new RecipientStringTerm(Message.RecipientType.CC, adToEmailPattern)));
                }
            }

            if (adSearchCategory != null && !Validation.isNull(adSearchPattern)) {
                // Subject Pattern
                if (adSearchCategory.equals("s")) {
                    searchList.add(new SubjectTerm(adSearchPattern));

                    // Body Pattern
                } else if (adSearchCategory.equals("b")) {
                    searchList.add(new BodyTerm(adSearchPattern));

                    // Attach File Name Pattern
                } else if (adSearchCategory.equals("af")) {
                    searchList.add(new AttachTerm(adSearchPattern, AttachTerm.FILE_NAME));

                    // Attach File Content Pattern
                } else if (adSearchCategory.equals("ab")) {
                    searchList.add(new AttachTerm(adSearchPattern, AttachTerm.FILE_CONTENT));

                    // Subject + Body Pattern
                } else if (adSearchCategory.equals("sb")) {
                    searchList.add(new OrTerm(new SubjectTerm(adSearchPattern), new BodyTerm(adSearchPattern)));

                    // Subject + Attach File Name Pattern
                } else if (adSearchCategory.equals("saf")) {
                    searchList.add(new OrTerm(new SubjectTerm(adSearchPattern), new AttachTerm(adSearchPattern,
                            AttachTerm.FILE_NAME)));

                    // Subject + Attach File Content Pattern
                } else if (adSearchCategory.equals("sab")) {
                    searchList.add(new OrTerm(new SubjectTerm(adSearchPattern), new AttachTerm(adSearchPattern,
                            AttachTerm.FILE_CONTENT)));
                }
            }

        }

        boolean isOr = false;
        if (operation.equalsIgnoreCase("or")) {
            isOr = true;
        }
        int size = searchList.size();
        
        if(size < 0){
			size = 0;
		}
        
        SearchTerm[] searchTerms = new SearchTerm[size];

        if (size > 0) {
            searchList.toArray(searchTerms);

            if (size == 1) {
                searchTerm = searchTerms[0];
            } else if (isOr) {
                SearchTerm searchOrTerm = searchTerms[0];
                for (int i = 1; i < size; i++) {
                    searchOrTerm = makeOrTerm(searchTerms[i], searchOrTerm);
                }
                searchTerm = searchOrTerm;
            } else {
                searchTerm = new AndTerm(searchTerms);
            }
        }

        SearchTerm dateTerm = null;

        if (!Validation.isNull(fromDate)) {
            Date d = FormatUtil.strToDate(toDate);

            if (d != null) {
                dateTerm = (dateTerm == null) ? new ReceivedDateTerm(ComparisonTerm.LE, d) : null;
            }
        }

        if (!Validation.isNull(toDate)) {
            Date d = FormatUtil.strToDate(fromDate);

            if (d != null) {
                dateTerm = (dateTerm != null) ? new AndTerm(new ReceivedDateTerm(ComparisonTerm.GE, d), dateTerm)
                        : new ReceivedDateTerm(ComparisonTerm.GE, d);
            }
        }

        if (dateTerm != null) {
            searchTerm = new AndTerm(searchTerm, dateTerm);
        }

    }

    /**
     * <p>
     * OrTerm 생성
     * </p>
     * 
     * @param addTerm
     *            추가 될 조건
     * @param mainTerm
     *            본 조건
     * @return OrTerm
     */
    private OrTerm makeOrTerm(SearchTerm addTerm, SearchTerm mainTerm) {
        return new OrTerm(new SearchTerm[] { addTerm, mainTerm });
    }

}
