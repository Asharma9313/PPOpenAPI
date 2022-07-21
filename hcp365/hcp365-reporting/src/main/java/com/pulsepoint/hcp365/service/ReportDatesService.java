package com.pulsepoint.hcp365.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Service
public class ReportDatesService {
    public String[] generateReportDates(Object[] schedule) throws ParseException {
        Date startDate = getDateBeforeDays(1, false);
        Date endDate = Calendar.getInstance().getTime();
        switch (Integer.parseInt(schedule[1].toString())) {
            case 1: { // Past 7 days
                endDate = getDateBeforeDays(1, false);
                startDate = getDateBeforeDays(7, true);
                break;
            }
            case 2: // Past 30 days
                endDate = getDateBeforeDays(1, false);
                startDate = getDateBeforeDays(30, true);
                break;
            case 3: //Month to Date
                endDate = getDateBeforeDays(0, false);
                startDate = getDateBeforeDays(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1, true);
                break;
            case 4: //Week to Date
                endDate = Calendar.getInstance().getTime();
                //Don't change to day of week - 2 to get Monday as week start day instead of Sunday.
                startDate = getDateBeforeDays(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1, true);
                break;
            case 5: //Quarter to Date
                endDate = Calendar.getInstance().getTime();
                startDate = getQuarterStatDate(false);
                break;
            case 6: //Flight to Date
                break;
            case 7: //Lifetime
                break;
            case 8: //Yesterday
                endDate = getDateBeforeDays(1, false);
                startDate = getDateBeforeDays(1, true);
                break;
            case 9: //Last xxx days/weeks/months/quarters
                endDate = getDateBeforeDays(1, false);
                int periodicalNumber = Integer.parseInt(schedule[4].toString());
                Calendar cal = Calendar.getInstance();
                int today = cal.get(Calendar.DATE);
                setTimeToZero(cal);
                switch (Integer.parseInt(schedule[3].toString())){ //Periodical Number Type - Days/Weeks/Months/Quarters
                    case 1: //days
                        startDate = getDateBeforeDays(periodicalNumber, true);
                        break;
                    case 2: //weeks
                    {
                        startDate =calculateStartDateForWeek(periodicalNumber, null);
                        endDate = calculatePreviousWeekEndDate(null);
                        break;
                    }
                    case 3: //months
                    {
                        startDate =calculateMonthsStartDate(periodicalNumber, null);
                        endDate =calculateMonthEndDate(null);
                        break;
                    }
                    case 4://quarters
                        startDate = calculateQuaterStartDate(periodicalNumber,null);
                        endDate = calculateQuaterEndDate(startDate,periodicalNumber);
                        break;
                }
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        //formatter.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String[] dates = new String[2];
        dates[0] = formatter.format(startDate);
        dates[1] = formatter.format(endDate);
        return dates;
    }
    private Date getDateBeforeDays(int days, boolean isTimeZero) {
        Calendar cal = Calendar.getInstance();
        if (days > 0) {
            cal.add(Calendar.DATE, (-1 * days));
        }
        if (isTimeZero) {
            setTimeToZero(cal);
        } else {
            setTimeToEndOfDay(cal);
        }
        return cal.getTime();
    }
    private void setTimeToZero(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    private  void setTimeToEndOfDay(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
    }
    private Date getQuarterStatDate(boolean startFromYesterday){
        Calendar start = Calendar.getInstance();
        if(startFromYesterday == true){
            start.add(Calendar.DATE, -1);
        }
        int month = start.get(Calendar.MONTH) + 1;
        if (month % 3 == 2) {
            month = month - 1;
        } else if (month % 3 == 0) {
            month = month - 2;
        }
        start.set(Calendar.DATE, 1);
        start.set(Calendar.MONTH, month -1 );
        setTimeToZero(start);
        return start.getTime();
    }
    private Date calculateStartDateForWeek(int periodicalNumber, Date scheduleStartDate) {
        if(scheduleStartDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calculatePreviousWeekEndDate(null));
            cal.add(Calendar.DAY_OF_WEEK, 1);
            cal.add(Calendar.WEEK_OF_MONTH, (-1 * periodicalNumber));
            setTimeToZero(cal);
            return cal.getTime();
        }else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(scheduleStartDate);
            cal.add(Calendar.DAY_OF_WEEK, 1);
            cal.add(Calendar.WEEK_OF_MONTH, (-1 * periodicalNumber));
            setTimeToZero(cal);
            return cal.getTime();
        }
    }
    private Date calculatePreviousWeekEndDate(Date scheduleDate) {
        Calendar cal=Calendar.getInstance();
        if(scheduleDate == null){
            cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1));
            setTimeToEndOfDay(cal);
            return cal.getTime();
        } else {
            cal.setTime(scheduleDate);
            cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1));
            setTimeToEndOfDay(cal);
            return cal.getTime();
        }
    }
    public Date calculateMonthsStartDate(int periodicalNumber, Date scheduleDate){
        Calendar cal = Calendar.getInstance();
        if(scheduleDate == null) {
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.MONTH, ((-1 * (periodicalNumber))));
            setTimeToZero(cal);
            return cal.getTime();
        }else {
            cal.setTime(scheduleDate);
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.MONTH, ((-1 * (periodicalNumber))));
            setTimeToZero(cal);
            return cal.getTime();
        }
    }
    public Date calculateMonthEndDate(Date scheduleDate) {
        Calendar cal = Calendar.getInstance();
        if(scheduleDate == null) {
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndOfDay(cal);
            return cal.getTime();
        }else {
            cal.setTime(scheduleDate);
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndOfDay(cal);
            return cal.getTime();
        }
    }
    private Date calculateQuaterStartDate(int periodicalNumber, Date scheduleDate) {
        Calendar start = Calendar.getInstance();
        if(scheduleDate ==null) {
            int month = start.get(Calendar.MONTH) + 1;
            if (month % 3 == 2) {
                month = month - 1;
            } else if (month % 3 == 0) {
                month = month - 2;
            }
            start.set(Calendar.DATE, 1);
            start.set(Calendar.MONTH, month - 1);
            setTimeToZero(start);
            start.add(Calendar.MONTH, (-1 * (periodicalNumber) * 3));
            return start.getTime();
        } else {
            start.setTime(scheduleDate);
            int month = start.get(Calendar.MONTH) + 1;
            if (month % 3 == 2) {
                month = month - 1;
            } else if (month % 3 == 0) {
                month = month - 2;
            }
            start.set(Calendar.DATE, 1);
            start.set(Calendar.MONTH, month - 1);
            setTimeToZero(start);
            start.add(Calendar.MONTH, (-1 * (periodicalNumber) * 3));
            return start.getTime();
        }
    }
    private Date calculateQuaterEndDate(Date startDate, int periodicalNumber) {
        Calendar calEndDate = Calendar.getInstance();
        calEndDate.setTime(startDate);
        calEndDate.add(Calendar.MONTH, (periodicalNumber * 3) - 1);
        calEndDate.set(Calendar.DAY_OF_MONTH, calEndDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndOfDay(calEndDate);
        return calEndDate.getTime();
    }
}
