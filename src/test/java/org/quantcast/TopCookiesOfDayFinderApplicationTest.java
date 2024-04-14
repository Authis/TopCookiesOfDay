package org.quantcast;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TopCookiesOfDayFinderApplication.class,args = {"-f","cookie_log.csv", "-d","2018-12-09"})
@TestPropertySource(locations = "classpath:application.properties")
class TopCookiesOfDayFinderApplicationTest {


    @Autowired
    TopCookiesOfDayFinderApplication topCookiesOfDayFinderApplication;

    @Test
    public void contextLoadsActualOutput() throws Exception {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] argsArray  = {"-f" ,"cookie_log.csv","-d","2018-12-09"};
        topCookiesOfDayFinderApplication.run(argsArray);
        System.setOut(System.out);
        String output = outContent.toString();
         assertTrue(output.contains("AtY0laUfhglK3lC7 : 2"));
         assertTrue(output.contains("SAZuXPGUrfbcn5UA : 2"));
    }

    @Test
    public void contextLoadsWrongOutput() throws Exception {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] argsArray  = {"-f" ,"cookie_log.csv","-d","2018-12-09"};
        topCookiesOfDayFinderApplication.run(argsArray);
        System.setOut(System.out);
        String output = outContent.toString();
        assertFalse(output.contains("AtY0laUfhglK3lC7 : 3"));
     }



}