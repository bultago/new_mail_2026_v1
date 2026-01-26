package com.terracetech.tims.webmail.scheduler.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.icu.util.ChineseCalendar;
import com.terracetech.tims.webmail.util.StringUtils;

public class SchedulerHandler {
    private SimpleDateFormat BASIC_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private Calendar cal;
    private Calendar caltmp;

    public SchedulerHandler() {
        cal = Calendar.getInstance();
        caltmp = Calendar.getInstance();
    }

    public SchedulerHandler(Date d) {
        cal = Calendar.getInstance();
        cal.setTime(d);
        caltmp = Calendar.getInstance();
    }

    public SchedulerHandler(long t) {
        cal = Calendar.getInstance();
        cal.setTimeInMillis(t);
        caltmp = Calendar.getInstance();
    }

    public SchedulerHandler(int year, int month, int day) {
        cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        caltmp = Calendar.getInstance();
    }

    public SchedulerHandler(int year, int month) {
        cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        caltmp = Calendar.getInstance();
    }

    public Date getthisDate() {
        return cal.getTime();
    }

    public int getthisYear() {
        return cal.get(Calendar.YEAR);
    }

    public int getthisMonth() {
        return (cal.get(Calendar.MONTH) + 1);
    }

    public int getthisDay() {
        return cal.get(Calendar.DATE);
    }

    public int getthisHour() {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getthisMin() {
        return cal.get(Calendar.MINUTE);
    }

    public int getthisDayOfWeek() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public String getthisYyyymmdd() {
        return BASIC_DATE_FORMAT.format(cal.getTime());
    }

    public int getprevYear() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.YEAR, -1);
        return caltmp.get(Calendar.YEAR);
    }

    public int getprevMonth() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.MONTH, -1);
        return (caltmp.get(Calendar.MONTH) + 1);
    }

    public int getprevDay() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.DATE, -1);
        return caltmp.get(Calendar.DATE);
    }

    public int getnextYear() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.YEAR, 1);
        return caltmp.get(Calendar.YEAR);
    }

    public int getnextMonth() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.MONTH, 1);
        return caltmp.get(Calendar.MONTH);
    }

    public int getnextDay() {
        caltmp.setTime(cal.getTime());
        caltmp.add(Calendar.DATE, 1);
        return cal.get(Calendar.DATE);
    }

    public int getfirstDateOfMonth() {
        caltmp.setTime(cal.getTime());
        caltmp.set(Calendar.DAY_OF_MONTH, 1);

        return caltmp.get(Calendar.DAY_OF_WEEK);
    }

    public String getfirstDateOfMonthStr() {
        caltmp.setTime(cal.getTime());
        caltmp.set(Calendar.DAY_OF_MONTH, 1);

        return BASIC_DATE_FORMAT.format(caltmp.getTime());
    }

    public int getmaxDayOfMonth() {
        caltmp.setTime(cal.getTime());
        return caltmp.getActualMaximum(Calendar.DATE);
    }

    public String getmaxDayOfMonthStr() {
        caltmp.setTime(cal.getTime());
        caltmp.set(Calendar.DAY_OF_MONTH, caltmp.getActualMaximum(Calendar.DATE));
        return BASIC_DATE_FORMAT.format(caltmp.getTime());
    }

    public int getstartDateOfWeek(int year, int month, int day) {
        caltmp.set(year, month - 1, day);
        int current = caltmp.get(Calendar.DAY_OF_WEEK);
        caltmp.add(Calendar.DATE, -current + 1);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public int getendDateOfWeek(int year, int month, int day) {

        caltmp.set(year, month - 1, day);
        int week = 7;
        int current = caltmp.get(Calendar.DAY_OF_WEEK);
        caltmp.add(Calendar.DATE, week - current);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public List getWeekDayList(int year, int month, int day) {
        List list = new ArrayList();
        for (int i = 0; i < 7; i++) {
            caltmp.set(year, month - 1, day + i);
            list.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
        }
        return list;
    }

    public List getMonthDayList(int year, int month) {
        List list = new ArrayList();
        caltmp.set(year, month - 1, 1);
        int start = caltmp.get(Calendar.DAY_OF_WEEK);
        if (start > 1) {
            caltmp.add(Calendar.DATE, -start + 1);
            for (int i = 1; i <= start - 1; i++) {
                list.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
                caltmp.add(Calendar.DATE, 1);
            }
        }

        int maxdayofmonth = caltmp.getActualMaximum(Calendar.DATE);

        for (int i = 0; i < maxdayofmonth; i++) {
            list.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
            caltmp.add(Calendar.DATE, 1);
        }
        if ((list.size()) % 7 != 0) {
            int nextmonthday = 7 - (list.size() % 7);
            for (int i = 0; i < nextmonthday; i++) {
                list.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
                caltmp.add(Calendar.DATE, 1);
            }
        }

        return list;
    }

    public int getPrevWeek(int year, int month, int day) {
        caltmp.set(year, month - 1, day);
        int week = 7;
        caltmp.add(Calendar.DATE, -week);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public int getNextWeek(int year, int month, int day) {
        caltmp.set(year, month - 1, day);
        int week = 7;
        caltmp.add(Calendar.DATE, week);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public int getPrevDay(int year, int month, int day) {
        caltmp.set(year, month - 1, day);
        caltmp.add(Calendar.DATE, -1);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public int getNextDay(int year, int month, int day) {
        caltmp.set(year, month - 1, day);
        int week = 7;
        caltmp.add(Calendar.DATE, 1);

        return Integer.parseInt(BASIC_DATE_FORMAT.format(caltmp.getTime()));
    }

    public int getLunar(int year, int month, int day) {
        ChineseCalendar lunar = new ChineseCalendar();
        caltmp.set(year, month - 1, day);
        lunar.setTimeInMillis(caltmp.getTimeInMillis());
        int y = lunar.get(com.ibm.icu.util.Calendar.EXTENDED_YEAR) - 2637;
        int m = lunar.get(com.ibm.icu.util.Calendar.MONTH) + 1;
        int d = lunar.get(com.ibm.icu.util.Calendar.DAY_OF_MONTH);

        return y * 10000 + m * 100 + d;
    }

    public int getLunarTosolar(int year, int month, int day) {
        ChineseCalendar lunar = new ChineseCalendar();
        lunar.set(com.ibm.icu.util.Calendar.EXTENDED_YEAR, year + 2637);
        lunar.set(com.ibm.icu.util.Calendar.MONTH, month - 1);
        lunar.set(com.ibm.icu.util.Calendar.DAY_OF_MONTH, day);
        caltmp.setTimeInMillis(lunar.getTimeInMillis());

        int y = caltmp.get(Calendar.YEAR);
        int m = caltmp.get(Calendar.MONTH) + 1;
        int d = caltmp.get(Calendar.DAY_OF_MONTH);

        return y * 10000 + m * 100 + d;
    }

    public int getTermDay(String startDate, String endDate) {
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(4, 6));
        int startDay = Integer.parseInt(startDate.substring(6));

        int endYear = Integer.parseInt(endDate.substring(0, 4));
        int endMonth = Integer.parseInt(endDate.substring(4, 6));
        int endDay = Integer.parseInt(endDate.substring(6));

        caltmp.set(startYear, startMonth - 1, startDay);
        long startTime = caltmp.getTimeInMillis();
        caltmp.set(endYear, endMonth - 1, endDay);
        long endTime = caltmp.getTimeInMillis();
        int day = 1000 * 60 * 60 * 24;
        int term = (int) ((endTime - startTime) / day);

        return term;
    }

    public int getTodayTermTime(String startTime, String endTime) {

        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(2, 4));

        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(2, 4));

        caltmp.set(Calendar.HOUR_OF_DAY, startHour);
        caltmp.set(Calendar.MINUTE, startMinute);
        long startMillis = caltmp.getTimeInMillis();
        caltmp.set(Calendar.HOUR_OF_DAY, endHour);
        caltmp.set(Calendar.MINUTE, endMinute);
        long endMillis = caltmp.getTimeInMillis();
        int halfHour = 1000 * 60 * 30;
        int term = (int) ((endMillis - startMillis) / halfHour);

        return term;
    }

    public List<String> getRepeatDays(int firstDate, int lastDate, String startDate, String repeatTerm,
            String repeatEndDate) {

        List<String> startList = null;

        int firstYear = firstDate / 10000;
        int firstMonth = (firstDate / 100) - (firstYear * 100);
        int firstDay = firstDate % 100;

        int lastYear = lastDate / 10000;
        int lastMonth = (lastDate / 100) - (lastYear * 100);
        int lastDay = lastDate % 100;

        int start = Integer.parseInt(startDate.substring(0, 8));
        int startYear = start / 10000;
        int startMonth = (start / 100) - (startYear * 100);
        int startDay = start % 100;

        long endMillis = 0;

        if (start <= lastDate) {

            caltmp.set(firstYear, firstMonth - 1, firstDay);
            long firstMillis = caltmp.getTimeInMillis();
            caltmp.set(lastYear, lastMonth - 1, lastDay);
            long lastMillis = caltmp.getTimeInMillis();

            if (repeatEndDate != null && repeatEndDate.length() > 0) {
                int endDate = Integer.parseInt(repeatEndDate);
                int endYear = endDate / 10000;
                int endMonth = (endDate / 100) - (endYear * 100);
                int endDay = endDate % 100;
                caltmp.set(endYear, endMonth - 1, endDay);
                endMillis = caltmp.getTimeInMillis();
            }
            caltmp.set(startYear, startMonth - 1, startDay);
            long startMillis = caltmp.getTimeInMillis();

            int repeatType = Integer.parseInt(repeatTerm.substring(0, 2));

            switch (repeatType) {
            case 1:
                startList = getDayRepeat(caltmp, repeatTerm, firstMillis, lastMillis, endMillis, startMillis);
                break;
            case 2:
                startList = getWeekRepeat(caltmp, repeatTerm, firstMillis, lastMillis, endMillis, startMillis);
                break;
            case 3:
                startList = getMonthRepeat(caltmp, repeatTerm, firstMillis, lastMillis, endMillis, startMillis);
                break;
            case 4:
                startList = getYearRepeat(caltmp, repeatTerm, firstMillis, lastMillis, endMillis, startMillis);
                break;
            default:
                break;
            }
        }
        return startList;
    }

    private List<String> getDayRepeat(Calendar caltmp, String repeatTerm, long firstMillis, long lastMillis,
            long endMillis, long startMillis) {

        List<String> startList = new ArrayList<String>();
        int termSize = Integer.parseInt(repeatTerm.substring(4));
        long tmpEndMills = 0;
        boolean isSmall = false;
        if (firstMillis <= startMillis) {
            isSmall = true;
        }
        while (firstMillis > startMillis) {
            tmpEndMills = caltmp.getTimeInMillis();
            caltmp.add(Calendar.DATE, termSize);
            startMillis = caltmp.getTimeInMillis();
            if (firstMillis <= startMillis) {
                isSmall = true;
                break;
            }
        }

        cal.setTimeInMillis(tmpEndMills);
        startList.add(BASIC_DATE_FORMAT.format(cal.getTime()));

        while (firstMillis <= startMillis) {
            if (isSmall && (startMillis <= lastMillis) && ((endMillis == 0) || (endMillis >= caltmp.getTimeInMillis()))) {
                startList.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
            }
            caltmp.add(Calendar.DATE, termSize);
            if (lastMillis < caltmp.getTimeInMillis() || ((endMillis != 0) && (endMillis < caltmp.getTimeInMillis()))) {
                break;
            }
            isSmall = true;
        }
        return startList;
    }

    private List<String> getWeekRepeat(Calendar caltmp, String repeatTerm, long firstMillis, long lastMillis,
            long endMillis, long startMillis) {

        List<String> startList = new ArrayList<String>();

        int termSize1 = Integer.parseInt(repeatTerm.substring(2, 4));
        String subTermStr = repeatTerm.substring(4);
        String[] subTermArray = new String[subTermStr.length() / 2];

        for (int i = 0; i < subTermStr.length() / 2; i++) {
            subTermArray[i] = subTermStr.substring(i * 2, (i * 2) + 2);
        }

        Calendar temp = Calendar.getInstance();
        int termSize2 = 0;
        long tmpStartMillis = 0;
        long tmpEndMills = 0;
        long tmpPassMills = 0;
        boolean isSmall = false;
        long tempTimeMillis = caltmp.getTimeInMillis();

        for (int i = 0; i < subTermArray.length; i++) {
            tmpStartMillis = startMillis;
            temp.setTimeInMillis(tempTimeMillis);
            termSize2 = Integer.parseInt(subTermArray[i]);

            if (firstMillis <= tmpStartMillis) {
                temp.set(Calendar.DAY_OF_WEEK, termSize2);
                if (tmpStartMillis > temp.getTimeInMillis()) {
                    temp.add(Calendar.DAY_OF_WEEK_IN_MONTH, termSize1);
                }
                isSmall = true;
            }

            while (firstMillis > tmpStartMillis) {
                temp.set(Calendar.DAY_OF_WEEK, termSize2);
                tmpStartMillis = temp.getTimeInMillis();
                if (firstMillis <= tmpStartMillis) {
                    tmpEndMills = tmpPassMills;
                    isSmall = true;
                    break;
                } else {
                    tmpPassMills = temp.getTimeInMillis();
                    temp.add(Calendar.DAY_OF_WEEK_IN_MONTH, termSize1);
                    tmpEndMills = temp.getTimeInMillis();
                }
            }

            if (i == 0) {
                cal.setTimeInMillis(tmpEndMills);
                startList.add(BASIC_DATE_FORMAT.format(cal.getTime()));
            }

            while (firstMillis <= tmpStartMillis) {
                if (isSmall && (tmpStartMillis <= lastMillis && lastMillis >= temp.getTimeInMillis())
                        && ((endMillis == 0) || (endMillis >= temp.getTimeInMillis()))) {
                    startList.add(BASIC_DATE_FORMAT.format(temp.getTime()));
                }
                temp.set(Calendar.DAY_OF_WEEK, termSize2);
                temp.add(Calendar.DAY_OF_WEEK_IN_MONTH, termSize1);
                if (lastMillis < temp.getTimeInMillis() || ((endMillis != 0) && (endMillis < temp.getTimeInMillis()))) {
                    break;
                }
                isSmall = true;
            }

            termSize2 = 0;
            tmpEndMills = 0;
            isSmall = false;
        }
        return startList;
    }

    private List<String> getMonthRepeat(Calendar caltmp, String repeatTerm, long firstMillis, long lastMillis,
            long endMillis, long startMillis) {

        List<String> startList = new ArrayList<String>();

        int termSize1 = Integer.parseInt(repeatTerm.substring(2, 4));
        int subTermLength = repeatTerm.substring(4).length();
        int termSize2 = Integer.parseInt(repeatTerm.substring(4, 6));
        int termSize3 = 0;
        long tmpEndMills = 0;
        if (subTermLength == 4) {
            termSize3 = Integer.parseInt(repeatTerm.substring(6));
        }
        boolean isOver = false;
        boolean isSmall = false;

        setMonthRepeatTerm(caltmp, subTermLength, termSize2, termSize3);

        if (firstMillis > startMillis) {
            while (firstMillis > startMillis) {
                startMillis = caltmp.getTimeInMillis();
                tmpEndMills = startMillis;
                if (firstMillis <= startMillis) {
                    if (startMillis <= lastMillis && (endMillis == 0 || endMillis <= lastMillis)) {
                        isSmall = true;
                        break;
                    } else if (startMillis > lastMillis) {
                        isOver = true;
                        break;
                    }
                } else {
                    caltmp.add(Calendar.MONTH, termSize1);

                    setMonthRepeatTerm(caltmp, subTermLength, termSize2, termSize3);
                }
            }
            cal.setTimeInMillis(tmpEndMills);
            cal.add(Calendar.MONTH, -(termSize1));
            int thisMonth = cal.get(Calendar.MONTH);
            int thisDate = cal.get(Calendar.DATE);
            int maxdayOfMonth = cal.getActualMaximum(Calendar.DATE);

            setMonthRepeatTerm(cal, subTermLength, termSize2, termSize3);
            if (subTermLength == 2) {
                if (termSize2 != thisDate) {
                    cal.add(Calendar.MONTH, -2);
                    setMonthRepeatTerm(cal, subTermLength, termSize2, termSize3);
                    maxdayOfMonth = cal.getActualMaximum(Calendar.DATE);
                }
                if (thisMonth == cal.get(Calendar.MONTH)
                        || (firstMillis <= cal.getTimeInMillis() && cal.getTimeInMillis() <= lastMillis && maxdayOfMonth >= termSize2)) {
                    startList.add(BASIC_DATE_FORMAT.format(cal.getTime()));
                }
            } else {
                startList.add(BASIC_DATE_FORMAT.format(cal.getTime()));
            }
        } else {
            isSmall = true;
            if (startMillis > lastMillis) {
                isOver = true;
            }
        }

        setMonthRepeatTerm(caltmp, subTermLength, termSize2, termSize3);

        boolean overDate = false;
        while (!overDate) {
            if (((firstMillis <= caltmp.getTimeInMillis() && caltmp.getTimeInMillis() <= lastMillis) || (isSmall && caltmp
                    .getTimeInMillis() <= lastMillis))
                    && !isOver
                    && ((endMillis == 0) || (endMillis >= caltmp.getTimeInMillis()))) {
                startList.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));

                caltmp.add(Calendar.MONTH, (termSize1));

                int thisMonth = caltmp.get(Calendar.MONTH);

                setMonthRepeatTerm(caltmp, subTermLength, termSize2, termSize3);

                if (thisMonth != caltmp.get(Calendar.MONTH) || caltmp.getTimeInMillis() > lastMillis) {
                    overDate = true;
                }
            } else {
                overDate = true;
            }
        }
        return startList;
    }

    private List<String> getYearRepeat(Calendar caltmp, String repeatTerm, long firstMillis, long lastMillis,
            long endMillis, long startMillis) {

        List<String> startList = new ArrayList<String>();

        int termSize1 = Integer.parseInt(repeatTerm.substring(2, 4));
        int termSize2 = Integer.parseInt(repeatTerm.substring(4));
        long tmpEndMills = 0;
        boolean isSmall = false;

        caltmp.set(Calendar.MONTH, termSize1 - 1);
        caltmp.set(Calendar.DAY_OF_MONTH, termSize2);

        while (firstMillis > startMillis) {
            startMillis = caltmp.getTimeInMillis();
            tmpEndMills = startMillis;
            if (firstMillis <= startMillis && startMillis <= lastMillis
                    && ((endMillis == 0) || (endMillis >= caltmp.getTimeInMillis()))) {
                isSmall = true;
                break;
            }
            caltmp.add(Calendar.YEAR, 1);
            caltmp.set(Calendar.MONTH, termSize1 - 1);
            caltmp.set(Calendar.DAY_OF_MONTH, termSize2);
        }

        cal.setTimeInMillis(tmpEndMills);
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.MONTH, termSize1 - 1);
        cal.set(Calendar.DAY_OF_MONTH, termSize2);
        startList.add(BASIC_DATE_FORMAT.format(cal.getTime()));

        int maxdayOfMonth = caltmp.getActualMaximum(Calendar.DATE);
        int thisMonth = caltmp.get(Calendar.MONTH);

        boolean isThisMonth = true;
        if (isSmall) {
            if (termSize1 < (thisMonth + 1) || (maxdayOfMonth < termSize2)) {
                isThisMonth = false;
            }
        }

        if (isThisMonth
                && ((isSmall) || (firstMillis <= caltmp.getTimeInMillis() && caltmp.getTimeInMillis() <= lastMillis))) {
            startList.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
        }
        return startList;
    }

    private void setMonthRepeatTerm(Calendar caltmp, int subTermLength, int term1, int term2) {
        if (subTermLength == 2) {
            caltmp.set(Calendar.DAY_OF_MONTH, term1);
        } else {
            caltmp.set(Calendar.DAY_OF_WEEK, term2);
            caltmp.set(Calendar.DAY_OF_WEEK_IN_MONTH, term1);
        }
    }

    public Map<String, String> getRepeatPreDay(String first, String start, String repeatEnd, int planSize) {
        Map<String, String> preMap = new HashMap<String, String>();

        int startYear = Integer.parseInt(start.substring(0, 4));
        int startMonth = Integer.parseInt(start.substring(4, 6));
        int startDay = Integer.parseInt(start.substring(6));

        int firstYear = Integer.parseInt(first.substring(0, 4));
        int firstMonth = Integer.parseInt(first.substring(4, 6));
        int firstDay = Integer.parseInt(first.substring(6));

        int realPlanSize = planSize - 1;

        long endMiles = 0;

        cal.set(firstYear, firstMonth - 1, firstDay);
        long firstMiles = cal.getTimeInMillis();

        if (StringUtils.isNotEmpty(repeatEnd)) {
            int endYear = Integer.parseInt(repeatEnd.substring(0, 4));
            int endMonth = Integer.parseInt(repeatEnd.substring(4, 6));
            int endDay = Integer.parseInt(repeatEnd.substring(6));
            cal.set(endYear, endMonth - 1, endDay);
            endMiles = cal.getTimeInMillis();
        }

        cal.set(startYear, startMonth - 1, startDay);
        cal.add(Calendar.DATE, planSize - 1);
        long lastMiles = cal.getTimeInMillis();

        caltmp.set(startYear, startMonth - 1, startDay);
        long startMiles = caltmp.getTimeInMillis();

        if (firstMiles > startMiles) {
            if (planSize > 1) {
                for (int i = 0; i < planSize; i++) {
                    caltmp.add(Calendar.DATE, 1);
                    if ((endMiles == 0 || firstMiles <= endMiles) && firstMiles <= caltmp.getTimeInMillis()
                            && realPlanSize > 0) {
                        preMap.put("startDate", start);
                        preMap.put("drowStartDate", first);

                        caltmp.set(startYear, startMonth - 1, startDay);

                        if (endMiles > 0) {
                            int planCount = 0;
                            for (int j = 0; j < planSize; j++) {
                                if (caltmp.getTimeInMillis() >= lastMiles || caltmp.getTimeInMillis() >= endMiles) {
                                    break;
                                }
                                caltmp.add(Calendar.DATE, 1);
                                planCount++;
                            }
                            realPlanSize = planSize - (planSize - realPlanSize) - (planSize - planCount) + 1;
                        } else {
                            caltmp.add(Calendar.DATE, planSize - 1);
                        }
                        preMap.put("planSize", Integer.toString(realPlanSize));
                        preMap.put("endDate", BASIC_DATE_FORMAT.format(caltmp.getTime()));
                        break;
                    } else {
                        realPlanSize--;
                    }
                }
            }
        }
        return preMap;
    }

    public List<String> getRepeatEndDays(List<String> startList, int planSize) {
        List<String> endList = new ArrayList<String>();
        int startYear = 0;
        int startMonth = 0;
        int startDay = 0;

        for (int i = 0; i < startList.size(); i++) {
            startYear = Integer.parseInt(startList.get(i).substring(0, 4));
            startMonth = Integer.parseInt(startList.get(i).substring(4, 6));
            startDay = Integer.parseInt(startList.get(i).substring(6));

            caltmp.set(startYear, startMonth - 1, startDay);
            caltmp.add(Calendar.DATE, planSize - 1);

            endList.add(BASIC_DATE_FORMAT.format(caltmp.getTime()));
        }

        return endList;
    }

    public String getEndDate(String startDate, int planSize) {
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(4, 6));
        int startDay = Integer.parseInt(startDate.substring(6));

        caltmp.set(startYear, startMonth - 1, startDay);
        caltmp.add(Calendar.DATE, planSize);

        return BASIC_DATE_FORMAT.format(caltmp.getTime());
    }

}
