import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CronParser {

    List<String> outputFields = List.of("minute", "hour", "day of month", "month", "day of week", "command");
     Map<String, String> displayString = Map
            .of("day", "day of month", "weekday", "day of week");
    private final static CronField MINUTES_FIELD_PARSER = new CronField(CronField.Type.MINUTE);
    private final static CronField HOURS_FIELD_PARSER = new CronField(CronField.Type.HOUR);
    private final static CronField DAYS_FIELD_PARSER = new CronField(CronField.Type.DAY_OF_MONTH);
    private final static CronField MONTHS_FIELD_PARSER = new CronField(CronField.Type.MONTH);
    private final static CronField DAY_OF_WEEK_FIELD_PARSER = new CronField(CronField.Type.DAY_OF_WEEK);

    private String expression;
    private String command;
    HashMap<String, List<Integer>> parsedMap;
    public CronParser(String arg) {
        this.expression = arg;
        this.parsedMap = new HashMap<>();
    }

    public CronParser parse() throws EmptyExpressionException {
        if (null == expression) {
            throw new EmptyExpressionException("expression is empty");
        }
        String[] values = this.expression.split("\\s+");
        if (values.length < 6) {
            throw new EmptyExpressionException("Invalid Expression");
        }
        int i = 0;
        this.parsedMap.put(outputFields.get(i), CronParser.MINUTES_FIELD_PARSER.parse(values[i++]));
        this.parsedMap.put(outputFields.get(i), CronParser.HOURS_FIELD_PARSER.parse(values[i++]));
        this.parsedMap.put(outputFields.get(i), CronParser.DAYS_FIELD_PARSER.parse(values[i++]));
        this.parsedMap.put(outputFields.get(i), CronParser.MONTHS_FIELD_PARSER.parse(values[i++]));
        this.parsedMap.put(outputFields.get(i), CronParser.DAY_OF_WEEK_FIELD_PARSER.parse(values[i++]));

        this.command = values[i] + extractArguments(values, i + 1);
        return this;
    }


    private String extractArguments(String[] segments, int startingIndex) {
        StringBuilder arguments = new StringBuilder();
        for (int i = startingIndex; i < segments.length; i++) {
            arguments.append(" ").append(segments[i]);
        }
        return arguments.toString();
    }

    public String getElements() {
        StringBuilder sb = new StringBuilder();
        for (String section : outputFields) {
            if (this.parsedMap.get(section) == null) {
                continue;
            }
            String displayString = this.displayString.getOrDefault(section, section);
            sb.append(String.format(displayString + " ".repeat(14 - displayString.length()) + "%s",
                    this.parsedMap.get(section).stream().map(Object::toString).collect(
                            Collectors.joining(" "))));
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(String.format("command       %s", this.command));
        return sb.toString();
    }
}
