package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimingRule extends Stopwatch {

    private static final Logger log = LoggerFactory.getLogger(TimingRule.class);

    private static final Map<String, Long> timings = new LinkedHashMap<>();

    @Override
    protected void finished(long nanos, Description description) {
        String testName = description.getMethodName();
        long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        timings.put(testName, millis);
        log.info("Test '{}' finished in {} ms", testName, millis);
    }

    public static void printSummary() {
        StringBuilder stringBuilder = new StringBuilder("\n === TEST EXECUTION TIME SUMMARY === \n");
        timings.forEach((name, time) -> stringBuilder
                .append(name)
                .append(" - ")
                .append(time)
                .append(" ms\n"));
        stringBuilder.append("===============================");
        log.info(stringBuilder.toString());
    }
}
