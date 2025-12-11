package com.terracetech.tims.webmail.scheduler.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.dao.DataAccessException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terracetech.tims.common.I18nResources;
import com.terracetech.tims.mail.TMailAddress;
import com.terracetech.tims.webmail.common.ladmin.Protocol;
import com.terracetech.tims.webmail.common.log.LogManager;
import com.terracetech.tims.webmail.common.manager.LadminManager;
import com.terracetech.tims.webmail.mail.ibean.MailAddressBean;
import com.terracetech.tims.webmail.mail.ibean.MailSendResultBean;
import com.terracetech.tims.webmail.mail.ibean.MessageParserInfoBean;
import com.terracetech.tims.webmail.mail.ibean.SenderInfoBean;
import com.terracetech.tims.webmail.mail.manager.send.BatchSendHandler;
import com.terracetech.tims.webmail.mail.manager.send.SendHandler;
import com.terracetech.tims.webmail.mailuser.User;
import com.terracetech.tims.webmail.mailuser.dao.MailUserDao;
import com.terracetech.tims.webmail.mailuser.manager.SearchEmailManager;
import com.terracetech.tims.webmail.mailuser.vo.MailUserInfoVO;
import com.terracetech.tims.webmail.mailuser.vo.SearchUserVO;
import com.terracetech.tims.webmail.mobile.manager.MobileSyncManager;
import com.terracetech.tims.webmail.organization.manager.OrganizationManager;
import com.terracetech.tims.webmail.organization.vo.DeptVO;
import com.terracetech.tims.webmail.scheduler.dao.SchedulerDao;
import com.terracetech.tims.webmail.scheduler.ibean.SchedulerAssetBean;
import com.terracetech.tims.webmail.scheduler.ibean.SchedulerBean;
import com.terracetech.tims.webmail.scheduler.ibean.SchedulerDateBean;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryImageVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetCategoryVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetPlanVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerAssetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerDataVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerHolidayVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareExtVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareTargetVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerShareVO;
import com.terracetech.tims.webmail.scheduler.vo.SchedulerVO;
import com.terracetech.tims.webmail.util.FileUtil;
import com.terracetech.tims.webmail.util.FormatUtil;
import com.terracetech.tims.webmail.util.StringUtils;

@Service
@Transactional
public class SchedulerManager {

    private SchedulerDao schedulerDao;
    private MailUserDao mailUserDao;
    private SearchEmailManager searchEmailManager = null;
    private LadminManager ladminManager = null;
    private OrganizationManager organizationManager = null;
    private MobileSyncManager mobileSyncManager;

    public void setSchedulerDao(SchedulerDao schedulerDao) {
        this.schedulerDao = schedulerDao;
    }

    public void setMailUserDao(MailUserDao mailUserDao) {
        this.mailUserDao = mailUserDao;
    }

    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    public void setMobileSyncManager(MobileSyncManager mobileSyncManager) {
        this.mobileSyncManager = mobileSyncManager;
    }

    public void setSearchEmailManager(SearchEmailManager searchEmailManager) {
        this.searchEmailManager = searchEmailManager;
    }

    public void setLadminManager(LadminManager ladminManager) {
        this.ladminManager = ladminManager;
    }

    public SchedulerVO getMonthScheduler(int year, int month) {
        SchedulerHandler thisHandler = null;
        SchedulerVO schedulerVo = new SchedulerVO();

        if (year == 0 || month == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month);
        }
        schedulerVo.setThisdayStr(thisHandler.getthisYyyymmdd());
        int thisYear = thisHandler.getthisYear();
        int thisMonth = thisHandler.getthisMonth();
        int prevYear = 0;
        int prevMonth = 0;
        int nextYear = 0;
        int nextMonth = 0;
        if (thisMonth <= 1) {
            prevYear = thisYear - 1;
            prevMonth = 12;
        } else {
            prevYear = thisYear;
            prevMonth = thisMonth - 1;
        }

        if (thisMonth >= 12) {
            nextYear = thisYear + 1;
            nextMonth = 1;
        } else {
            nextYear = thisYear;
            nextMonth = thisMonth + 1;
        }

        schedulerVo.setThisYear(thisYear);
        schedulerVo.setThisMonth(thisMonth);
        schedulerVo.setThisDay(thisHandler.getthisDay());
        schedulerVo.setPrevYear(prevYear);
        schedulerVo.setPrevMonth(prevMonth);
        schedulerVo.setNextYear(nextYear);
        schedulerVo.setNextMonth(nextMonth);
        schedulerVo.setFirstdayOfMonth(thisHandler.getfirstDateOfMonth());
        schedulerVo.setMaxdayOfMonth(thisHandler.getmaxDayOfMonth());
        schedulerVo.setMonthDayList(thisHandler.getMonthDayList(thisYear, thisMonth));

        return schedulerVo;
    }

    public SchedulerVO getWeekScheduler(int year, int month, int day) {
        SchedulerHandler thisHandler = null;
        SchedulerVO schedulerVo = new SchedulerVO();

        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }
        schedulerVo.setThisdayStr(thisHandler.getthisYyyymmdd());

        schedulerVo.setThisYear(thisHandler.getthisYear());
        schedulerVo.setThisMonth(thisHandler.getthisMonth());
        schedulerVo.setThisDay(thisHandler.getthisDay());

        int startDateOfWeek = thisHandler.getstartDateOfWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int endDateOfWeek = thisHandler.getendDateOfWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int prevDateOfWeek = thisHandler.getPrevWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int nextDateOfWeek = thisHandler.getNextWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());

        int firstYear = startDateOfWeek / 10000;
        int firstMonth = (startDateOfWeek / 100) - (firstYear * 100);
        int firstDay = startDateOfWeek % 100;
        schedulerVo.setFirstYear(firstYear);
        schedulerVo.setFirstMonth(firstMonth);
        schedulerVo.setFirstDay(firstDay);
        schedulerVo.setWeekDayList(thisHandler.getWeekDayList(firstYear, firstMonth, firstDay));

        int lastYear = endDateOfWeek / 10000;
        int lastMonth = (endDateOfWeek / 100) - (lastYear * 100);
        int lastDay = endDateOfWeek % 100;
        schedulerVo.setLastYear(lastYear);
        schedulerVo.setLastMonth(lastMonth);
        schedulerVo.setLastDay(lastDay);

        int prevYear = prevDateOfWeek / 10000;
        int prevMonth = (prevDateOfWeek / 100) - (prevYear * 100);
        int prevDay = prevDateOfWeek % 100;
        schedulerVo.setPrevYear(prevYear);
        schedulerVo.setPrevMonth(prevMonth);
        schedulerVo.setPrevDay(prevDay);

        int nextYear = nextDateOfWeek / 10000;
        int nextMonth = (nextDateOfWeek / 100) - (nextYear * 100);
        int nextDay = nextDateOfWeek % 100;
        schedulerVo.setNextYear(nextYear);
        schedulerVo.setNextMonth(nextMonth);
        schedulerVo.setNextDay(nextDay);

        return schedulerVo;
    }

    public SchedulerVO getDayScheduler(int year, int month, int day) {
        SchedulerHandler thisHandler = null;
        SchedulerVO schedulerVo = new SchedulerVO();

        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }

        schedulerVo.setThisdayStr(thisHandler.getthisYyyymmdd());
        schedulerVo.setThisDayOfWeek(thisHandler.getthisDayOfWeek());

        schedulerVo.setThisYear(thisHandler.getthisYear());
        schedulerVo.setThisMonth(thisHandler.getthisMonth());
        schedulerVo.setThisDay(thisHandler.getthisDay());

        int prevDate = thisHandler.getPrevDay(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int prevYear = prevDate / 10000;
        int prevMonth = (prevDate / 100) - (prevYear * 100);
        int prevDay = prevDate % 100;
        schedulerVo.setPrevYear(prevYear);
        schedulerVo.setPrevMonth(prevMonth);
        schedulerVo.setPrevDay(prevDay);

        int nextDate = thisHandler.getNextDay(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int nextYear = nextDate / 10000;
        int nextMonth = (nextDate / 100) - (nextYear * 100);
        int nextDay = nextDate % 100;
        schedulerVo.setNextYear(nextYear);
        schedulerVo.setNextMonth(nextMonth);
        schedulerVo.setNextDay(nextDay);

        return schedulerVo;
    }

    public SchedulerVO getTodayDate(SchedulerVO schedulerVo) {
        SchedulerHandler todayHandler = new SchedulerHandler();
        schedulerVo.setTodayYear(todayHandler.getthisYear());
        schedulerVo.setTodayMonth(todayHandler.getthisMonth());
        schedulerVo.setTodayDay(todayHandler.getthisDay());
        schedulerVo.setTodayStr(todayHandler.getthisYyyymmdd());
        return schedulerVo;
    }

    public int getLunar(int year, int month, int day) {
        SchedulerHandler todayHandler = new SchedulerHandler();
        return todayHandler.getLunar(year, month, day);
    }

    public List<SchedulerDataVO> getMonthScheduleList(int year, int month, int mailDomainSeq, int mailUserSeq,
            String email) {
        SchedulerHandler thisHandler = null;
        SchedulerDataVO schedulerDataVo = null;
        List<SchedulerDataVO> list = new ArrayList<SchedulerDataVO>();
        if (year == 0 || month == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month);
        }

        List dateList = thisHandler.getMonthDayList(year, month);

        String firstDayStr = (String) dateList.get(0);
        String lastDayStr = (String) dateList.get(dateList.size() - 1);

        int firstDay = Integer.parseInt(firstDayStr);
        int lastDay = Integer.parseInt(lastDayStr);
        boolean flag = true;
        firstDayStr = firstDayStr + "0000";
        lastDayStr = lastDayStr + "2359";

        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        int[] schedulerIds = getSchedulerIds(shareScheduleList);

        List<SchedulerDataVO> scheduleList = schedulerDao.readScheduleList(mailUserSeq, schedulerIds, firstDayStr,
                lastDayStr);
        int assetCount = 0;
        for (int i = 0; i < scheduleList.size(); i++) {
            flag = true;
            schedulerDataVo = scheduleList.get(i);

            if (schedulerIds != null) {
                for (int j = 0; j < shareScheduleList.size(); j++) {
                    if (schedulerDataVo.getSchedulerId() == shareScheduleList.get(j).getSchedulerId()) {
                        schedulerDataVo.setShareColor(shareScheduleList.get(j).getShareColor());
                        shareScheduleList.remove(j);
                        break;
                    }
                }
            }

            assetCount = schedulerDao.checkAssetSchedulerCount(schedulerDataVo.getSchedulerId());
            schedulerDataVo.setCheckAsset((assetCount > 0) ? "on" : "off");

            int startDate = Integer.parseInt(schedulerDataVo.getStartDate().substring(0, 8));
            int endDate = Integer.parseInt(schedulerDataVo.getEndDate().substring(0, 8));

            String startTime = schedulerDataVo.getStartDate().substring(8);
            String endTime = schedulerDataVo.getEndDate().substring(8);

            if ((startDate < firstDay && endDate < firstDay) || (startDate > lastDay && endDate > lastDay)
                    || "on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                flag = false;
            } else {
                if (startDate < firstDay) {
                    startDate = firstDay;
                }
                if (endDate > lastDay) {
                    endDate = lastDay;
                }
            }

            schedulerDataVo.setDrowStartDate(startDate);
            schedulerDataVo.setDrowEndDate(endDate);
            schedulerDataVo.setDrowStartTime(Integer.parseInt(startTime));
            schedulerDataVo.setDrowEndTime(Integer.parseInt(endTime));

            int planSize = thisHandler.getTermDay(Integer.toString(startDate), Integer.toString(endDate));
            schedulerDataVo.setPlanSize(planSize + 1);

            if (flag) {
                list.add(schedulerDataVo);
            }

            if ("on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                list = checkRepeatSchedule(thisHandler, list, schedulerDataVo, firstDay, lastDay);
            }
        }

        list = checkMonthDrow(list, dateList);

        return sortRegDate(list);
    }

    public List<SchedulerDataVO> getWeekScheduleList(int year, int month, int day, int mailDomainSeq, int mailUserSeq,
            String email) {
        List<SchedulerDataVO> list = new ArrayList<SchedulerDataVO>();
        SchedulerHandler thisHandler = null;
        SchedulerDataVO schedulerDataVo = null;

        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }
        int startDateOfWeek = thisHandler.getstartDateOfWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int startYear = startDateOfWeek / 10000;
        int startMonth = (startDateOfWeek / 100) - (startYear * 100);
        int startDay = startDateOfWeek % 100;

        List dateList = thisHandler.getWeekDayList(startYear, startMonth, startDay);

        String firstDayStr = (String) dateList.get(0);
        String lastDayStr = (String) dateList.get(dateList.size() - 1);

        int firstDay = Integer.parseInt(firstDayStr);
        int lastDay = Integer.parseInt(lastDayStr);
        boolean flag = true;
        firstDayStr = firstDayStr + "0000";
        lastDayStr = lastDayStr + "2359";

        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        int[] schedulerIds = getSchedulerIds(shareScheduleList);

        List<SchedulerDataVO> scheduleList = schedulerDao.readScheduleList(mailUserSeq, schedulerIds, firstDayStr,
                lastDayStr);
        int assetCount = 0;
        for (int i = 0; i < scheduleList.size(); i++) {
            flag = true;
            schedulerDataVo = scheduleList.get(i);

            if (schedulerIds != null) {
                for (int j = 0; j < shareScheduleList.size(); j++) {
                    if (schedulerDataVo.getSchedulerId() == shareScheduleList.get(j).getSchedulerId()) {
                        schedulerDataVo.setShareColor(shareScheduleList.get(j).getShareColor());
                        shareScheduleList.remove(j);
                        break;
                    }
                }
            }

            assetCount = schedulerDao.checkAssetSchedulerCount(schedulerDataVo.getSchedulerId());
            schedulerDataVo.setCheckAsset((assetCount > 0) ? "on" : "off");

            int startDate = Integer.parseInt(schedulerDataVo.getStartDate().substring(0, 8));
            int endDate = Integer.parseInt(schedulerDataVo.getEndDate().substring(0, 8));

            String startTime = schedulerDataVo.getStartDate().substring(8);
            String endTime = schedulerDataVo.getEndDate().substring(8);

            if (startDate == endDate) {
                schedulerDataVo.setTimeSize(thisHandler.getTodayTermTime(startTime, endTime));
            }

            if ((startDate < firstDay && endDate < firstDay) || (startDate > lastDay && endDate > lastDay)
                    || "on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                flag = false;
            } else {
                if (startDate < firstDay) {
                    startDate = firstDay;
                }
                if (endDate > lastDay) {
                    endDate = lastDay;
                }
            }

            schedulerDataVo.setDrowStartDate(startDate);
            schedulerDataVo.setDrowEndDate(endDate);
            schedulerDataVo.setDrowStartTime(Integer.parseInt(startTime));
            schedulerDataVo.setDrowEndTime(Integer.parseInt(endTime));

            int planSize = thisHandler.getTermDay(Integer.toString(startDate), Integer.toString(endDate));

            schedulerDataVo.setPlanSize(planSize + 1);

            if (flag) {
                list.add(schedulerDataVo);
            }

            if ("on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                list = checkRepeatSchedule(thisHandler, list, schedulerDataVo, firstDay, lastDay);
            }
        }

        list = checkMonthDrow(list, dateList);

        return sortRegDate(list);
    }

    public List<SchedulerDataVO> getDayScheduleList(int year, int month, int day, int mailDomainSeq, int mailUserSeq,
            String email) {
        List<SchedulerDataVO> list = new ArrayList<SchedulerDataVO>();
        SchedulerHandler thisHandler = null;
        SchedulerDataVO schedulerDataVo = null;

        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }

        String thisDayStr = thisHandler.getthisYyyymmdd();
        int thisDay = Integer.parseInt(thisDayStr);
        String firstDayStr = thisDayStr + "0000";
        String lastDayStr = thisDayStr + "2359";
        boolean flag = true;

        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        int[] schedulerIds = getSchedulerIds(shareScheduleList);

        List<SchedulerDataVO> scheduleList = schedulerDao.readScheduleList(mailUserSeq, schedulerIds, firstDayStr,
                lastDayStr);
        int assetCount = 0;
        for (int i = 0; i < scheduleList.size(); i++) {
            flag = true;
            schedulerDataVo = scheduleList.get(i);

            if (schedulerIds != null) {
                for (int j = 0; j < shareScheduleList.size(); j++) {
                    if (schedulerDataVo.getSchedulerId() == shareScheduleList.get(j).getSchedulerId()) {
                        schedulerDataVo.setShareColor(shareScheduleList.get(j).getShareColor());
                        shareScheduleList.remove(j);
                        break;
                    }
                }
            }

            assetCount = schedulerDao.checkAssetSchedulerCount(schedulerDataVo.getSchedulerId());
            schedulerDataVo.setCheckAsset((assetCount > 0) ? "on" : "off");

            int startDate = Integer.parseInt(schedulerDataVo.getStartDate().substring(0, 8));
            int endDate = Integer.parseInt(schedulerDataVo.getEndDate().substring(0, 8));

            String startTime = schedulerDataVo.getStartDate().substring(8);
            String endTime = schedulerDataVo.getEndDate().substring(8);

            if (startDate == endDate) {
                schedulerDataVo.setTimeSize(thisHandler.getTodayTermTime(startTime, endTime));
            }

            if ((startDate < thisDay && endDate < thisDay) || (startDate > thisDay && endDate > thisDay)
                    || "on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                flag = false;
            } else {
                if (startDate < thisDay) {
                    startDate = thisDay;
                }
                if (endDate > thisDay) {
                    endDate = thisDay;
                }
            }

            int planSize = thisHandler.getTermDay(Integer.toString(startDate), Integer.toString(endDate));
            schedulerDataVo.setPlanSize(planSize + 1);

            schedulerDataVo.setDrowStartDate(startDate);
            schedulerDataVo.setDrowEndDate(endDate);
            schedulerDataVo.setDrowStartTime(Integer.parseInt(startTime));
            schedulerDataVo.setDrowEndTime(Integer.parseInt(endTime));

            if (flag) {
                list.add(schedulerDataVo);
            }

            if ("on".equalsIgnoreCase(schedulerDataVo.getRepeatFlag())) {
                list = checkRepeatSchedule(thisHandler, list, schedulerDataVo, thisDay, thisDay);
            }
        }

        return sortRegDate(list);
    }

    private List<SchedulerDataVO> sortRegDate(List<SchedulerDataVO> list) {

        Comparator<SchedulerDataVO> comparetor = new Comparator<SchedulerDataVO>() {
            public int compare(SchedulerDataVO sdata1, SchedulerDataVO sdata2) {
                return (((sdata1.getDrowStartDate() > sdata2.getDrowStartDate())
                        || ((sdata1.getDrowStartDate() == sdata2.getDrowStartDate()) && (sdata1.getPlanSize() > sdata2
                                .getPlanSize()))
                        || ((sdata1.getDrowStartDate() == sdata2.getDrowStartDate())
                                && (sdata1.getPlanSize() == sdata2.getPlanSize()) && (sdata1.getDrowStartTime() > sdata2
                                .getDrowStartTime())) || ((sdata1.getDrowStartDate() == sdata2.getDrowStartDate())
                        && (sdata1.getPlanSize() == sdata2.getPlanSize())
                        && (sdata1.getDrowStartTime() == sdata2.getDrowStartTime()) && ("on".equals(sdata2.getAllDay())))) == true ? 1
                        : 0);
            }
        };
        Collections.sort(list, comparetor);

        return list;
    }

    private List<SchedulerDataVO> checkMonthDrow(List<SchedulerDataVO> list, List dateList) {
        int count = 0;
        int thisIndex = 0;
        int first = 7;
        int weekTerm = 0;
        int planSize = 0;
        int maxCount = 0;
        boolean overCheck = false;
        String drowStartDateStr = null;
        List<String> drowList = null;

        for (int i = 0; i < list.size(); i++) {
            drowStartDateStr = Integer.toString(list.get(i).getDrowStartDate());
            planSize = list.get(i).getPlanSize();
            drowList = new ArrayList<String>();
            count = 0;
            weekTerm = 0;
            thisIndex = 0;
            first = 7;
            overCheck = false;

            for (int j = 0; j < dateList.size(); j++) {
                if (drowStartDateStr.equals(dateList.get(j))) {
                    count = j;
                    break;
                }
            }

            thisIndex = (count % 7);
            if (thisIndex > 0) {
                first = 7 - thisIndex;
            }

            if ((thisIndex + planSize) >= 7) {
                drowList.add(first + "|" + dateList.get(count));
                if (planSize + count >= dateList.size()) {
                    list.get(i).setPlanSize(first);
                }
                planSize = planSize - first;
                overCheck = true;
            }

            while (planSize >= 7) {
                planSize = planSize - 7;
                maxCount = count + first + weekTerm;
                if (dateList.size() > maxCount) {
                    drowList.add(7 + "|" + dateList.get(maxCount));
                    weekTerm += 7;
                }
            }

            if (planSize > 0) {
                if (overCheck) {
                    if (weekTerm > 0 && dateList.size() > maxCount + 7) {
                        drowList.add(planSize + "|" + dateList.get(maxCount + 7));
                    } else if (weekTerm <= 0) {
                        maxCount = count + first;
                        if (dateList.size() > maxCount) {
                            drowList.add(planSize + "|" + dateList.get(maxCount));
                        }
                    }
                } else {
                    drowList.add(planSize + "|" + dateList.get(count));
                }
            }
            list.get(i).setDrowList(drowList);
        }

        return list;
    }

    private List<SchedulerDataVO> checkRepeatSchedule(SchedulerHandler thisHandler, List<SchedulerDataVO> list,
            SchedulerDataVO schedulerDataVo, int firstDay, int lastDay) {
        List<String> startList = null;
        List<String> endList = null;
        SchedulerDataVO newDataVo = null;
        Map<String, String> preMap = null;
        boolean repeatCheck = false;
        int realPlanSize = 0;
        int planSize = schedulerDataVo.getPlanSize();
        String repeatSdate = null;
        String repeatEdate = null;
        String drowRepeatSdate = null;

        String startDate = schedulerDataVo.getStartDate();
        String startTime = schedulerDataVo.getStartDate().substring(8);
        String endTime = schedulerDataVo.getEndDate().substring(8);
        String repeatTerm = schedulerDataVo.getRepeatTerm();
        String repeatEndDate = schedulerDataVo.getRepeatEndDate();

        startList = thisHandler.getRepeatDays(firstDay, lastDay, startDate, repeatTerm, repeatEndDate);

        List<SchedulerDataVO> repeatIgnoreList = schedulerDao.readRepeatIgnoreList(schedulerDataVo.getSchedulerId());
        Map<String, String> repeatIgnoreMap = new HashMap<String, String>();
        if (repeatIgnoreList != null && repeatIgnoreList.size() > 0) {
            String repeatIgnoreTime = null;
            for (int i = 0; i < repeatIgnoreList.size(); i++) {
                repeatIgnoreTime = repeatIgnoreList.get(i).getIgnoreTime().substring(0, 8);
                repeatIgnoreMap.put(repeatIgnoreTime, repeatIgnoreTime);
            }
        }

        if (startList != null && startList.size() > 0) {
            String startFirst = startList.get(0);
            int startFirstInt = Integer.parseInt(startFirst);
            if (firstDay > startFirstInt || lastDay < startFirstInt) {
                preMap = thisHandler.getRepeatPreDay(Integer.toString(firstDay), startFirst, repeatEndDate, planSize);
                if (!preMap.isEmpty() && !repeatIgnoreMap.containsKey(preMap.get("drowStartDate"))) {
                    startList.set(0, preMap.get("drowStartDate"));
                    repeatCheck = true;
                } else {
                    startList.remove(0);
                }
            }

            endList = thisHandler.getRepeatEndDays(startList, planSize);

            for (int i = 0; i < startList.size(); i++) {

                repeatSdate = startList.get(i);
                repeatEdate = endList.get(i);
                realPlanSize = planSize;
                drowRepeatSdate = repeatSdate;

                if (repeatIgnoreMap.containsKey(repeatSdate)) {
                    continue;
                }

                if (i == 0 && repeatCheck) {
                    repeatSdate = preMap.get("startDate");
                    realPlanSize = Integer.parseInt(preMap.get("planSize"));
                    repeatEdate = preMap.get("endDate");
                }

                newDataVo = new SchedulerDataVO();

                int endDay = Integer.parseInt(repeatEdate);
                if (lastDay < endDay) {
                    endDay = lastDay;
                }
                newDataVo.setSchedulerId(schedulerDataVo.getSchedulerId());
                newDataVo.setStartDate(repeatSdate + startTime);
                newDataVo.setEndDate(repeatEdate + endTime);
                newDataVo.setDrowStartDate(Integer.parseInt(drowRepeatSdate));
                newDataVo.setDrowStartTime(Integer.parseInt(startTime));
                newDataVo.setDrowEndDate(endDay);
                newDataVo.setDrowEndTime(Integer.parseInt(endTime));
                newDataVo.setMailUserSeq(schedulerDataVo.getMailUserSeq());
                newDataVo.setTitle(schedulerDataVo.getTitle());
                newDataVo.setLocation(schedulerDataVo.getLocation());
                newDataVo.setContent(schedulerDataVo.getContent());
                newDataVo.setAllDay(schedulerDataVo.getAllDay());
                newDataVo.setHoliday(schedulerDataVo.getHoliday());
                newDataVo.setLunar(schedulerDataVo.getLunar());
                newDataVo.setRepeatFlag(schedulerDataVo.getRepeatFlag());
                newDataVo.setRepeatTerm(schedulerDataVo.getRepeatTerm());
                newDataVo.setRepeatEndDate(schedulerDataVo.getRepeatEndDate());
                newDataVo.setCreateTime(schedulerDataVo.getCreateTime());
                newDataVo.setPlanSize(realPlanSize);
                newDataVo.setShareColor(schedulerDataVo.getShareColor());

                list.add(newDataVo);
            }
        }

        return list;
    }

    private int[] getSchedulerIds(List<SchedulerDataVO> shareScheduleList) {
        int[] schedulerIds = { 0 };
        if (shareScheduleList != null && shareScheduleList.size() > 0) {
            schedulerIds = new int[shareScheduleList.size()];
            for (int i = 0; i < shareScheduleList.size(); i++) {
                schedulerIds[i] = shareScheduleList.get(i).getSchedulerId();
            }
        }
        return schedulerIds;
    }

    public List getProgressDateList(int year, int month, int day) {
        SchedulerHandler thisHandler = null;
        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }
        int startDateOfWeek = thisHandler.getstartDateOfWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int firstYear = startDateOfWeek / 10000;
        int firstMonth = (startDateOfWeek / 100) - (firstYear * 100);
        int firstDay = startDateOfWeek % 100;

        return thisHandler.getWeekDayList(firstYear, firstMonth, firstDay);
    }

    public JSONArray getJsonMonthScheduleList(int year, int month, int mailDomainSeq, int mailUserSeq, String email) {
        JSONArray beanArray = new JSONArray();
        List<SchedulerDataVO> schedulerDataList = getMonthScheduleList(year, month, mailDomainSeq, mailUserSeq, email);
        if (schedulerDataList != null && schedulerDataList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < schedulerDataList.size(); i++) {
                beanObject = new SchedulerBean(schedulerDataList.get(i)).toJson();
                beanArray.add(beanObject);
            }
        }

        return beanArray;
    }

    public JSONArray getJsonWeekScheduleList(int year, int month, int day, int mailDomainSeq, int mailUserSeq,
            String email) {
        JSONArray beanArray = new JSONArray();
        List<SchedulerDataVO> schedulerDataList = getWeekScheduleList(year, month, day, mailDomainSeq, mailUserSeq,
                email);
        if (schedulerDataList != null && schedulerDataList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < schedulerDataList.size(); i++) {
                beanObject = new SchedulerBean(schedulerDataList.get(i)).toJson();
                beanArray.add(beanObject);
            }
        }

        return beanArray;
    }

    public JSONArray getJsonDayScheduleList(int year, int month, int day, int mailDomainSeq, int mailUserSeq,
            String email) {
        JSONArray beanArray = new JSONArray();
        List<SchedulerDataVO> schedulerDataList = getDayScheduleList(year, month, day, mailDomainSeq, mailUserSeq,
                email);
        if (schedulerDataList != null && schedulerDataList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < schedulerDataList.size(); i++) {
                beanObject = new SchedulerBean(schedulerDataList.get(i)).toJson();
                beanArray.add(beanObject);
            }
        }

        return beanArray;
    }

    @Transactional
    public Map<String, Object> saveSchedule(SchedulerDataVO schedulerDataVo) {
        Map<String, Object> result = new HashMap<String, Object>();
        String isSuccess = "success";
        try {
            schedulerDataVo.setOutlookSync("unsync");
            schedulerDataVo.setCreateTime(FormatUtil.getBasicDateStr());
            schedulerDataVo.setModifyTime(FormatUtil.getBasicDateStr());
            schedulerDao.saveSchedule(schedulerDataVo);
            if ("on".equals(schedulerDataVo.getCheckShare())) {
                String shareSeqStr = schedulerDataVo.getShareValue();
                if (StringUtils.isNotEmpty(shareSeqStr)) {
                    schedulerDao.saveShareSchedule(schedulerDataVo.getSchedulerId(), Integer.parseInt(shareSeqStr));
                }
                if ("on".equals(schedulerDataVo.getCheckSelfTarget())) {
                    String[] selfTargetList = schedulerDataVo.getSelfTargetList();
                    if (selfTargetList != null && selfTargetList.length > 0) {
                        for (String email : selfTargetList) {
                            schedulerDao.saveSchedulerShareExt(schedulerDataVo.getSchedulerId(),
                                    schedulerDataVo.getMailUserSeq(), email);
                        }
                    }
                }
            }

            if ("on".equals(schedulerDataVo.getCheckAsset())) {
                String assetSeqStr = schedulerDataVo.getAssetReserveValue();
                if (StringUtils.isNotEmpty(assetSeqStr)) {
                    String[] assetArray = assetSeqStr.split("_");
                    if (assetArray != null && assetArray.length > 0) {
                        SchedulerAssetPlanVO schedulerAssetPlanVo = null;
                        for (int i = 0; i < assetArray.length; i++) {
                            schedulerAssetPlanVo = new SchedulerAssetPlanVO();
                            schedulerAssetPlanVo.setSchedulerId(schedulerDataVo.getSchedulerId());
                            schedulerAssetPlanVo.setAssetSeq(Integer.parseInt(assetArray[i]));
                            schedulerAssetPlanVo.setMailUserSeq(schedulerDataVo.getMailUserSeq());
                            schedulerAssetPlanVo.setStartDate(schedulerDataVo.getStartDate());
                            schedulerAssetPlanVo.setEndDate(schedulerDataVo.getEndDate());
                            schedulerAssetPlanVo.setContect(schedulerDataVo.getContect());
                            schedulerAssetPlanVo.setCreateTime(schedulerDataVo.getCreateTime());
                            schedulerDao.saveAssetSchedule(schedulerAssetPlanVo);
                        }
                    }
                }
            }
            result.put("schedulerId", schedulerDataVo.getSchedulerId());

            mobileSyncManager.saveCalendarMobileSyncLog(schedulerDataVo.getMailUserSeq(),
                    schedulerDataVo.getSchedulerId(), "insert");
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            isSuccess = "fail";
        }

        result.put("isSuccess", isSuccess);
        return result;
    }

    @Transactional
    public Map<String, Object> modifySchedule(SchedulerDataVO schedulerDataVo) {
        Map<String, Object> result = new HashMap<String, Object>();
        String isSuccess = "success";
        try {
            schedulerDataVo.setModifyTime(FormatUtil.getBasicDateStr());
            schedulerDataVo.setOutlookSync("unsync");
            schedulerDao.modifySchedule(schedulerDataVo);
            int scheduleId = schedulerDataVo.getSchedulerId();
            deleteRepeatIgnore(scheduleId);

            if ("on".equals(schedulerDataVo.getCheckShare())) {
                String shareSeqStr = schedulerDataVo.getShareValue();
                if (StringUtils.isNotEmpty(shareSeqStr)) {
                    SchedulerShareVO shareScheduleVo = schedulerDao.readShareSchedule(scheduleId);
                    if (shareScheduleVo == null) {
                        schedulerDao.saveShareSchedule(scheduleId, Integer.parseInt(shareSeqStr));
                    } else {
                        schedulerDao.modifyShareSchedule(scheduleId, Integer.parseInt(shareSeqStr));
                    }
                } else {
                    schedulerDao.deleteShareSchedule(scheduleId);
                }

                if ("on".equals(schedulerDataVo.getCheckSelfTarget())) {
                    String[] selfTargetList = schedulerDataVo.getSelfTargetList();
                    if (selfTargetList != null && selfTargetList.length > 0) {
                        schedulerDao.deleteSchedulerShareExt(scheduleId);
                        for (String email : selfTargetList) {
                            schedulerDao.saveSchedulerShareExt(schedulerDataVo.getSchedulerId(),
                                    schedulerDataVo.getMailUserSeq(), email);
                        }
                    }
                } else {
                    schedulerDao.deleteSchedulerShareExt(scheduleId);
                }
            } else {
                schedulerDao.deleteShareSchedule(scheduleId);
                schedulerDao.deleteSchedulerShareExt(scheduleId);
            }

            deleteAssetScheduleBySchedulerId(scheduleId);
            if ("on".equals(schedulerDataVo.getCheckAsset())) {
                String assetSeqStr = schedulerDataVo.getAssetReserveValue();
                if (StringUtils.isNotEmpty(assetSeqStr)) {
                    String[] assetArray = assetSeqStr.split("_");
                    if (assetArray != null && assetArray.length > 0) {
                        SchedulerAssetPlanVO schedulerAssetPlanVo = null;
                        for (int i = 0; i < assetArray.length; i++) {
                            if (schedulerDao.checkAssetScheduleDuplicateCount(Integer.parseInt(assetArray[i]), -1,
                                    schedulerDataVo.getStartDate(), schedulerDataVo.getEndDate()) > 0) {
                                isSuccess = "reserved";
                                break;
                            }

                            schedulerAssetPlanVo = new SchedulerAssetPlanVO();
                            schedulerAssetPlanVo.setSchedulerId(schedulerDataVo.getSchedulerId());
                            schedulerAssetPlanVo.setAssetSeq(Integer.parseInt(assetArray[i]));
                            schedulerAssetPlanVo.setMailUserSeq(schedulerDataVo.getMailUserSeq());
                            schedulerAssetPlanVo.setStartDate(schedulerDataVo.getStartDate());
                            schedulerAssetPlanVo.setEndDate(schedulerDataVo.getEndDate());
                            schedulerAssetPlanVo.setContect(schedulerDataVo.getContect());
                            schedulerAssetPlanVo.setCreateTime(schedulerDataVo.getModifyTime());
                            schedulerDao.saveAssetSchedule(schedulerAssetPlanVo);
                        }
                    }
                }
            }
            result.put("schedulerId", schedulerDataVo.getSchedulerId());
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            isSuccess = "fail";
        }
        result.put("isSuccess", isSuccess);
        return result;
    }

    public String deleteSchedule(int schedulerId, String sendMail) {
        String result = "success";
        try {
            // schedulerDao.deleteSchedule(schedulerId);
            int[] schedulerIds = { schedulerId };
            String modifyTime = FormatUtil.getBasicDateStr();
            schedulerDao.schedulerOutlookMark(schedulerIds, modifyTime, "delete");
            if (!"on".equalsIgnoreCase(sendMail)) {
                deleteAssetScheduleBySchedulerId(schedulerId);
            }
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public String deleteScheduleComplete(int schedulerId) {
        String result = "success";
        try {
            schedulerDao.deleteSchedule(schedulerId);
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public String deleteRepeatSchedule(int schedulerId, String repeatStartDate) {
        String result = "success";
        try {
            saveRepeatIgnore(schedulerId, repeatStartDate);
            int[] schedulerIds = { schedulerId };
            String modifyTime = FormatUtil.getBasicDateStr();
            schedulerDao.schedulerOutlookMark(schedulerIds, modifyTime, "unsync");
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public SchedulerDataVO getSchedule(int mailDomainSeq, int schedulerId, String orgLocale, String domainMessage) {
        SchedulerDataVO schedulerDataVo = schedulerDao.readSchedule(schedulerId);
        SchedulerShareVO shareScheduleVo = schedulerDao.readShareSchedule(schedulerId);
        List<SchedulerShareExtVO> shareExtList = schedulerDao.readSchedulerShareExtList(schedulerId);
        List<SchedulerAssetPlanVO> scheduleAssetPlanList = schedulerDao.readAssetPlanInfoBySchedulerId(schedulerId);
        JSONArray assetPlanInfoList = makeAssetPlanInfoList(scheduleAssetPlanList);
        schedulerDataVo.setCheckAsset("off");
        if (scheduleAssetPlanList != null && scheduleAssetPlanList.size() > 0) {
            schedulerDataVo.setCheckAsset("on");
        }
        schedulerDataVo.setAssetPlanInfoList(assetPlanInfoList);

        schedulerDataVo.setCheckShare("off");
        if (shareScheduleVo != null) {
            schedulerDataVo.setCheckShare("on");
            schedulerDataVo.setShareValue(Integer.toString(shareScheduleVo.getShareSeq()));
            schedulerDataVo.setShareName(shareScheduleVo.getShareName());
            schedulerDataVo.setUserName(shareScheduleVo.getUserName());
            schedulerDataVo.setShareTagetNameList(getShareGroupTargetName(mailDomainSeq, shareScheduleVo.getShareSeq(),
                    orgLocale, domainMessage));
        }
        if (shareExtList != null && shareExtList.size() > 0) {
            schedulerDataVo.setCheckShare("on");
            JSONArray jsonShareExtList = getShareExtUserName(shareExtList, true);
            if (shareScheduleVo != null) {
                JSONArray shareTargetNameList = schedulerDataVo.getShareTagetNameList();
                shareTargetNameList.addAll(jsonShareExtList);
                schedulerDataVo.setShareTagetNameList(shareTargetNameList);
            } else {
                schedulerDataVo.setUserName(shareExtList.get(0).getUserName());
                schedulerDataVo.setShareTagetNameList(jsonShareExtList);
            }
            schedulerDataVo.setShareSelfTargetList(getShareExtUserName(shareExtList, false));
        }

        return schedulerDataVo;
    }

    public JSONObject getJsonSchedule(int mailDomainSeq, int schedulerId, String orgLocale, String domainMessage) {
        SchedulerDataVO schedulerDataVo = getSchedule(mailDomainSeq, schedulerId, orgLocale, domainMessage);
        SchedulerBean schedulerBean = new SchedulerBean(schedulerDataVo);
        return schedulerBean.toJson();
    }

    public JSONArray makeAssetPlanInfoList(List<SchedulerAssetPlanVO> scheduleAssetPlanList) {
        JSONArray result = new JSONArray();

        if (scheduleAssetPlanList != null && scheduleAssetPlanList.size() > 0) {
            JSONObject data = null;
            for (SchedulerAssetPlanVO scheduleAssetPlan : scheduleAssetPlanList) {
                data = new JSONObject();
                data.put("categorySeq", scheduleAssetPlan.getCategorySeq());
                data.put("categoryName", scheduleAssetPlan.getCategoryName());
                data.put("assetSeq", scheduleAssetPlan.getAssetSeq());
                data.put("assetName", scheduleAssetPlan.getAssetName());
                data.put("contect", scheduleAssetPlan.getContect());
                result.add(data);
            }
        }
        return result;
    }

    public JSONArray getJsonScheduleList(int schedulerId, String firstDate, String lastDate) {
        SchedulerDataVO schedulerDataVo = schedulerDao.readSchedule(schedulerId);
        int start = Integer.parseInt(schedulerDataVo.getStartDate().substring(0, 8));
        int end = Integer.parseInt(schedulerDataVo.getEndDate().substring(0, 8));
        int first = Integer.parseInt(firstDate);
        int last = Integer.parseInt(lastDate);

        if (start < first) {
            start = first;
        }
        if (end > last) {
            end = last;
        }

        schedulerDataVo.setDrowStartDate(start);
        schedulerDataVo.setDrowEndDate(end);

        SchedulerHandler thisHandler = new SchedulerHandler();
        int planSize = thisHandler.getTermDay(Integer.toString(start), Integer.toString(end));
        schedulerDataVo.setPlanSize(planSize + 1);
        SchedulerBean schedulerBean = new SchedulerBean(schedulerDataVo);
        JSONArray array = new JSONArray();
        array.add(schedulerBean.toJson());
        return array;
    }

    public JSONArray getJsonSearchScheduleList(int mailDomainSeq, int mailUserSeq, String email, String searchType,
            String keyWord, int currentPage, int skipResult, int maxResult) {
        JSONArray beanArray = new JSONArray();

        List<SchedulerDataVO> schedulerDataList = schedulerDao.searchScheduleList(mailUserSeq, searchType, keyWord,
                skipResult, maxResult);
        int schedulerDataListCount = schedulerDao.searchScheduleListCount(mailUserSeq, searchType, keyWord);

        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        int[] schedulerIds = getSchedulerIds(shareScheduleList);
        List<SchedulerDataVO> searchShareScheduleList = schedulerDao.searchshareScheduleList(mailUserSeq, schedulerIds,
                searchType, keyWord, 0, Integer.MAX_VALUE);

        if (schedulerDataList != null && schedulerDataList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < schedulerDataList.size(); i++) {
                beanObject = new SchedulerBean(schedulerDataList.get(i)).toJson();
                beanObject.put("type", "basic");
                beanObject.put("schedulerCount", schedulerDataListCount);
                beanObject.put("currentPage", currentPage);
                beanObject.put("pageBase", maxResult);
                beanArray.add(beanObject);
            }
        }

        if (searchShareScheduleList != null && searchShareScheduleList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < searchShareScheduleList.size(); i++) {
                beanObject = new SchedulerBean(searchShareScheduleList.get(i)).toJson();
                beanObject.put("type", "share");
                beanArray.add(beanObject);
            }
        }

        return beanArray;
    }

    public JSONArray getDnDJsonSchedule(int year, int month, int schedulerId, String changeStartDate, String firstDate,
            String lastDate) {
        List<SchedulerDataVO> list = new ArrayList<SchedulerDataVO>();
        SchedulerDataVO schedulerDataVo = schedulerDao.readSchedule(schedulerId);
        String startDate = schedulerDataVo.getStartDate().substring(0, 8);
        String startTime = schedulerDataVo.getStartDate().substring(8);
        String endDate = schedulerDataVo.getEndDate().substring(0, 8);
        String endTime = schedulerDataVo.getEndDate().substring(8);

        SchedulerHandler thisHandler = null;
        if (year == 0 || month == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month);
        }

        List dateList = thisHandler.getMonthDayList(year, month);

        int planSize = thisHandler.getTermDay(startDate, endDate);
        String changeEndDate = thisHandler.getEndDate(changeStartDate, planSize);

        schedulerDataVo.setStartDate(changeStartDate + startTime);
        schedulerDataVo.setEndDate(changeEndDate + endTime);
        schedulerDataVo.setOutlookSync("unsync");
        schedulerDataVo.setModifyTime(FormatUtil.getBasicDateStr());
        schedulerDao.modifySchedule(schedulerDataVo);

        schedulerDataVo = null;

        schedulerDataVo = schedulerDao.readSchedule(schedulerId);

        int start = Integer.parseInt(schedulerDataVo.getStartDate().substring(0, 8));
        int end = Integer.parseInt(schedulerDataVo.getEndDate().substring(0, 8));
        int first = Integer.parseInt(firstDate);
        int last = Integer.parseInt(lastDate);

        if (start < first) {
            start = first;
        }
        if (end > last) {
            end = last;
        }

        schedulerDataVo.setDrowStartDate(start);
        schedulerDataVo.setDrowEndDate(end);
        schedulerDataVo.setDrowStartTime(Integer.parseInt(startTime));
        schedulerDataVo.setDrowEndTime(Integer.parseInt(endTime));
        planSize = thisHandler.getTermDay(Integer.toString(start), Integer.toString(end));
        schedulerDataVo.setPlanSize(planSize + 1);

        if (startDate.equals(endDate)) {
            schedulerDataVo.setTimeSize(thisHandler.getTodayTermTime(startTime, endTime));
        }

        schedulerDataVo.setCheckShare("off");
        SchedulerShareVO shareScheduleVo = schedulerDao.readShareSchedule(schedulerId);
        if ( "on".equalsIgnoreCase(schedulerDataVo.getCheckSendMail())
        		|| shareScheduleVo != null) {
            schedulerDataVo.setCheckShare("on");
        }

        list.add(schedulerDataVo);
        list = checkMonthDrow(list, dateList);

        JSONArray beanArray = new JSONArray();
        if (list != null && list.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < list.size(); i++) {
                beanObject = new SchedulerBean(list.get(i)).toJson();
                beanArray.add(beanObject);
            }
        }
        return beanArray;
    }

    private List<SchedulerHolidayVO> makeHoliday(int mailDomainSeq, int thisYear, String firstDay,
            SchedulerHandler thisHandler) {

        List<SchedulerHolidayVO> holidayList = schedulerDao.readScheduleHoliday(mailDomainSeq);

        if (holidayList != null && holidayList.size() > 0) {
            String holidayDate = null;
            int holidayIntDate;
            int firstLunar;
            int firstLunarDate;
            int solarDate;
            int fYear = Integer.parseInt(firstDay.substring(0, 4));
            int fMonth = Integer.parseInt(firstDay.substring(4, 6));
            int fDay = Integer.parseInt(firstDay.substring(6, 8));
            int year;
            String monthDay = null;

            for (int i = 0; i < holidayList.size(); i++) {
                holidayDate = holidayList.get(i).getHolidayDate();

                if (holidayDate != null && holidayDate.length() == 8) {
                    year = Integer.parseInt(holidayDate.substring(0, 4));
                    monthDay = holidayDate.substring(4);

                    if ("on".equalsIgnoreCase(holidayList.get(i).getRepeat())) {
                        year = thisYear;
                    }

                    if ("on".equalsIgnoreCase(holidayList.get(i).getLunar())) {
                        holidayIntDate = Integer.parseInt(monthDay);
                        firstLunar = getLunar(fYear, fMonth, fDay);
                        firstLunarDate = firstLunar % 10000;
                        if ((fMonth == 1) && (holidayIntDate >= firstLunarDate)) {
                            year = year - 1;
                        }
                        solarDate = thisHandler.getLunarTosolar(year, (holidayIntDate / 100), (holidayIntDate % 100));
                        holidayList.get(i).setHolidayDate(Integer.toString(solarDate));
                    } else {
                        monthDay = Integer.toString(year) + monthDay;
                        holidayList.get(i).setHolidayDate(monthDay);
                    }
                } else {
                    continue;
                }
            }
        }
        return holidayList;
    }

    public List<SchedulerHolidayVO> getHoliday(int mailDomainSeq, int thisYear, String firstDay) {
        SchedulerHandler thisHandler = new SchedulerHandler();
        return makeHoliday(mailDomainSeq, thisYear, firstDay, thisHandler);
    }

    public JSONArray getJsonHoliday(int mailDomainSeq, int thisYear, String firstDay) {
        JSONArray jsonArray = new JSONArray();
        List<SchedulerHolidayVO> holidayList = getHoliday(mailDomainSeq, thisYear, firstDay);
        if (holidayList != null && holidayList.size() > 0) {
            for (int i = 0; i < holidayList.size(); i++) {
                JSONObject jsonHoliday = new JSONObject();
                jsonHoliday.put("holidayDate", holidayList.get(i).getHolidayDate());
                jsonHoliday.put("lunar", holidayList.get(i).getLunar());
                jsonHoliday.put("holidayName", holidayList.get(i).getHolidayName());
                jsonHoliday.put("isHoliday", holidayList.get(i).getHoliday());

                jsonArray.add(jsonHoliday);
            }
        }
        return jsonArray;
    }

    public JSONObject getMonthInfo(int year, int month) {

        SchedulerVO monthScheduler = getMonthScheduler(year, month);
        monthScheduler = getTodayDate(monthScheduler);
        monthScheduler.setLunar(getLunar(monthScheduler.getThisYear(), monthScheduler.getThisMonth(),
                monthScheduler.getThisDay()));
        SchedulerDateBean schedulerDateBean = new SchedulerDateBean(monthScheduler);

        return schedulerDateBean.monthToJson();
    }

    public JSONObject getWeekInfo(int year, int month, int day) {

        SchedulerVO weekScheduler = getWeekScheduler(year, month, day);
        weekScheduler = getTodayDate(weekScheduler);
        weekScheduler.setFirstLunar(getLunar(weekScheduler.getFirstYear(), weekScheduler.getFirstMonth(),
                weekScheduler.getFirstDay()));
        weekScheduler.setLastLunar(getLunar(weekScheduler.getLastYear(), weekScheduler.getLastMonth(),
                weekScheduler.getLastDay()));
        SchedulerDateBean schedulerDateBean = new SchedulerDateBean(weekScheduler);

        return schedulerDateBean.weekToJson();
    }

    public JSONObject getDayInfo(int year, int month, int day) {

        SchedulerVO dayScheduler = getDayScheduler(year, month, day);
        dayScheduler = getTodayDate(dayScheduler);
        dayScheduler.setLunar(getLunar(dayScheduler.getThisYear(), dayScheduler.getThisMonth(),
                dayScheduler.getThisDay()));
        SchedulerDateBean schedulerDateBean = new SchedulerDateBean(dayScheduler);

        return schedulerDateBean.dayToJson();
    }

    // schedulerShare
    @Transactional
    public String saveShareGroup(SchedulerShareVO schedulerShareVo, String mailDomainSeq, String targetType,
            String[] targetValues) {
        String result = "success";
        try {
            schedulerDao.saveShareGroup(schedulerShareVo);

            SchedulerShareTargetVO schedulerShareTargetVo = null;

            if ("domain".equals(targetType)) {
                schedulerShareTargetVo = new SchedulerShareTargetVO();
                schedulerShareTargetVo.setShareSeq(schedulerShareVo.getShareSeq());
                schedulerShareTargetVo.setTargetType("domain");
                schedulerShareTargetVo.setTargetValue(mailDomainSeq);
                schedulerDao.saveShareGroupTarget(schedulerShareTargetVo);
            } else {
                if (targetValues != null && targetValues.length > 0) {
                    StringTokenizer st = null;
                    for (int i = 0; i < targetValues.length; i++) {
                        schedulerShareTargetVo = new SchedulerShareTargetVO();
                        schedulerShareTargetVo.setShareSeq(schedulerShareVo.getShareSeq());
                        st = new StringTokenizer(targetValues[i], "|");

                        if ("O".equalsIgnoreCase(st.nextToken())) {
                            schedulerShareTargetVo.setTargetType("org");
                        } else {
                            schedulerShareTargetVo.setTargetType("user");
                        }
                        schedulerShareTargetVo.setTargetValue(st.nextToken());

                        schedulerDao.saveShareGroupTarget(schedulerShareTargetVo);
                    }
                }
            }
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }

        return result;
    }

    @Transactional
    public String modifyShareGroup(SchedulerShareVO schedulerShareVo, String mailDomainSeq, String targetType,
            String[] targetValues) {
        String result = "success";
        try {
            schedulerDao.modifyShareGroup(schedulerShareVo);

            SchedulerShareTargetVO schedulerShareTargetVo = null;

            if ("domain".equals(targetType)) {
                schedulerShareTargetVo = new SchedulerShareTargetVO();
                schedulerShareTargetVo.setShareSeq(schedulerShareVo.getShareSeq());
                schedulerShareTargetVo.setTargetType("domain");
                schedulerShareTargetVo.setTargetValue(mailDomainSeq);
                schedulerDao.deleteShareGroupTarget(schedulerShareVo.getShareSeq());
                schedulerDao.saveShareGroupTarget(schedulerShareTargetVo);
            } else {
                if (targetValues != null && targetValues.length > 0) {
                    schedulerDao.deleteShareGroupTarget(schedulerShareVo.getShareSeq());
                    StringTokenizer st = null;
                    for (int i = 0; i < targetValues.length; i++) {
                        schedulerShareTargetVo = new SchedulerShareTargetVO();
                        schedulerShareTargetVo.setShareSeq(schedulerShareVo.getShareSeq());
                        st = new StringTokenizer(targetValues[i], "|");

                        if ("O".equalsIgnoreCase(st.nextToken())) {
                            schedulerShareTargetVo.setTargetType("org");
                        } else {
                            schedulerShareTargetVo.setTargetType("user");
                        }
                        schedulerShareTargetVo.setTargetValue(st.nextToken());

                        schedulerDao.saveShareGroupTarget(schedulerShareTargetVo);
                    }
                }
            }
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }

        return result;
    }

    public List<SchedulerShareVO> getShareGroupList(int mailUserSeq) {
        return schedulerDao.readShareGroupList(mailUserSeq);
    }

    public JSONArray getShareGroupJsonList(int mailUserSeq) {
        List<SchedulerShareVO> schedulerShareList = getShareGroupList(mailUserSeq);
        JSONArray shareGroupList = new JSONArray();
        if (schedulerShareList != null && schedulerShareList.size() > 0) {
            JSONObject shareGroup = null;
            SchedulerShareVO schedulerShareVo = null;
            for (int i = 0; i < schedulerShareList.size(); i++) {
                shareGroup = new JSONObject();
                schedulerShareVo = schedulerShareList.get(i);
                shareGroup.put("id", schedulerShareVo.getShareSeq());
                shareGroup.put("name", schedulerShareVo.getShareName());
                shareGroup.put("color", schedulerShareVo.getShareColor());
                shareGroupList.add(shareGroup);
            }
        }
        return shareGroupList;
    }

    @SuppressWarnings("unchecked")
    public JSONArray getShareGroupTargetName(int mailDomainSeq, int shareSeq, String orgLocale, String domainMessage) {
        JSONArray shareTargetArray = new JSONArray();
        List<SchedulerShareTargetVO> schedulerShareTargetList = getSchedulerShareTargetList(shareSeq);
        if (schedulerShareTargetList != null && schedulerShareTargetList.size() > 0) {
            String targetType = null;
            String targetValue = null;
            SearchUserVO searchUserVo = null;
            String[] deptArray = null;
            for (int i = 0; i < schedulerShareTargetList.size(); i++) {
                targetType = schedulerShareTargetList.get(i).getTargetType();
                targetValue = schedulerShareTargetList.get(i).getTargetValue();
                if ("user".equals(targetType)) {
                    searchUserVo = mailUserDao.readUserIdAndName(Integer.parseInt(targetValue));
                    if (searchUserVo != null) {
                        shareTargetArray.add(searchUserVo.getUserName());
                    }
                } else if ("org".equals(targetType)) {
                    deptArray = targetValue.split(":");
                    if (deptArray != null && deptArray.length > 1) {
                        DeptVO deptVo = null;
                        String deptName = "";
                        for (int j = 1; j < deptArray.length; j++) {
                            deptVo = organizationManager.readDept(mailDomainSeq, deptArray[j]);
                            if (deptVo != null) {
                                if ("jp".equals(orgLocale)) {
                                    deptVo.setOrgName(StringUtils.isEmpty(deptVo.getOrgJpName()) ? "&nbsp;" : deptVo
                                            .getOrgJpName());
                                } else if ("en".equals(orgLocale)) {
                                    deptVo.setOrgName(StringUtils.isEmpty(deptVo.getOrgEnName()) ? "&nbsp;" : deptVo
                                            .getOrgEnName());
                                }
                                if (StringUtils.isEmpty(deptName)) {
                                    deptName += deptVo.getOrgName();
                                } else {
                                    deptName += ">" + deptVo.getOrgName();
                                }
                            }
                        }
                        shareTargetArray.add(deptName);
                        deptName = "";
                    }
                } else if ("domain".equals(targetType)) {
                    shareTargetArray.add(domainMessage);
                }
            }
        }
        return shareTargetArray;
    }

    public JSONArray getShareExtUserName(List<SchedulerShareExtVO> extList, boolean isRead) {
        JSONArray shareTargetArray = new JSONArray();
        if (extList != null && extList.size() > 0) {
            String[] emailData = null;
            MailUserInfoVO userInfo = null;
            for (SchedulerShareExtVO schedulerShareExtVO : extList) {
                try {
                    if (isRead) {
                        emailData = schedulerShareExtVO.getEmail().split("@");
                        userInfo = mailUserDao.readMailUserInfoByUserAndDomain(emailData[0], emailData[1]);
                        if (userInfo != null && StringUtils.isNotEmpty(userInfo.getUserName())) {
                            shareTargetArray.add(userInfo.getUserName());
                        } else {
                            shareTargetArray.add(schedulerShareExtVO.getEmail());
                        }
                    } else {
                        shareTargetArray.add(schedulerShareExtVO.getEmail());
                    }
                } catch (Exception e) {
                	LogManager.writeErr(this, e.getMessage(), e);
                }
            }
        }
        return shareTargetArray;
    }

    public List<SchedulerShareTargetVO> getSchedulerShareTargetList(int shareSeq) {
        return schedulerDao.readShareTarget(shareSeq);
    }

    public JSONObject getShareGroup(int shareSeq) {
        SchedulerShareVO schedulerShareVo = schedulerDao.readShareGroup(shareSeq);
        JSONObject shareInfo = new JSONObject();
        JSONObject share = null;
        JSONArray shareTargetArray = null;
        List<SchedulerShareTargetVO> schedulerShareTargetList = null;
        if (schedulerShareVo != null) {
            share = new JSONObject();
            share.put("id", schedulerShareVo.getShareSeq());
            share.put("name", schedulerShareVo.getShareName());
            share.put("color", schedulerShareVo.getShareColor());

            schedulerShareTargetList = getSchedulerShareTargetList(shareSeq);

            if (schedulerShareTargetList != null && schedulerShareTargetList.size() > 0) {
                shareTargetArray = new JSONArray();
                JSONObject shareTargetInfo = null;
                String targetType = null;
                String targetValue = null;
                String userId = null;
                String userName = null;
                SearchUserVO searchUserVo = null;
                for (int i = 0; i < schedulerShareTargetList.size(); i++) {
                    shareTargetInfo = new JSONObject();

                    targetType = schedulerShareTargetList.get(i).getTargetType();
                    targetValue = schedulerShareTargetList.get(i).getTargetValue();
                    shareTargetInfo.put("targetType", targetType);
                    shareTargetInfo.put("targetValue", targetValue);

                    if ("user".equals(targetType)) {
                        searchUserVo = mailUserDao.readUserIdAndName(Integer.parseInt(targetValue));
                        if (searchUserVo != null) {
                            userId = searchUserVo.getMailUid();
                            userName = searchUserVo.getUserName();
                            shareTargetInfo.put("targetName", userId + "(" + userName + ")");
                        }
                    }
                    shareTargetArray.add(shareTargetInfo);
                }
            }
        }

        if (share == null || shareTargetArray == null) {
            shareInfo.put("isSuccess", "fail");
        } else {
            shareInfo.put("isSuccess", "success");
            shareInfo.put("share", share);
            shareInfo.put("shareTarget", shareTargetArray);
        }
        return shareInfo;
    }

    public String deleteShareGroup(int shareSeq) {
        String result = "success";

        try {
            schedulerDao.deleteShareGroup(shareSeq);
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }

        return result;
    }

    public int[] readSchedulerShareIds(int mailDomainSeq, int mailUserSeq, String email) {
        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        return getSchedulerIds(shareScheduleList);
    }

    public void schedulerOutlookMark(int[] schedulerIds, String modifyTime, String mark) {
        schedulerDao.schedulerOutlookMark(schedulerIds, modifyTime, mark);
    }

    public List<Integer> getSchedulerIdOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) {
        return schedulerDao.getSchedulerIdOutlookSyncStatus(mailUserSeq, syncStatus, firstDay);
    }

    public List<SchedulerDataVO> getSchedulerShareModifyTime(int[] schedulerIds, String firstDay) {
        return schedulerDao.getSchedulerShareModifyTime(schedulerIds, firstDay);
    }

    public List<SchedulerDataVO> getSchedulerListOutlookSyncStatus(int mailUserSeq, String syncStatus, String firstDay) {
        return schedulerDao.getSchedulerListOutlookSyncStatus(mailUserSeq, syncStatus, firstDay);
    }

    public List<SchedulerDataVO> getSchedulerShareListOutlookSyncStatus(int mailUserSeq, String syncStatus,
            String firstDay, int[] schedulerIds) {
        return schedulerDao.getSchedulerShareListOutlookSyncStatus(mailUserSeq, syncStatus, firstDay, schedulerIds);
    }

    public List<SchedulerDataVO> getSchedulerListByIds(int[] schedulerIds) {
        return schedulerDao.getSchedulebyIds(schedulerIds);
    }

    public void changeSyncFlag(int schedulerId, String syncStatus) {
        schedulerDao.changeSyncFlag(schedulerId, syncStatus);
    }

    public List<SchedulerDataVO> readRepeatIgnoreList(int schedulerId) {
        return schedulerDao.readRepeatIgnoreList(schedulerId);
    }

    public void deleteRepeatIgnore(int scheduleId) {
        schedulerDao.deleteRepeatIgnore(scheduleId);
    }

    public void saveRepeatIgnore(int scheduleId, String repeatStartDate) {
        schedulerDao.saveRepeatIgnore(scheduleId, repeatStartDate);
    }

    public JSONArray getJsonAssetCategoryList(int mailDomainSeq) {
        List<SchedulerAssetCategoryVO> schedulerAssetCategoryList = readSchedulerAssetCategoryList(mailDomainSeq);
        return makeJonAssetCategoryList(schedulerAssetCategoryList);
    }

    public List<SchedulerAssetCategoryVO> readSchedulerAssetCategoryList(int mailDomainSeq) {
        return schedulerDao.readSchedulerAssetCategoryList(mailDomainSeq);
    }

    public List<SchedulerAssetCategoryVO> makeAssetNotDuplicateList(int mailDomainSeq, int mailUserSeq,
            String startDate, String endDate) {
        return schedulerDao.readAssetNotDuplicateList(mailDomainSeq, mailUserSeq, startDate, endDate);
    }

    public JSONArray getAssetNotDuplicateList(int mailDomainSeq, int mailUserSeq, String startDate, String endDate) {
        List<SchedulerAssetCategoryVO> schedulerAssetCategoryList = makeAssetNotDuplicateList(mailDomainSeq,
                mailUserSeq, startDate, endDate);
        return makeJonAssetCategoryList(schedulerAssetCategoryList);
    }

    @SuppressWarnings("unchecked")
    private JSONArray makeJonAssetCategoryList(List<SchedulerAssetCategoryVO> schedulerAssetCategoryList) {
        JSONArray categoryArray = new JSONArray();
        if (schedulerAssetCategoryList != null && schedulerAssetCategoryList.size() > 0) {
            JSONObject category = null;
            List<SchedulerAssetVO> assetList = null;
            for (SchedulerAssetCategoryVO schedulerAssetCategoryVo : schedulerAssetCategoryList) {
                category = new JSONObject();

                category.put("categorySeq", schedulerAssetCategoryVo.getCategorySeq());
                category.put("categoryName", schedulerAssetCategoryVo.getCategoryName());

                assetList = schedulerAssetCategoryVo.getAssetList();
                if (assetList != null && assetList.size() > 0) {
                    JSONArray assetArray = new JSONArray();
                    JSONObject asset = null;
                    for (SchedulerAssetVO assetVo : assetList) {
                        asset = new JSONObject();
                        asset.put("assetSeq", assetVo.getAssetSeq());
                        asset.put("assetName", assetVo.getAssetName());
                        asset.put("assetType", assetVo.getAssetType());
                        asset.put("assetStatus", assetVo.getAssetStatus());

                        assetArray.add(asset);
                    }
                    category.put("assetList", assetArray);
                }

                categoryArray.add(category);
            }
        }
        return categoryArray;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getJsonAssetCategoryDescription(int categorySeq, MessageParserInfoBean infoBean) {
        JSONObject result = new JSONObject();

        SchedulerAssetCategoryVO schedulerAssetCategoryVo = schedulerDao
                .readSchedulerAssetCategoryDescription(categorySeq);
        String description = parseDescriptionImage(schedulerAssetCategoryVo.getCategoryDescription(),
                schedulerAssetCategoryVo.getCategoryImageList(), infoBean);
        result.put("categorySeq", schedulerAssetCategoryVo.getCategorySeq());
        result.put("categoryName", schedulerAssetCategoryVo.getCategoryName());
        result.put("categoryDescription", description);

        return result;
    }

    public JSONArray getAssetScheduleList(int year, int month, int day, int assetSeq) {
        JSONArray beanArray = new JSONArray();

        List<SchedulerAssetPlanVO> schedulerAssetPlanList = getSchedulerAssetPlanList(year, month, day, assetSeq);
        if (schedulerAssetPlanList != null && schedulerAssetPlanList.size() > 0) {
            JSONObject beanObject = null;
            for (int i = 0; i < schedulerAssetPlanList.size(); i++) {
                beanObject = new SchedulerAssetBean(schedulerAssetPlanList.get(i)).toJson();
                beanArray.add(beanObject);
            }
        }

        return beanArray;
    }

    public List<SchedulerAssetPlanVO> getSchedulerAssetPlanList(int year, int month, int day, int assetSeq) {
        SchedulerHandler thisHandler = null;
        List<SchedulerAssetPlanVO> list = new ArrayList<SchedulerAssetPlanVO>();
        if (year == 0 || month == 0 || day == 0) {
            thisHandler = new SchedulerHandler();
        } else {
            thisHandler = new SchedulerHandler(year, month, day);
        }
        int startDateOfWeek = thisHandler.getstartDateOfWeek(thisHandler.getthisYear(), thisHandler.getthisMonth(),
                thisHandler.getthisDay());
        int startYear = startDateOfWeek / 10000;
        int startMonth = (startDateOfWeek / 100) - (startYear * 100);
        int startDay = startDateOfWeek % 100;

        List dateList = thisHandler.getWeekDayList(startYear, startMonth, startDay);

        String firstDayStr = (String) dateList.get(0);
        String lastDayStr = (String) dateList.get(dateList.size() - 1);

        int firstDay = Integer.parseInt(firstDayStr);
        int lastDay = Integer.parseInt(lastDayStr);
        firstDayStr = firstDayStr + "0000";
        lastDayStr = lastDayStr + "2359";

        SchedulerAssetPlanVO schedulerAssetPlanVo = null;
        List<SchedulerAssetPlanVO> schedulerAssetPlanList = schedulerDao.readSchedulerAssetPlanList(assetSeq,
                firstDayStr, lastDayStr);

        for (int i = 0; i < schedulerAssetPlanList.size(); i++) {
            schedulerAssetPlanVo = schedulerAssetPlanList.get(i);

            int startDate = Integer.parseInt(schedulerAssetPlanVo.getStartDate().substring(0, 8));
            int endDate = Integer.parseInt(schedulerAssetPlanVo.getEndDate().substring(0, 8));

            String startTimeStr = schedulerAssetPlanVo.getStartDate().substring(8);
            String endTimeStr = schedulerAssetPlanVo.getEndDate().substring(8);
            int startTime = Integer.parseInt(startTimeStr);
            int endTime = Integer.parseInt(endTimeStr);

            if ((startDate < firstDay && endDate < firstDay) || (startDate > lastDay && endDate > lastDay)) {
                continue;
            } else {
                if (startDate < firstDay) {
                    startDate = firstDay;
                }
                if (endDate > lastDay) {
                    endDate = lastDay;
                }
            }

            int planSize = thisHandler.getTermDay(Integer.toString(startDate), Integer.toString(endDate));
            schedulerAssetPlanVo.setPlanSize(planSize + 1);
            schedulerAssetPlanVo.setDrowStartDate(startDate);
            schedulerAssetPlanVo.setDrowEndDate(endDate);
            schedulerAssetPlanVo.setDrowStartTime(startTime);
            schedulerAssetPlanVo.setDrowEndTime(endTime);

            schedulerAssetPlanVo = checkAssetPlanSize(schedulerAssetPlanVo, dateList, thisHandler);

            list.add(schedulerAssetPlanVo);

        }
        return list;
    }

    private SchedulerAssetPlanVO checkAssetPlanSize(SchedulerAssetPlanVO schedulerAssetPlanVo, List dateList,
            SchedulerHandler thisHandler) {
        int count = 0;
        int planSize = schedulerAssetPlanVo.getPlanSize();
        int firstDayTimeSize = 0;
        int allDayTimeSize = 0;
        int lastDayTimeSize = 0;
        String firstTimeStr = "0000";
        String endTimeStr = "2400";
        int startDate = Integer.parseInt(schedulerAssetPlanVo.getStartDate().substring(0, 8));
        int endDate = Integer.parseInt(schedulerAssetPlanVo.getEndDate().substring(0, 8));
        int drowStartDate = schedulerAssetPlanVo.getDrowStartDate();
        int drowEndDate = schedulerAssetPlanVo.getDrowEndDate();
        String drowStartDateStr = Integer.toString(drowStartDate);
        String drowStartTimeStr = schedulerAssetPlanVo.getStartDate().substring(8);
        String drowEndTimeStr = schedulerAssetPlanVo.getEndDate().substring(8);

        List<String> drowList = new ArrayList<String>();

        for (int i = 0; i < dateList.size(); i++) {
            if (drowStartDateStr.equals(dateList.get(i))) {
                count = i;
                break;
            }
        }

        if (planSize == 1) {
            firstDayTimeSize = thisHandler.getTodayTermTime(drowStartTimeStr, drowEndTimeStr);
            drowList.add(firstDayTimeSize + "|" + dateList.get(count) + "|" + drowStartTimeStr);
        } else if (planSize > 1) {
            allDayTimeSize = thisHandler.getTodayTermTime(firstTimeStr, endTimeStr);
            if (startDate >= drowStartDate) {
                firstDayTimeSize = thisHandler.getTodayTermTime(drowStartTimeStr, endTimeStr);
                drowList.add(firstDayTimeSize + "|" + dateList.get(count) + "|" + drowStartTimeStr + "|" + endTimeStr);
            } else {
                drowList.add(allDayTimeSize + "|" + dateList.get(count) + "|" + firstTimeStr + "|" + endTimeStr);
            }

            int index = 0;
            for (int i = 1; i < planSize - 1; i++) {
                drowList.add(allDayTimeSize + "|" + dateList.get(count + i) + "|" + firstTimeStr + "|" + endTimeStr);
                index = i;
            }

            if (drowEndDate >= endDate) {
                lastDayTimeSize = thisHandler.getTodayTermTime(firstTimeStr, drowEndTimeStr);
                drowList.add(lastDayTimeSize + "|" + dateList.get(count + index + 1) + "|" + firstTimeStr + "|"
                        + drowEndTimeStr);
            } else {
                drowList.add(allDayTimeSize + "|" + dateList.get(count + index + 1) + "|" + firstTimeStr + "|"
                        + endTimeStr);
            }
        }

        schedulerAssetPlanVo.setDrowList(drowList);

        return schedulerAssetPlanVo;
    }

    private String parseDescriptionImage(String description,
            List<SchedulerAssetCategoryImageVO> AssetCategoryImageList, MessageParserInfoBean infoBean) {
        if (AssetCategoryImageList != null && AssetCategoryImageList.size() > 0) {
            File file = null;
            String imageSrc = null;
            String imageUrl = null;
            String imageName = null;
            for (int i = 0; i < AssetCategoryImageList.size(); i++) {
                if (StringUtils.isEmpty(AssetCategoryImageList.get(i).getImageCid())) {
                    continue;
                }
                imageName = "_asset_inline" + i + "_" + Long.toString(System.nanoTime()) + ".gif";
                imageSrc = infoBean.getAttachesDir() + imageName;
                imageUrl = infoBean.getStrLocalhost() + infoBean.getAttachesUrl() + imageName;
                file = new File(imageSrc);
                FileUtil.writeFile(AssetCategoryImageList.get(i).getImageData(), file);
                description = description.replace("cid:" + AssetCategoryImageList.get(i).getImageCid(), imageUrl);
            }
        }
        return description;
    }

    public String saveAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) {
        String result = "success";
        try {
            schedulerDao.saveAssetSchedule(schedulerAssetPlanVo);
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public String modifyAssetSchedule(SchedulerAssetPlanVO schedulerAssetPlanVo) {
        String result = "success";
        try {
            schedulerDao.modifyAssetSchedule(schedulerAssetPlanVo);
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public JSONObject getJsonAssetSchedule(int planSeq) {
        SchedulerAssetPlanVO schedulerAssetPlanVo = schedulerDao.readAssetSchedule(planSeq);
        return new SchedulerAssetBean(schedulerAssetPlanVo).toJson();
    }

    public String deleteAssetSchedule(int planSeq) {
        String result = "success";
        try {
            schedulerDao.deleteAssetSchedule(planSeq);
        } catch (DataAccessException e) {
            LogManager.writeErr(this, e.getMessage(), e);
            result = "fail";
        }
        return result;
    }

    public int checkAssetScheduleDuplicateCount(int assetSeq, int planSeq, String startDate, String endDate) {
        return schedulerDao.checkAssetScheduleDuplicateCount(assetSeq, planSeq, startDate, endDate);
    }

    public SchedulerAssetPlanVO checkMyScheduleOrShareSchedule(int mailDomainSeq, int mailUserSeq, int planSeq,
            String email) {
        List<SchedulerDataVO> shareScheduleList = schedulerDao
                .readSchedulerShareInfo(mailDomainSeq, mailUserSeq, email);
        int[] schedulerIds = getSchedulerIds(shareScheduleList);
        return schedulerDao.checkMyScheduleOrShareSchedule(planSeq, mailUserSeq, schedulerIds);
    }

    public String makeEmail(int mailUserSeq) {
        Map<String, String> userInfo = mailUserDao.readUserInfoByUid(mailUserSeq);
        return userInfo.get("mail_uid") + "@" + userInfo.get("mail_domain");
    }

    public String makeDomainUserEmail(int mailDomainSeq, String mailDomain) {
        StringBuffer sb = new StringBuffer();
        List<String> mailUids = mailUserDao.readMailUids(mailDomainSeq);
        if (mailUids != null && mailUids.size() > 0) {
            for (int i = 0; i < mailUids.size(); i++) {
                sb.append(mailUids.get(i)).append("@").append(mailDomain).append(",");
            }
        }
        return sb.toString();
    }

    public boolean isShareSchedule(int schedulerId) {
        SchedulerShareVO shareScheduleVo = schedulerDao.readShareSchedule(schedulerId);
        if (shareScheduleVo == null) {
            return false;
        }
        return true;
    }

    public List<SchedulerDataVO> getScheduleListAfterSyncTime(int mailUserSeq, String syncTime, int count) {
        return schedulerDao.readScheduleListAfterSyncTime(mailUserSeq, syncTime, count);
    }

    public List<SchedulerDataVO> getDeletedScheduleList(int mailUserSeq, String syncTime, int count) {
        return schedulerDao.readDeletedScheduleListAfterSyncTime(mailUserSeq, syncTime, count);
    }

    public List<SchedulerDataVO> getModifiedScheduleIdList(int mailUserSeq, String syncTime, int count) {
        return schedulerDao.readModifiedScheduleListAfterSyncTime(mailUserSeq, syncTime, count);
    }

    public int readUnsyncedScheduleCount(int mailUserSeq, String syncTime) {
        return schedulerDao.readUnsyncedScheduleCount(mailUserSeq, syncTime);
    }

    public void deleteAssetScheduleBySchedulerId(int schedulerId) {
        schedulerDao.deleteAssetScheduleBySchedulerId(schedulerId);
    }

    public List<SchedulerShareExtVO> getSchedulerShareExtList(int schedulerId) {
        return schedulerDao.readSchedulerShareExtList(schedulerId);
    }

    public List<SchedulerAssetPlanVO> readAssetUseList(int mailDomainSeq, int schedulerId, int[] assetSeqs,
            String startDate, String endDate) {
        return schedulerDao.readAssetUseList(mailDomainSeq, schedulerId, assetSeqs, startDate, endDate);
    }

    public JSONArray readJsonAssetUseList(int mailDomainSeq, int schedulerId, int[] assetSeqs, String startDate,
            String endDate) {
        JSONArray assetArray = new JSONArray();
        JSONObject assetObj = null;
        List<SchedulerAssetPlanVO> assetUseList = readAssetUseList(mailDomainSeq, schedulerId, assetSeqs, startDate,
                endDate);
        for (SchedulerAssetPlanVO asset : assetUseList) {
            assetObj = new JSONObject();
            assetObj.put("assetSeq", asset.getAssetSeq());
            assetObj.put("assetName", asset.getAssetName());
            assetObj.put("contect", asset.getContect());
            assetObj.put("userName", asset.getUserName());
            assetArray.add(assetObj);
        }
        return assetArray;
    }

    @SuppressWarnings("unchecked")
    public void sendMailProcess(User user, I18nResources resource, String type, int schedulerId, int shareSeq, String subject, String content) {
        int mailDomainSeq = Integer.parseInt(user.get(User.MAIL_DOMAIN_SEQ));
        List<SchedulerShareTargetVO> schedulerShareTargetList = getSchedulerShareTargetList(shareSeq);
        List<SchedulerShareExtVO> schedulerShareExtList = getSchedulerShareExtList(schedulerId);
        StringBuffer sb = new StringBuffer();
        sb.append(user.get(User.EMAIL)).append(",");
        if (schedulerShareTargetList != null && schedulerShareTargetList.size() > 0) {
            String targetType = null;
            String targetValue = null;
            List<MailAddressBean> addrList = null;
            for (int i = 0; i < schedulerShareTargetList.size(); i++) {
                targetType = schedulerShareTargetList.get(i).getTargetType();
                targetValue = schedulerShareTargetList.get(i).getTargetValue();
                if ("user".equals(targetType)) {
                    sb.append(makeEmail(Integer.parseInt(targetValue))).append(",");
                } else if ("org".equals(targetType)) {
                    addrList = searchEmailManager.readAddressList(mailDomainSeq, targetValue);
                    sb.append(getMailAddressStr(addrList));
                } else if ("domain".equals(targetType)) {
                    sb.append(makeDomainUserEmail(mailDomainSeq, user.get(User.MAIL_DOMAIN)));
                }
            }
        }

        if (schedulerShareExtList != null && schedulerShareExtList.size() > 0) {
            for (SchedulerShareExtVO schedulerShareExt : schedulerShareExtList) {
                sb.append(schedulerShareExt.getEmail()).append(",");
            }
        }

        try {
            Properties props = new Properties();
            props.setProperty("mail.debug", "false");
            props.setProperty("mail.transport.protocol", "smtp");

            Session session = Session.getDefaultInstance(props, null);

            String emails = sb.toString();

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setContent(content, "text/html; charset=utf-8");
            mimeMessage.addHeader("Content-Transfer-Encoding", "base64");
            mimeMessage.setSubject(subject, "utf-8");
            mimeMessage.setFrom(new InternetAddress(user.get(User.EMAIL), user.get(User.USER_NAME), "utf-8"));
            mimeMessage.setRecipients(Message.RecipientType.TO, TMailAddress.getInternetAddress(emails));
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

            String mid = mimeMessage.getMessageID();
            if (mid.charAt(0) == '<' && mid.charAt(mid.length() - 1) == '>') {
                mid = mid.substring(1, mid.length() - 1);
            }

            if (StringUtils.isNotEmpty(emails)) {
                InternetAddress[] addresses = TMailAddress.getInternetAddress(emails);
                Protocol ladminProtocol = new Protocol(user.get(User.MAIL_HOST));

                SenderInfoBean info = new SenderInfoBean();
                info.setIasTo(addresses);
                info.setIasRcptto(addresses);
                info.setMessageId(mid);
                info.setSentDate(new Date());
                info.setSenderEmail(user.get(User.EMAIL));
                info.setSenderName(user.get(User.USER_NAME));
                info.setCharset("UTF-8");
                info.setSubject(subject);
                info.setHtmlMode(true);
                info.setContent(content);
                info.setUser(user);

                ladminManager.setResource(ladminProtocol, resource);
                info.setLadminManager(ladminManager);

                MailSendResultBean sendResult = new MailSendResultBean();
                SendHandler sendHandler = new BatchSendHandler(session, info, sendResult, null);
                sendHandler.sendMailMessage(null, null);
            }

            if ("delete".equals(type)) {
                deleteAssetScheduleBySchedulerId(schedulerId);
            }
        } catch (Exception e) {
        	LogManager.writeErr(this, e.getMessage(), e);
            // TODO: handle exception
        }
    }

    private String getMailAddressStr(List<MailAddressBean> list) {
        StringBuffer sb = new StringBuffer();
        MailAddressBean mailAddressBean = null;
        if (list.size() > 0) {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                mailAddressBean = (MailAddressBean) iterator.next();
                sb.append(mailAddressBean.getAddress());
                sb.append(",");
            }
        }
        mailAddressBean = null;
        return sb.toString();
    }
}
