package org.quantcast;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.quantcast.exceptions.CommonExceptions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CookieFinderService {

    private static final Logger logger = LogManager.getLogger(CookieFinderService.class);
    private final LogCookieReaderImpl logCookieReader;

    private final DailyCookiesUtils dailyCookiesUtils;


    /**
     * Retrieves the top active cookies for a given date based on the command line arguments.
     *
     * @param args Command line arguments containing file name and date.
     * @return A map containing the top active cookies and their occurrences.
     * @throws Exception If an error occurs while processing the cookies.
     */
    public Map<String, Integer> getTopActiveCookies(String [] args) throws Exception {
        Map<String, String> dataMap;
        dataMap = getFileNameDateType(args);

        List<String> cookiesOfTheDay = logCookieReader.readCookies(dataMap.get(DailyCookiesConstants.FILE_NAME),
                dataMap.get(DailyCookiesConstants.DATE),
                dataMap.get(DailyCookiesConstants.DATE_TYPE));
        Map<String, Integer> result;
        if (null != cookiesOfTheDay && cookiesOfTheDay.size() > 0) {
            result = findTheTopActiveCookies(cookiesOfTheDay);

             if(!result.isEmpty()){
                 return result;
             }else{
                 logger.info(DailyCookiesConstants.NO_MOST_ACTIVE_COOKIES_FOUND);
                 System.out.println(DailyCookiesConstants.NO_MOST_ACTIVE_COOKIES_FOUND);
             }

        } else {
            System.out.println(DailyCookiesConstants.NO_COOKIES_FOUND);
            logger.info(DailyCookiesConstants.NO_COOKIES_FOUND);
            return Collections.emptyMap();
        }
        return Collections.emptyMap();
    }

    /**
     * Extracts the file name, date, and date type from the command line arguments.
     *
     * @param args The command line arguments containing the file name and date.
     * @return A map containing the extracted file name, date, and date type.
     * @throws CommonExceptions.MissingInputException If the file name or date is missing in the command line arguments.
     * @throws CommonExceptions.InvalidDateException  If the provided date is invalid.
     * @throws Exception                              If an unexpected error occurs during extraction.
     */

    public Map<String, String> getFileNameDateType(String[] args) throws Exception {
        Map<String, String> dataMap = new HashMap<>();

        String fileName;
        String date;
        String dateType;
        List<String> cmdArgs = Arrays.asList(args); //args.getNonOptionArgs();

        fileName = dailyCookiesUtils.commandLineDataExtractor("-f", cmdArgs);
        date = dailyCookiesUtils.commandLineDataExtractor("-d", cmdArgs);
        dateType = dailyCookiesUtils.dateTypeAndValidityChecker(date);


        if (fileName.equals(DailyCookiesConstants.NONE) || date.equals(DailyCookiesConstants.NONE)) {
            logger.error(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
            throw new CommonExceptions.MissingInputException(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
        }

        if (dateType.equals(DailyCookiesConstants.INVALID_DATE)) {
            logger.error(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
            throw new CommonExceptions.InvalidDateException(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
        }
        dataMap.put(DailyCookiesConstants.FILE_NAME, fileName);
        dataMap.put(DailyCookiesConstants.DATE, date);
        dataMap.put(DailyCookiesConstants.DATE_TYPE, dateType);

        return dataMap;
    }

    /**
     * Finds the top active cookies from a list of cookies observed on a given day greater than 1.
     *
     * @param cookiesOfTheDay A list of cookies observed on a specific day.
     * @return A map containing the top active cookies and their counts which is greater than 1.
     */

    public static Map<String, Integer> findTheTopActiveCookies(List<String> cookiesOfTheDay) {
        Map<String, Integer> topCookies = new HashMap<>();
        for (String cookie : cookiesOfTheDay) {
            if (!topCookies.containsKey(cookie)) {
                topCookies.put(cookie, 1);
            } else {
                topCookies.put(cookie, topCookies.get(cookie) + 1);
            }
        }
        topCookies.entrySet().removeIf(entry -> entry.getValue() <= 1);


        return topCookies;
    }

}
