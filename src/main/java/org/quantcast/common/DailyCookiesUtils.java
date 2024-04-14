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


    /**
     * Compares two dates based on the provided date type.
     *
     * @param queryDate      The date inputted from Console
     * @param cookieDate     The date from the cookie log file.
     * @param dateType       A flag indicating the type of date (VALID_DATE OR VALID_UTC) comparison to perform.
     * @param compareDateFmt The format in which the dates should be compared.
     * @return               {@code true} if the dates match based on the comparison criteria, {@code false} otherwise.
     */

    public static boolean compareDates(String queryDate, String cookieDate, String dateType, String compareDateFmt) {
         return  dateType.equals(DailyCookiesConstants.VALID_DATE)
                 ? queryDate.equals(formatDate(cookieDate, compareDateFmt))
                 : formatDate(queryDate, compareDateFmt).equals(formatDate(cookieDate, compareDateFmt));
    }


    /**
     * Compares two dates based on the provided date type.
     *
     * @param date      The date string to be formatted.
     * @param dateFormat The format in which the dates should be compared.
     * @return       The formatted date string.
     */
    public static String formatDate(String date, String dateFormat) {
          return OffsetDateTime.parse(date).format(DateTimeFormatter.ofPattern(dateFormat));
    }


    /**
     * Extracts data corresponding to the specified command line parameter from the given list of command line arguments.
     * If the parameter is found and has a valid value following it, that value is returned. Otherwise, returns a default value.
     *
     * @param parameter   The command line parameter (-f / -d) to search for.
     * @param cmdArgs     The list of command line arguments.
     * @return            The value corresponding to the specified parameter, or a default value (NONE) if not found
     */
    public String commandLineDataExtractor(String parameter, List<String> cmdArgs) {
        return cmdArgs.indexOf(parameter) >= 0
                && (cmdArgs.indexOf(parameter) + 1) < cmdArgs.size()
                && !cmdArgs.get(cmdArgs.indexOf(parameter) + 1).equals(DailyCookiesConstants.EMPTY)
                ? cmdArgs.get(cmdArgs.indexOf(parameter) + 1)
                : DailyCookiesConstants.NONE;
    }


    /**
     * Checks the type and validity of the input date string.
     *
     * @param date  The date string to check.
     * @return      A string indicating the type and validity of the date: "VALID_DATE" if it's a valid date,
     *              "VALID_UTC" if it's a valid UTC date, or "INVALID_DATE" if it's invalid.
     */

    public String dateTypeAndValidityChecker(String date){
        boolean validDate;
        boolean dateOnly;
        boolean UTCOnly;
        validDate = patternMatcher(DailyCookiesConstants.VALID_DATE_PATTERN,date);
        dateOnly = patternMatcher(DailyCookiesConstants.DATE_ONLY_PATTERN,date);
        UTCOnly = patternMatcher(DailyCookiesConstants.UTC_DATE_PATTERN,date);

        return validDate == true && dateOnly ==true
                ? DailyCookiesConstants.VALID_DATE
                :  validDate == true && UTCOnly ==true
                ? DailyCookiesConstants.VALID_UTC : DailyCookiesConstants.INVALID_DATE;
    }


    /**
     * Performs pattern matching for the given pattern and input string.
     *
     * @param pattern   The regular expression pattern to match against.
     * @param date      The input string to match against the pattern.
     * @return          {@code true} if the input string matches the pattern, {@code false} otherwise.
     */
    private boolean patternMatcher(String pattern, String date){

        Pattern pat = Pattern.compile(pattern);
        Matcher matcher = pat.matcher(date);
          return matcher.matches();
    }

    /**
     * PPredicate to filter String arrays based on length and non-empty elements.
     *
     * @param part The string[] contains cookie and date

     * @return   {@code true} if the input string[] having both Cookie and date, {@code false} otherwise.
     */
    public Predicate<String[]> matchFilter = part -> part.length == 2
            && !StringUtils.isEmpty(part[0]) && !StringUtils.isEmpty(part[1]);

}
