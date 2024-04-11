package org.quantcast.common;

public class DailyCookiesConstants {


     public static final String VALID_DATE = "VALID_DATE";

    public static final String VALID_UTC = "VALID_UTC";

    public static final String INVALID_DATE = "INVALID_DATE";

    public static final String NONE = "NONE";

    public static final String EMPTY = "";


    public static final String LOG_DIR_PATH = "${log.dir.path}";

    public static final String COMPARE_DATE_FORMAT = "${compare-date-format}";

    public static final String VALID_DATE_PATTERN  = "^(\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?(([+-]\\d{2}:\\d{2})|(Z)))?)$";

    public static final String UTC_DATE_PATTERN  =  "\\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2]\\d|3[0-1])T(?:[0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:\\.\\d+|)(?:Z|(?:\\+|\\-)(?:\\d{2}):?(?:\\d{2}))";

    public static final String DATE_ONLY_PATTERN = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public static final String FILE_NOT_FOUND_ERROR = "File not found while reading CSV: ";

    public static final String IO_EXCEPTION_ERROR = "IO exception occurred while reading CSV: ";

    public static final String SECURITY_EXCEPTION_ERROR = "Security exception occurred while reading CSV: ";

    public static final String UNEXPECTED_EXCEPTION_ERROR = "Unexpected exception occurred while reading CSV: ";

    public static final String MISSING_INPUT_ERROR_MESSAGE = "Filename or Date is missing. Please provide both filename and date.";

    public static final String INVALID_DATE_ERROR_MESSAGE = "The provided date is invalid or in an incorrect format.";

    public static final String COOKIE_FINDING_START_MESSAGE = "Finding Cookies of the Day begins :-";

    public static final String MOST_ACTIVE_COOKIES_INFO = "Most Active Cookies and their occurrences";

     public static final String NO_COOKIES_FOUND = "No Cookies found.";

     public static final String NO_MOST_ACTIVE_COOKIES_FOUND = "No Most active Cookie(s) found.";

}
