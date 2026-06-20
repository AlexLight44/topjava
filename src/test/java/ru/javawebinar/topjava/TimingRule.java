package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class TimingRule extends TestWatcher {

    private static final Logger log = LoggerFactory.getLogger(TimingRule.class);

    private static final Map<String, Long> timings = new LinkedHashMap<>();
    private long startTime;

    @Override
    protected void starting(Description description) {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void finished(Description description) {
        long duratin = System.currentTimeMillis() - startTime;
        String testName = description.getMethodName();

        timings.put(testName, duratin);
        log.info("Test '{}' finished in {} ms", testName, duratin);
    }

    public static void printSummary() {
        log.info("=== TEST EXECUTION TIME SUMMARY");
        timings.forEach((name, time) -> log.info("{} - {} ms", name, time));
        log.info("===============================");
    }
}
