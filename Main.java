
public class Main {
    public static void main(String[] args) throws EmptyExpressionException {
        String expression = "*/15 0 1,15 * 1-5 /usr/bin/find";
        CronParser parser = new CronParser(expression);
        System.out.println(parser.parse().getElements());
    }
}
