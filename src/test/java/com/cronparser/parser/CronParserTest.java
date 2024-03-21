package com.cronparser.parser;

import com.cronparser.exception.EmptyExpressionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CronParserTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    HashMap<String, List<Integer>> expectedMap = new HashMap<>();

    @Test
    public void testCronExpression() throws EmptyExpressionException {
        expectedMap.put("minute", List.of(0, 15, 30, 45));
        expectedMap.put("hour", List.of(0));
        expectedMap.put("day of month", List.of(1, 15));
        expectedMap.put("month", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        expectedMap.put("day of week", List.of(1, 2, 3, 4, 5));
        assertEquals(new CronParser("*/15 0 1,15 * 1-5 /usr/bin/find").parse().parsedMap,
                expectedMap);
    }

    @Test(expected = RuntimeException.class)
    public void testCronExpressionThrowsException() throws EmptyExpressionException {
        expectedMap.put("minute", List.of(0, 15, 30, 45));
        expectedMap.put("hour", List.of(0));
        expectedMap.put("day of month", List.of(1, 15));
        expectedMap.put("month", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        expectedMap.put("day of week", List.of(1, 2, 3, 4, 5));
        new CronParser("*/15 0 1,15 * 5-1 /usr/bin/find").parse().getElements();
    }
}
