import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class printAllNumbersupto25Having2 {

    public static void printAllNumbersupto25Having2(){

        Long totalCount2s = IntStream.range(0,26).boxed().map(i -> Integer.toString(i))
                .map(s -> Arrays.stream(s.split(""))
                        .filter(t -> {
                            if(t.contains("2")) {
                                System.out.println(s);
                                return true;
                            }
                            return false;
                        })
                        .count() )
                .filter(c -> c>0)
                .collect(Collectors.summingLong(c -> c));

        System.out.println(" total Count of 2 til 25 ::::: "+ totalCount2s);
    }
}
