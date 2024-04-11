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
    public void run(ApplicationArguments args) throws Exception {
        logger.info(DailyCookiesConstants.COOKIE_FINDING_START_MESSAGE);
        String fileName;
        String date;
        List<String> cmdArgs = args.getNonOptionArgs();

        fileName = dailyCookiesUtils.commandLineDataExtractor("-f",cmdArgs);
        date = dailyCookiesUtils.commandLineDataExtractor("-d",cmdArgs);

        String dateType = dailyCookiesUtils.dateTypeAndValidityChecker(date);

        if(fileName.equals(DailyCookiesConstants.NONE) || date.equals(DailyCookiesConstants.NONE)){
            logger.error(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
            throw new CommonExceptions.MissingInputException(DailyCookiesConstants.MISSING_INPUT_ERROR_MESSAGE);
        }

        if(dateType.equals(DailyCookiesConstants.INVALID_DATE)){
            logger.error(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
            throw new CommonExceptions.InvalidDateException(DailyCookiesConstants.INVALID_DATE_ERROR_MESSAGE);
        }
        cookieFinderService.getTopActiveCookies(fileName, date,dateType);
    }


}
