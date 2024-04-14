package org.quantcast;


import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Test;
import org.quantcast.exceptions.CommonExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TopCookiesOfDayFinderApplication.class,args = {"-f","cookie_log.csv", "-d","2018-12-09"})
@TestPropertySource(locations = "classpath:application.properties")

class CookieFinderServiceTest {

    @Autowired
     private  CookieFinderService cookieFinderService ;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();


        @Test

        void getTopActiveCookies_With_Valid_FileName_And_Valid_Date_WithMultipleResult() throws Exception {

            String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-09" };
            Map<String,Integer> expectedResult = new HashMap<>();
            expectedResult.put("AtY0laUfhglK3lC7", 2);
            expectedResult.put("SAZuXPGUrfbcn5UA", 2);
            Map<String,Integer> result  = cookieFinderService.getTopActiveCookies( commandLineArgs );
            assertEquals(result,expectedResult);

        }


        @Test
        void getTopActiveCookies_With_Valid_FileName_And_Valid_DateWithSingleResult() throws Exception {
            String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-08" };
            Map<String,Integer> expectedResult = new HashMap<>();
            expectedResult.put("4sMM2LxV07bPJzwf", 2);

            Map<String,Integer> result  = cookieFinderService.getTopActiveCookies( commandLineArgs );
            assertEquals(result,expectedResult);

        }


    @Test
    void getTopActiveCookies_With_Valid_FileName_And_Valid_UTCDateWithSingleResult() throws Exception {
        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-09T14:19:00+00:00" };
        Map<String,Integer> expectedResult = new HashMap<>();
        expectedResult.put("AtY0laUfhglK3lC7", 2);
        expectedResult.put("SAZuXPGUrfbcn5UA", 2);

        Map<String,Integer> result  = cookieFinderService.getTopActiveCookies( commandLineArgs );
        assertEquals(result,expectedResult);

    }
    @Test
    void getTopActiveCookies_With_Valid_FileName_And_Valid_UTCDateWithNOCookiesFound() throws Exception {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-12T14:19:00+00:00" };
        Map<String,Integer> expectedResult = new HashMap<>();
        Map<String,Integer> result  = cookieFinderService.getTopActiveCookies( commandLineArgs );
        assertEquals(result,expectedResult);

        System.setOut(System.out);
        String output = outContent.toString();
        assertTrue(output.contains("No Cookies found."));

    }

    @Test
    void getTopActiveCookies_With_Valid_FileName_And_Valid_UTCDateWithNO_MOST_ACTIVE_COOKIES_FOUND() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-07T14:19:00+00:00" };
        Map<String,Integer> expectedResult = new HashMap<>();
        Map<String,Integer> result  = cookieFinderService.getTopActiveCookies( commandLineArgs );
        assertEquals(result,expectedResult);


        System.setOut(System.out);
        String output = outContent.toString();
        assertTrue(output.contains("No Most active Cookie(s) found."));
    }

    @Test
    void getTopActiveCookies_With_Valid_FileName_And_IN_VALID_DATE() throws Exception {
        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-32-07T14:19:00+00:00" };

        CommonExceptions.InvalidDateException exception = assertThrows(CommonExceptions.InvalidDateException.class, () -> {
            cookieFinderService.getFileNameDateType(commandLineArgs);
        });
        assertEquals("The provided date is invalid or not in the correct format.", exception.getMessage());
    }

    @Test
    void getTopActiveCookies_With_MIssingDate() throws Exception {
        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","" };

        CommonExceptions.MissingInputException  exception = assertThrows(CommonExceptions.MissingInputException .class, () -> {
            cookieFinderService.getFileNameDateType(commandLineArgs);
        });
        assertEquals("Filename or Date is missing. Please provide both filename and date.", exception.getMessage());
    }

    @Test
    void getTopActiveCookies_With_MIssingFileName() throws Exception {
        String[] commandLineArgs = { "-f","", "-d","2018-32-07T14:19:00+00:00" };

        CommonExceptions.MissingInputException  exception = assertThrows(CommonExceptions.MissingInputException .class, () -> {
            cookieFinderService.getFileNameDateType(commandLineArgs);
        });
        assertEquals("Filename or Date is missing. Please provide both filename and date.", exception.getMessage());
    }


    @Test
    void getFileNameDateType_WithValidFileName_And_ValidDate() throws Exception {

        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-09" };

        Map<String,String> result = cookieFinderService.getFileNameDateType(commandLineArgs);

        Map<String,String> expectedResult = new HashMap<>();
        expectedResult.put("fileName", "cookie_log.csv");
        expectedResult.put("date", "2018-12-09");
        expectedResult.put("dateType", "VALID_DATE");
        assertEquals(result,expectedResult);

    }

        @Test
        void getFileNameDateType_WithValidFileName_And_ValidUTCDate() throws Exception {

            String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-07T14:19:00+00:00" };

            Map<String,String> result = cookieFinderService.getFileNameDateType(commandLineArgs);

            Map<String,String> expectedResult = new HashMap<>();
            expectedResult.put("fileName", "cookie_log.csv");
            expectedResult.put("date", "2018-12-07T14:19:00+00:00");
            expectedResult.put("dateType", "VALID_UTC");
            assertEquals(result,expectedResult);

        }

    @Test
    void getFileNameDateType_WithValidFileName_And_IN_ValidDate() throws Exception {

        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-77" };

        CommonExceptions.InvalidDateException exception = assertThrows(CommonExceptions.InvalidDateException.class, () -> {
            cookieFinderService.getFileNameDateType(commandLineArgs);
        });

        assertEquals("The provided date is invalid or not in the correct format.", exception.getMessage());

    }
    @Test
    void getFileNameDateType_WithValidFileName_And_IN_ValidUTCDate() throws Exception {

        String[] commandLineArgs = { "-f","cookie_log.csv", "-d","2018-12-907T14:19:00+00:00" };

        CommonExceptions.InvalidDateException exception = assertThrows(CommonExceptions.InvalidDateException.class, () -> {
            cookieFinderService.getFileNameDateType(commandLineArgs);
        });
        assertEquals("The provided date is invalid or not in the correct format.", exception.getMessage());

    }


    @Test
    void findTheTopActiveCookies_HavingTopCookies() {
        List<String> cookiesOfTheDay = Arrays.asList( "AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA","AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA");

        Map<String, Integer> result = cookieFinderService.findTheTopActiveCookies(cookiesOfTheDay);
        Map<String,Integer> expectedResult = new HashMap<>();
        expectedResult.put("AtY0laUfhglK3lC7", 2);
        expectedResult.put("SAZuXPGUrfbcn5UA", 2);
        assertEquals(result,expectedResult );
    }
}