import java.util.*;
import java.util.stream.IntStream;

public class maxSum2 {

    public static int maxSubsetSumNoAdjacent(int[] array) { // [11 ,1 , 5 , 3 , 10 , 22 , 7]


        if(array.length == 0)
            return 0;

        int second = array[0];

        if(array.length == 1)
            return second;     
        
        int first = Math.max(array[0], array[1]);

        if(array.length ==2)
            return first;
        
        int max_Sum = maxSumCompare(2, array, second, first, second);

        return max_Sum;
    }

    public static int maxSumCompare(int i, int[] array, int current, int first, int second) {
        if (i == array.length)
            return current;

        if (array[i] + second > first) {
            current = array[i] + second;
        } else {
            current = first;
        }
        second = first;
        first = current;
        return maxSumCompare(i + 1, array, current, first, second);

    }

    public static void main(String[] args) {

        int[] arr = { 75, 105, 120, 75, 90, 135 };
        System.out.println(maxSubsetSumNoAdjacent(arr));
    }
}
