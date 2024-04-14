package org.quantcast.common;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.quantcast.TopCookiesOfDayFinderApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TopCookiesOfDayFinderApplication.class,args = {"-f","cookie_log.csv", "-d","2018-12-09"})
class wd {

    @Autowired
    DailyCookiesUtils dailyCookiesUtils;



    @Test
    void compareDates_With_All_ValidData() {

        assertEquals(true, DailyCookiesUtils.compareDates("2018-12-09T14:19:00+00:00","2018-12-09T14:19:00+00:00","VALID_UTC","yyyy-MM-dd"));
    }


    @Test
    void testMatchFilterWithValidInput() {
        String input = "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00";
        String[] validInput = input.split("\\,");
        assertTrue(dailyCookiesUtils.matchFilter.test(validInput));
    }

    @Test
    void testMatchFilterWithMissingCookie() {
        String input = ",2018-12-09T14:19:00+00:00";
        String[] validInput = input.split("\\,");
        assertFalse(dailyCookiesUtils.matchFilter.test(validInput));
    }

    @Test
    void testMatchFilterWithMissingDate() {
        String input = "AtY0laUfhglK3lC7,";
        String[] validInput = input.split("\\,");
        assertFalse(dailyCookiesUtils.matchFilter.test(validInput));
    }

}