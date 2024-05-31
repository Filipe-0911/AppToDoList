package todo.list.api.App.domain.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverterFromString {
    private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    public static LocalDateTime parse (String data) {
        return LocalDate.parse(data, parser).atStartOfDay();
    }
}
