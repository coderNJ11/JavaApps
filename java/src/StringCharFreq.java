import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringCharFreq {

    public static void stringArrayFreq(){

        String input = "ilovedallastexas";

        String[] chars = input.split("");
        Map<String , Long> stringToCount = Arrays.asList(chars).stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()));

        stringToCount.forEach((k,v) -> System.out.println(k + "::"+v));

        System.out.println("\n Duplicate element in the list +++++++++++++++++++++++++++\n");

         List<String> duplicateCharacters =  stringToCount.entrySet().stream().filter(k -> k.getValue()>1).map( k -> k.getKey()).collect(Collectors.toList());
         duplicateCharacters.forEach(l -> System.out.println(l +" "));

        System.out.println("\n Non Duplicate element in the list +++++++++++++++++++++++++++\n");

        List<String> nonDuplicates = Arrays.asList(chars).stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
                .entrySet().stream().filter( k -> k.getValue()==1).map( k -> k.getKey()).collect(Collectors.toList());

        nonDuplicates.forEach(n -> System.out.println( n + " "));

        System.out.println("\n First Non repeating element in the list +++++++++++++++++++++++++++\n");

        List<String> charStr = Arrays.asList(chars);
        String charFirst = charStr.stream().filter(i -> Collections.frequency(charStr, i) ==1).findFirst().orElse(null);
        System.out.println(" First non repeating charaacter of a string ilovedallastexas   ::::: "+ charFirst);
    }
}
