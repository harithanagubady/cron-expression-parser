package com.cronparser.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CronField {

    private static final String[] MONTHS = new String[]
            {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private static final String[] DAYS = new String[]
            {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private String[] choices;
    private String fieldName;
    private Type type;
    private int length;
    private int maxAllowedValue;
    private int minAllowedValue;

    private String expression;

    private List<Integer> possibilities;

    public int getMaxAllowedValue() {
        return maxAllowedValue;
    }

    public void setMaxAllowedValue(int maxAllowedValue) {
        this.maxAllowedValue = maxAllowedValue;
    }

    public int getMinAllowedValue() {
        return minAllowedValue;
    }

    public void setMinAllowedValue(int minAllowedValue) {
        this.minAllowedValue = minAllowedValue;
    }

    protected CronField(Type type) {
        this.type = type;
        switch (type) {
            case SECOND, MINUTE -> {
                this.fieldName = this.type.toString().toLowerCase();
                this.length = 60;
                this.maxAllowedValue = 59;
                this.minAllowedValue = 0;
            }
            case HOUR -> {
                this.fieldName = this.type.toString().toLowerCase();
                this.length = 24;
                this.maxAllowedValue = 23;
                this.minAllowedValue = 0;
            }
            case DAY_OF_MONTH -> {
                this.fieldName = this.type.toString().toLowerCase();
                this.length = 31;
                this.maxAllowedValue = 31;
                this.minAllowedValue = 1;
            }
            case MONTH -> {
                this.fieldName = this.type.toString().toLowerCase();
                this.choices = CronField.MONTHS;
                this.length = 12;
                this.maxAllowedValue = 12;
                this.minAllowedValue = 1;
            }
            case DAY_OF_WEEK -> {
                this.fieldName = "day of week";
                this.choices = CronField.DAYS;
                this.length = 7;
                this.maxAllowedValue = 6;
                this.minAllowedValue = 0;
            }
        }
    }

    public List<Integer> parse(String value) {

        if (value.equals("*")) {
            return parseAll(value);
        }
        if (value.matches("^[0-9]+$")){
            return parseLiteral(value);
        }
        if (value.matches(".*,.*")) {
            return parseValues(value);
        }
        if (value.matches(".*\\/.*")) {
            return parseIncrements(value);
        }
        if (value.matches("[0-9]+-[0-9]+")) {
            return parseRange(value);
        }

        throw new RuntimeException("Invalid field value : " + value);
    }

    private List<Integer> parseLiteral(String value) {
        return List.of(Integer.parseInt(value));
    }


    private List<Integer> parseAll(String value) {
        return Arrays.stream(IntStream.range(this.getMinAllowedValue(), this.getMaxAllowedValue() + 1).toArray()).boxed()
                .collect(Collectors.toList());
    }

    private List<Integer> parseValues(String value) {
        String[] lists = value.split(",");

        if (lists.length != 2) {
            throw new RuntimeException(
                    "List does not have valid expression : " + value);
        }
        List<Integer> list = new ArrayList<>();
        for (String l : lists) {
            int i;
            try {
                i = Integer.parseInt(l);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid expression : " + value);
            }
            if (i >= minAllowedValue && i <= maxAllowedValue) {
                list.add(i);
            } else {
                throw new RuntimeException("Invalid number");
            }
        }
        return list;
    }

    private List<Integer> parseIncrements(String value) {
        String[] steps = value.split("/");

        if (steps.length != 2) {
            throw new RuntimeException(
                    "Step does not have valid expression : " + value);
        }

        if (steps[0].equals("*")) {
            steps[0] = String.valueOf(this.minAllowedValue);
        }

        Integer[] stepSegments;
        try {
            stepSegments = new Integer[]{Integer.valueOf(steps[0]), Integer.valueOf(steps[1])};
        } catch (Exception e) {
            throw new RuntimeException("Step does not have valid expression : " + value);
        }

        if (stepSegments[1] > this.maxAllowedValue) {
            throw new RuntimeException("Step size is more than maximum value");
        }

        if (stepSegments[0] > this.maxAllowedValue) {
            throw new RuntimeException("Step start is more than maximum value");
        }

        return IntStream.iterate(stepSegments[0], n -> n + stepSegments[1])
                .limit((this.maxAllowedValue - stepSegments[0])/ stepSegments[1] + 1).boxed().collect(Collectors.toList());
    }

    private List<Integer> parseRange(String value) {
        List<Integer> rangeLimits = Stream.of(value.split("-")).map(Integer::valueOf).toList();

        if (rangeLimits.size() != 2) {
            throw new RuntimeException(
                    "Invalid expression : " + value);
        }

        if (rangeLimits.get(1) < rangeLimits.get(0)) {
            throw new RuntimeException(
                    "Range is from maximum to minimum which is invalid, given minimum : " + rangeLimits.get(0)
                            + " and maximum : " + rangeLimits.get(1));
        }

        if (rangeLimits.get(0) < this.minAllowedValue) {
            throw new RuntimeException(
                    "Range minimum is not valid. Min given : " + rangeLimits.get(0) + " Min allowed : "
                            + this.minAllowedValue);
        }

        if (rangeLimits.get(0) > this.maxAllowedValue) {
            throw new RuntimeException(
                    "Range minimum is not valid. Max given : " + rangeLimits.get(0) + " Max allowed : "
                            + this.maxAllowedValue);
        }

        if (rangeLimits.get(1) > this.maxAllowedValue) {
            throw new RuntimeException(
                    "Range maximum is not valid. Max given : " + rangeLimits.get(1) + " Max allowed : "
                            + this.maxAllowedValue);
        }

        return Arrays.stream(IntStream.range(rangeLimits.get(0), rangeLimits.get(1) + 1).toArray())
                .boxed().collect(Collectors.toList());
    }
    protected enum Type {

        SECOND,
        MINUTE,
        HOUR,
        DAY_OF_MONTH,
        MONTH,
        DAY_OF_WEEK;

    }
}
