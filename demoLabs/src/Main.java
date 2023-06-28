import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        List<String> cities = new ArrayList<String>();
        cities.add("Boston");
        cities.add("Austin");
        cities.add("Chicago");
        cities.add("Atlanta");

        long count = cities.stream().filter(c -> c.startsWith("A")).count();

        System.out.print("Count with cities that start with letter A :: "+ count);


        System.out.println(" state :: "+ cities.stream().filter(c -> c.startsWith("Austin")).map(c -> "Texas").findAny()
                .orElse(new String("Match Not found")));

        List<String> weather = Arrays.asList("IT" , "is" , "80" , "Degrees" , "Outside");

        System.out.println(weather.stream().filter(w -> w!=null).map(w -> w).collect(Collectors.joining(new StringBuffer())));

        List<Integer> ages = Arrays.asList( 1, 2, 20 , 5 , 70);

        List<Integer> minors = ages.stream().filter(a -> a<18).collect(Collectors.toList());
        System.out.println();
    }
}