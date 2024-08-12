package todo.list.api.App.domain.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataService {
    private static final DateTimeFormatter parser = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime parse(String data) {
        return LocalDateTime.parse(data, parser);
    }

    public static String toString(LocalDateTime data) {
        return data.format(formatter);
    }

    public String somaDiasADataInformada(LocalDateTime dataInformada, Long quantidadeDeDias) {
        return DataService.toString(dataInformada.plusDays(quantidadeDeDias));
    }
}
