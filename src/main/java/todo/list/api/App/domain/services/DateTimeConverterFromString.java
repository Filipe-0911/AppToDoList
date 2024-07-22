package todo.list.api.App.domain.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverterFromString {
    private static final DateTimeFormatter parser = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime parse(String data) {
        return LocalDateTime.parse(data, parser);
    }

    public static String toString(LocalDateTime data) {
        return data.format(formatter);
    }
}
