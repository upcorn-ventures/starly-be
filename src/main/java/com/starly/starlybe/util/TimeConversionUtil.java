package com.starly.starlybe.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeConversionUtil {

    public static List<String> convertToServerLocalTimes(List<String> userLocalTimes, String userTimeZone) {
        List<String> serverLocalTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (String time : userLocalTimes) {
            LocalTime localTime = LocalTime.parse(time, formatter);
            ZonedDateTime userZoned = ZonedDateTime.of(LocalDate.now(), localTime, ZoneId.of(userTimeZone));
            ZonedDateTime serverZoned = userZoned.withZoneSameInstant(ZoneOffset.UTC);
            serverLocalTimes.add(serverZoned.toLocalTime().format(formatter));
        }
        return serverLocalTimes;
    }
}
