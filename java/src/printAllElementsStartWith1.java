import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class printAllElementsStartWith1 {

    public static void printAllEelementsStartWith1(){

        List<String> numberStartsWith1 = IntStream.range(0, 101).mapToObj(i -> Integer.toString(i)).filter(i -> i.startsWith("1")).collect(Collectors.toList());

        numberStartsWith1.forEach(i -> System.out.print(i +" "));
    }
}
