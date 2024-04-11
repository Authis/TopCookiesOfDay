package org.quantcast;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookieFinderService {

    private static final Logger logger = LogManager.getLogger(CookieFinderService.class);
    private final LogCookieReaderImpl logCookieReader;

    private final DailyCookiesUtils dailyCookiesUtils;

    @Value("${display-date-format}")
    String displayDateFormat;

    public void getTopActiveCookies(String fileName, String date, String dateType) {
        List<String> cookiesOfTheDay = logCookieReader.readCookies(fileName, date, dateType);
        Map<String, Integer> result;
        if (null != cookiesOfTheDay && cookiesOfTheDay.size() > 0) {
            result = findTheTopActiveCookies(cookiesOfTheDay);
            System.out.println(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
            logger.info(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
            Optional.ofNullable(result)
                    .filter(resultElm -> !resultElm.isEmpty())
                    .ifPresentOrElse(resultElm -> resultElm.entrySet()
                            .forEach(elm -> printResults(elm, date)), () -> System.out.println(DailyCookiesConstants.NO_MOST_ACTIVE_COOKIES_FOUND));
        } else {
            System.out.println(DailyCookiesConstants.NO_COOKIES_FOUND);
            logger.info(DailyCookiesConstants.NO_COOKIES_FOUND);
        }
    }

    public void printResults(Map.Entry<String, Integer> elm, String date) {
        logger.info(" : " + elm.getKey() + " : " + elm.getValue());
        System.out.println(elm.getKey() + " : " + elm.getValue());
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
