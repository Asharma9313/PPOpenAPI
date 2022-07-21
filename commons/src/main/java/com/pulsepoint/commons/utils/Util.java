package com.pulsepoint.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;

public class Util {
    public static Date convertToESTDate(String clientDate, java.util.TimeZone timeZone) throws ParseException {
        DateFormat dfEST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        dfEST.setTimeZone(TimeZone.getTimeZone("EST"));
        DateFormat dfClient = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        dfClient.setTimeZone(timeZone);
        return dfEST.parse(dfEST.format(dfClient.parse(clientDate)));
    }
    public static String convertToJSON(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        return mapper.writeValueAsString(obj);
    }
    public static Date setDateToEndOfDay(Date date) {
        Date endOfDay = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.HOUR);

        endOfDay = org.apache.commons.lang3.time.DateUtils.addHours(endOfDay, 23);
        endOfDay = org.apache.commons.lang3.time.DateUtils.addMinutes(endOfDay, 59);
        endOfDay = org.apache.commons.lang3.time.DateUtils.addSeconds(endOfDay, 59);

        return endOfDay;
    }
    public static Date setDateToStartOfDay(Date date) {
        Date endOfDay = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.HOUR);

        endOfDay = org.apache.commons.lang3.time.DateUtils.addHours(endOfDay, 00);
        endOfDay = org.apache.commons.lang3.time.DateUtils.addMinutes(endOfDay, 00);
        endOfDay = org.apache.commons.lang3.time.DateUtils.addSeconds(endOfDay, 00);

        return endOfDay;
    }
}
