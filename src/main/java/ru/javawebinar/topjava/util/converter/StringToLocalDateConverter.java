package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String source) {
        return DateTimeUtil.parseLocalDate(source);
    }
}
