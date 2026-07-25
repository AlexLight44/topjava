package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.annotation.Nullable;
import java.time.LocalTime;

public class SpringToLocalTimeConverter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(@Nullable String source) {
        return DateTimeUtil.parseLocalTime(source);
    }
}
