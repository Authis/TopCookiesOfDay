package org.quantcast;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TopCookiesOfDayFinderApplication.class,args = {"-f","cookie_log.csv", "-d","2018-12-09"})
class TopCookiesOfDayFinderApplicationTest {


    @Test
    public void contextLoads() {

    }


}