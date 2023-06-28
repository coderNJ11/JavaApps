import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ArrayMultiplicationUsingStream {


    public static void multiplyArray(){

        Integer[] inputArray = {1,2,3,4,5};
        List<Integer> numerList = Arrays.asList(inputArray);

        Optional<Integer> output =  numerList.stream().reduce((a, b) -> a*b);

        if(output.isPresent()){
            System.out.println("Array Multiply "+ output.get());
        }

    }
}
