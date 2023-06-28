import java.net.StandardSocketOptions;
import java.util.*;

public class SecondHighestNum {



    static void seconHighestNum() {
        Integer[] numbs = {5, 9, 11, 2, 3, 8, 21, 29, 1};
        List<Integer> numbList = Arrays.asList(numbs);

        Optional<Integer> secondMax = numbList.stream().filter(i -> i < Collections.max(numbList)).sorted((o1 , o2) -> o2 -o1). findFirst();

        if(secondMax.isPresent())
            System.out.println(" second man in list {5, 9, 11, 2, 3, 8, 21, 29, 1} is ::: "+ secondMax.get());

    }

    public static void nthinInList() {

        System.out.print(" Enteer Nth min in the list :{5, 9, 11, 2, 3, 8, 21, 29, 1};:::");
        int[] numbersArr = {5, 9, 11, 2, 3, 8, 21, 29, 1};

        Integer nth = new Scanner(System.in).nextInt();

        Integer nthMinimumNumber = Arrays.stream(numbersArr).boxed().sorted().skip(nth-1).findFirst().get();

        System.out.println("Nth Minimum  Number in the Array :::::: "+ nthMinimumNumber);

    }


    public static void nthLargestInList(){
        System.out.print(" Enteer Nth min in the list :{5, 9, 11, 2, 3, 8, 21, 29, 1};:::");
        int[] numbersArr = {5, 9, 11, 2, 3, 8, 21, 29, 1};

        Integer nthLargestNumber = new Scanner(System.in).nextInt();

        Integer nthLargestNum = Arrays.stream(numbersArr).boxed().sorted(Comparator.reverseOrder()).skip(nthLargestNumber-1).findFirst().get();

        System.out.println("Nth Minimum  Number in the Array :::::: "+ nthLargestNum);

    }
}