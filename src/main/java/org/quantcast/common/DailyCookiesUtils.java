package org.quantcast.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DailyCookiesUtils {




    public static boolean compareDates(String queryDate, String cookieDate, String dateType, String compareDateFmt) {
         return  dateType.equals(DailyCookiesConstants.VALID_DATE)
                 ? queryDate.equals(formatDate(cookieDate, compareDateFmt))
                 : formatDate(queryDate, compareDateFmt).equals(formatDate(cookieDate, compareDateFmt));
    }

    public static String formatDate(String date, String dateFormat) {
          return OffsetDateTime.parse(date).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public String commandLineDataExtractor(String parameter, List<String> cmdArgs) {
        return cmdArgs.indexOf(parameter) >= 0
                && (cmdArgs.indexOf(parameter) + 1) < cmdArgs.size()
                && !cmdArgs.get(cmdArgs.indexOf(parameter) + 1).equals(DailyCookiesConstants.EMPTY)
                ? cmdArgs.get(cmdArgs.indexOf(parameter) + 1)
                : DailyCookiesConstants.NONE;
    }


    public String dateTypeAndValidityChecker(String date){
        boolean validDate = false;
        boolean dateOnly = false;
        boolean UTCOnly = false;
        validDate = patternMatcher(DailyCookiesConstants.VALID_DATE_PATTERN,date);
        dateOnly = patternMatcher(DailyCookiesConstants.DATE_ONLY_PATTERN,date);
        UTCOnly = patternMatcher(DailyCookiesConstants.UTC_DATE_PATTERN,date);

        return validDate == true && dateOnly ==true
                ? DailyCookiesConstants.VALID_DATE
                :  validDate == true && UTCOnly ==true
                ? DailyCookiesConstants.VALID_UTC : DailyCookiesConstants.INVALID_DATE;
    }

    private boolean patternMatcher(String pattern, String date){

        Pattern pat = Pattern.compile(pattern);
        Matcher matcher = pat.matcher(date);
          return matcher.matches();
    }


    public Predicate<String[]> matchFilter = part -> part.length == 2
            && !StringUtils.isAllBlank(part[0],part[1]);

}
