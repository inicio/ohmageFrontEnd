package edu.ucla.cens.mobilize.client.utils;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * Contains various Date based helper utilities.
 * 
 * @author jhicks
 *
 */
public class DateUtils {
    // Format of the timestamp string returned from the server
    //public final static String timeStampFormat = "yyyy-MM-dd HH:mm:ss.S"; // old format
    public final static String timeStampFormat = "yyyy-MM-dd HH:mm:ss"; // as of May 2011
    // Format of the timestamp string to upload to server POST api
    public final static String uploadTimeStampFormat = "yyyy-MM-dd";
    public final static String apiRequestFormat = "yyyy-MM-dd";
    public final static String displayFormat = "MMM dd, yyyy";
    public final static String historyTokenFormat = "yyyy-MM-dd";
    
    /**
     * Translates a String from the server time format to a Date.
     * 
     * @param toTranslate The Date to translate.
     * @return The translated Date.
     */
    public static Date translateFromServerFormat(String toTranslate) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(timeStampFormat);
        return dateFormat.parse(toTranslate);
    }
    
    public static String translateToServerUploadFormat(Date toTranslate) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(uploadTimeStampFormat);
        return dateFormat.format(toTranslate);
    }
    
    /**
     * @param toTranslate Date that should be translated to server api format
     * @return Date in format expected by Ohmage api 
     */
    public static String translateToApiRequestFormat(Date toTranslate) {
      DateTimeFormat dateFormat = DateTimeFormat.getFormat(apiRequestFormat);
      return dateFormat.format(toTranslate);
    }

    /**
     * Converts history token style date param into Date object
     * @param toTranslate String parsed from history token
     * @return Date
     */
    public static Date translateFromHistoryTokenFormat(String toTranslate) {
      DateTimeFormat dateFormat = DateTimeFormat.getFormat(historyTokenFormat);
      return dateFormat.parse(toTranslate);
    }
    
    /**
     * Converts date to history token param so date can be encoded in url
     * @param toTranslate Date that should be translated to url params
     * @return Url param string that can be used as a history token
     */
    public static String translateToHistoryTokenFormat(Date toTranslate) {
      DateTimeFormat dateFormat = DateTimeFormat.getFormat(historyTokenFormat);
      return dateFormat.format(toTranslate);
    }
    
    /**
     * @return Use anywhere in the app that a date is displayed in a date box
     */
    public static DateTimeFormat getDateBoxDisplayFormat() {
      return DateTimeFormat.getFormat(displayFormat);
    }
    
    /**
     * @return Default date format to use when displaying a date in a data table
     */
    public static DateTimeFormat getTableDisplayFormat() {
      return DateTimeFormat.getFormat(displayFormat);
    }
    
    /**
     * Useful for adding one to the end date of an api query to make the date
     * range inclusive instead of exclusive (since mysql only returns values
     * up to midnight of the end date)
     * @param originalDate
     * @return day after originalDate
     */
    public static Date addOneDay(Date originalDate) {
      if (originalDate == null) return null;
      long secs = originalDate.getTime();
      secs = secs + (24 * 60 * 60 * 1000);
      return new Date(secs);
    }
    
    /**
     *  Checks to see if a Date is within a given Day.  Uses deprecated Date functionality,
     *  but GWT does not yet include the replacement functionality in Calendar.
     * 
     * @param date The date to check.
     * @param day The day that the date should be in.
     * @return True/false.
     */
    @SuppressWarnings("deprecation")
    public static boolean isDateInDay(Date date, Date day) {
        return (date.getYear() == day.getYear() &&
                date.getMonth() == day.getMonth() &&
                date.getDate() == day.getDate());
    }
    
    /**
     * Checks to see if a Date is within a given month.  Uses deprecated Date functionality,
     * but GWT does not yet include the replacement functionality in Calendar.
     * 
     * @param date The Date to check.
     * @param month The month the date should be in.
     * @return True/false.
     */
    @SuppressWarnings("deprecation")
    public static boolean isDateInMonth(Date date, Date month) {
        return (date.getYear() == month.getYear() &&
                date.getMonth() == month.getMonth());
    }
}