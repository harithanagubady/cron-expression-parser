# cron-expression-parser
cron-expression-parser is an application which has the utility to parse the cron expression to show the ideal range of 
values of each field in the expression.

**Run Program:**

Can directly run the Main.java file by hard coding the input expression in the main method.

Java 17 version is expected as we used enhanced switch case.


**GuideLines:**

You should only consider the standard cron format with five time fields (minute, hour, day of
month, month, and day of week) plus a command, and you do not need to handle the special
time strings such as "@yearly". The input will be on a single line.
The cron string will be passed to your application as a single argument.
~$ your-program ＂*/15 0 1,15 * 1-5 /usr/bin/find＂
The output should be formatted as a table with the field name taking the first 14 columns and
the times as a space-separated list following it.
For example, the following input argument:

***/15 0 1,15 * 1-5 /usr/bin/find**

Should yield the following output:

**minute 0 15 30 45**

**hour 0**

**day of month 1 15**

**month 1 2 3 4 5 6 7 8 9 10 11 12**

**day of week 1 2 3 4 5**

**command /usr/bin/find**



**Cron Expression Theory:**

Cron Expression format:

 <**minute**> <**hour**> <**day-of-month**> <**month**> <**day-of-week**> <**command>**

Eg: Following script runs every minute

<p>* * * * * /usr/local/ispconfig/server/server.sh</p>

Special Characters in the expression:
1. "*" (all) - event should happen every time unit, eg: "*" in <minute> field means "for every minute"
2. "?" (any) - utilized in <day-of-month> and <day-of-week> fields, eg: for 5th of every month, <day-of-week> field will be any - "?"
3. "-" (range) - eg: "10-11" in <hour> field means "10th and 11th hours"
4. "," (values) - specifies multiple values "MON,WED,FRI" in <day-of-week> field
5. "/" (increments) - specifies the incremental values. eg: "5/15" in <minute> field means at "5, 20, 35, 50 minutes of an hour"
6. "L" (last) - eg: L in <day-of-month>, <day-of-week> implies "Last day of month", "Last day of week", 
                L-3 in <day-of-month> implies "Third to last date of calendar month". 
                6L in <day-of-week> implies "Last Friday"
7. "W" (weekday) - weekday nearest to given day of month. eg: "10W" in <day-of-month> field means "weekday near 10th of that month"
                If 10th is Saturday, then job will be triggered on 9th which is Friday
                If 10th is Sunday, then job will be triggered on 11th which is Monday
                It will not jump back to previous month.
8. "#" - "Nth" occurrence of a weekday of the month. eg: "6#3" means third Friday of the Month