package org.quantcast.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class DailyCookiesUtilsTest {

    @Autowired
    DailyCookiesUtils dailyCookiesUtils;

    @Test
    void compareDates_With_All_ValidData() {

        assertEquals(true, DailyCookiesUtils.compareDates("2018-12-09T14:19:00+00:00","2018-12-09T14:19:00+00:00","VALID_UTC","yyyy-MM-dd"));
    }

    @Test
    void compareDates_With_Unmatch_Date() {

     }

    @Test
    void formatDate() {
    }

    @Test
    void commandLineDataExtractor() {
    }

    @Test
    void dateTypeAndValidityChecker() {
    }
}