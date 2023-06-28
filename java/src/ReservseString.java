import java.util.*;
import java.util.stream.Collectors;

public class ReservseString {

    public static void reverseString() {

        String input = "Rajeshvar";

        String[] inputsArr = input.split("");
        Stack strStack = Arrays.asList(inputsArr).stream().collect(Collectors.toCollection(Stack::new));
        Object st =null;
        while(!strStack.isEmpty() && strStack.peek()!=null){
            st = strStack.peek();
            System.out.print( st!=null? st: "");
            strStack.pop();
        }


    }
}
