package org.quantcast;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.quantcast.exceptions.CommonExceptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CookieFinderService {

    private static final Logger logger = LogManager.getLogger(CookieFinderService.class);
    private final LogCookieReaderImpl logCookieReader;

    private final DailyCookiesUtils dailyCookiesUtils;

    public Map<String, Integer> getTopActiveCookies(ApplicationArguments args) throws Exception {
        Map<String, String> dataMap;
        dataMap = getFileNameDateType(args);

        List<String> cookiesOfTheDay = logCookieReader.readCookies(dataMap.get("fileName"), dataMap.get("date"), dataMap.get("dateType"));
        Map<String, Integer> result;
        if (null != cookiesOfTheDay && cookiesOfTheDay.size() > 0) {
            result = findTheTopActiveCookies(cookiesOfTheDay);

//            Optional.ofNullable(result)
//                    .filter(resultElm -> !resultElm.isEmpty())
//                    .ifPresentOrElse(resultElm -> resultElm.entrySet()
//                            .forEach(CookieFinderService::printResults), () -> System.out.println(DailyCookiesConstants.NO_MOST_ACTIVE_COOKIES_FOUND));

             if(!result.isEmpty()){
                 return result;
             }else{
                 System.out.println(DailyCookiesConstants.NO_MOST_ACTIVE_COOKIES_FOUND);
             }

        } else {
            System.out.println(DailyCookiesConstants.NO_COOKIES_FOUND);
            logger.info(DailyCookiesConstants.NO_COOKIES_FOUND);
            return Collections.emptyMap();
        }
        return Collections.emptyMap();
    }


    public Map<String, String> getFileNameDateType(ApplicationArguments args) throws Exception {
        Map<String, String> dataMap = new HashMap<>();

        String fileName;
        String date;
        String dateType;
        List<String> cmdArgs = args.getNonOptionArgs();

        fileName = dailyCookiesUtils.commandLineDataExtractor("-f", cmdArgs);
        date = dailyCookiesUtils.commandLineDataExtractor("-d", cmdArgs);
        dateType = dailyCookiesUtils.dateTypeAndValidityChecker(date);
        dataMap.put("fileName", fileName);
        dataMap.put("date", date);
        dataMap.put("dateType", dateType);


        if (fileName.equals(DailyCookiesConstants.NONE) || date.equals(DailyCookiesConstants.NONE)) {
            logger.error(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
            throw new CommonExceptions.MissingInputException(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
        }

        if (dateType.equals(DailyCookiesConstants.INVALID_DATE)) {
            logger.error(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
            throw new CommonExceptions.InvalidDateException(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
        }

        return dataMap;
    }



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
