package org.quantcast;

import org.junit.jupiter.api.Test;
import org.quantcast.exceptions.CommonExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TopCookiesOfDayFinderApplication.class,args = {"-f","cookie_log.csv", "-d","2018-12-09"})
@TestPropertySource(locations = "classpath:application.properties")
class LogCookieReaderImplTest {

    @Autowired
    LogCookieReaderImpl logCookieReader;

    @Test
    void readCookiesWithCorrectFileNameDate_DateType() {

        List<String> expectedResult = Arrays.asList( "AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA","5UAVanZf6UtGyKVS","AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA");
        assertEquals(expectedResult,logCookieReader.readCookies("cookie_log.csv","2018-12-09","VALID_DATE"));

    }

    @Test
    void readCookiesWithCorrectFileNameDate_UTCDateType() {

        List<String> expectedResult = Arrays.asList( "AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA","5UAVanZf6UtGyKVS","AtY0laUfhglK3lC7","SAZuXPGUrfbcn5UA");
        assertEquals(expectedResult,logCookieReader.readCookies("cookie_log.csv","2018-12-09T14:19:00+00:00","VALID_UTC"));

    }

    @Test
    void readCookiesWithCorrectFileNameWrongDate_DateType() {

          assertEquals( new ArrayList<>(),logCookieReader.readCookies("cookie_log.csv","2018-12-21","VALID_DATE"));

    }

    @Test
    void readCookiesWithFileNotFoundExceptions() {


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logCookieReader.readCookies("cookie_log123.csv","2018-12-09T14:19:00+00:00","VALID_UTC");
        });
        assertTrue(exception.getMessage().contains("java.io.FileNotFoundException"));

    }


}