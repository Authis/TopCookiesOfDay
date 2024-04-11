package org.quantcast;

import java.util.List;


public interface CookieDateReader {

 List<String> readCookies(String fileName, String date, String dateType);
}
