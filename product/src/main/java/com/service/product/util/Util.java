package com.service.product.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static String formatLocalDateTimeToStr(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return localDateTime != null ? localDateTime.format(formatter) : "";
    }
}
