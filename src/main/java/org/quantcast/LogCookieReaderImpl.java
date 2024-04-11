package org.quantcast;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LogCookieReaderImpl implements CookieDateReader {

    private static final Logger logger = LogManager.getLogger(CookieFinderService.class);
    @Value(DailyCookiesConstants.LOG_DIR_PATH)
    String filePath;

    @Value(DailyCookiesConstants.COMPARE_DATE_FORMAT)
    String compareDateFmt;

    private final DailyCookiesUtils dailyCookiesUtils;

    @Override
    public List<String> readCookies(String fileName, String date, String dateType) {

        String logFilePath = filePath+fileName;
        List<String> filteredCookiesList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            Stream<String> lines = reader.lines();
            filteredCookiesList = lines.map(line -> line.split("\\,"))
                    .filter(part -> dailyCookiesUtils.matchFilter.test(part)
                            && DailyCookiesUtils.compareDates(date, part[1],dateType,compareDateFmt))
                    .map(part -> part[0])
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            logger.error(DailyCookiesConstants.FILE_NOT_FOUND_ERROR + logFilePath, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(DailyCookiesConstants.IO_EXCEPTION_ERROR + logFilePath, e);
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            logger.error(DailyCookiesConstants.SECURITY_EXCEPTION_ERROR + logFilePath, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error(DailyCookiesConstants.UNEXPECTED_EXCEPTION_ERROR + logFilePath, e);
            throw new RuntimeException(e);
        }
        return filteredCookiesList;
    }
}
