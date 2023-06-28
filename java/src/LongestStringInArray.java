import java.util.Arrays;
import java.util.stream.Collectors;

public class LongestStringInArray {

    public static void longestStringInArr() {
        String[] strArr = {"Ball", "cricket", "football", "springBoot", "Microservices"};

        String maxLengthString=Arrays.asList(strArr).stream().collect(Collectors.maxBy((o1, o2) -> o1.length() - o2.length())).get();

        System.out.println("max length string from  {\"Ball\", \"cricket\", \"football\", \"springBoot\", \"Microservices\"} ::::" + maxLengthString);



        //   Using map reduce

        maxLengthString = Arrays.asList(strArr).stream().collect(Collectors.reducing( (o1,o2) -> o1.length()>o2.length()?o1:o2 )).get();
        System.out.println("max length string using collectors.reducing() from  {\"Ball\", \"cricket\", \"football\", \"springBoot\", \"Microservices\"} ::::" + maxLengthString);


        maxLengthString = Arrays.asList(strArr).stream().reduce((o1,o2) -> o1.length()>o2.length()? o1: o2).get();
        System.out.println("max length string using reduce() from  {\"Ball\", \"cricket\", \"football\", \"springBoot\", \"Microservices\"} ::::" + maxLengthString);

    }
}
