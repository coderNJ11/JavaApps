import java.util.*;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args)  {
        System.out.println("Hello world!");

        // 2 's [0,25] ex 12 (1) 22(2)

        List<String> numbers25 = new ArrayList<String>();

//        int i=1;
//        while( i< 26){
//            numbers25.add( Integer.toString(i));
//            i++;
//        }


        HashSet<Character> ch = new HashSet<Character>();
        ch.add('2');
       // Integer count =0;

        Long count = IntStream.range(0, 26)
                .filter(i -> {

                    boolean  doesContain2 =false;
                    char[] chArr = Integer.toString(i).toCharArray();
                    for(char chr: chArr){
                        if (chr == '2') {
                           // count++;
                            doesContain2 = true;
                            System.out.println(i + "\n");
                        }
                        if (doesContain2)
                            return true;

                    }
                    return false;
                }).count();

        System.out.println("count " + count);
   }



}