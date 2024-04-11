package org.quantcast;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.exceptions.CommonExceptions;
import org.quantcast.common.DailyCookiesUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
public class TopCookiesOfDayFinderApplication implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(TopCookiesOfDayFinderApplication.class);

    private final DailyCookiesUtils dailyCookiesUtils;
    private final CookieFinderService cookieFinderService;

    public static void main(String[] args) {

        SpringApplication.run(TopCookiesOfDayFinderApplication.class, args);

    }

    @Override
    public void run(ApplicationArguments args)  throws Exception{
        logger.info(DailyCookiesConstants.COOKIE_FINDING_START_MESSAGE);
        Map<String,Integer> result = cookieFinderService.getTopActiveCookies(args);
        if(!result.isEmpty()) {
            System.out.println(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
            logger.info(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
        }
        result.entrySet().stream().forEach(cookies -> System.out.println(cookies.getKey() +" : "+cookies.getValue()));

    }


}
