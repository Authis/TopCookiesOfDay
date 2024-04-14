package org.quantcast;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
public class TopCookiesOfDayFinderApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(TopCookiesOfDayFinderApplication.class);

    private final DailyCookiesUtils dailyCookiesUtils;
    private final CookieFinderService cookieFinderService;

    public static void main(String[] args) {

        SpringApplication.run(TopCookiesOfDayFinderApplication.class, args);

    }

    /**
     * To find the top active cookies based on the provided command-line arguments.
     * It retrieves the  using the provided arguments, and prints the results to the console.
     * If a cookie found more than once then it will be the top active cookie,(If more than one cookie pass this criteria then print all of them)
     * If no active cookies are found, it logs a message indicating that no
     * active cookies were found.
     *
     * @param args The command-line arguments passed to the application.
     * @throws Exception If an error occurs during the execution of the application logic.
     */

    @Override
    public void run(String[] args)  throws Exception{
        logger.info(DailyCookiesConstants.COOKIE_FINDING_START_MESSAGE);
        Map<String,Integer> result = cookieFinderService.getTopActiveCookies(args);
        if(!result.isEmpty()) {
            System.out.println(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
            logger.info(DailyCookiesConstants.MOST_ACTIVE_COOKIES_INFO);
        }
        result.entrySet().stream().forEach(cookies -> System.out.println(cookies.getKey() +" : "+cookies.getValue()));

    }


}
