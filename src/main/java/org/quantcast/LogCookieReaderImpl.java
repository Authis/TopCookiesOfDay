package org.quantcast;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.common.DailyCookiesConstants;
import org.quantcast.common.DailyCookiesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    /**
     * Reads and filters cookies from a log file based on provided criteria.
     *
     * @param fileName  The name of the log file to read cookies from.
     * @param date      The date to filter cookies for.
     * @param dateType  The type of date comparison to perform (e.g., "UTC or Normal DATE Only").
     * @return A list of cookies observed on the specified date, filtered according to the criteria.
     * @throws RuntimeException If an error occurs while reading or filtering cookies.
     */

    @Override
    public List<String> readCookies(String fileName, String date, String dateType) {

        String logFilePath = filePath+fileName;
        List<String> filteredCookiesList;
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
        }  catch (Exception e) {
            logger.error(DailyCookiesConstants.UNEXPECTED_EXCEPTION_ERROR + logFilePath, e);
            throw new RuntimeException(e);
        }
        return filteredCookiesList;
    }
}
