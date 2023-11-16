import java.util.*;
import java.util.stream.IntStream;

class Program {
    public static int numberOfWaysToMakeChange(int n, int[] denoms) {
     Integer res = null;
     Integer ways[] = new Integer[n+1];
     ways[0] =1;
     IntStream.of(denoms).forEach(d ->{
      // res = makeDenomSum(n ,0 , d , ways);
     });
    return ways[n];
  }

    public static Integer makeDenomSum(int n, int i, int d, Integer[] ways) {
        if (i == d + 1) {
            return 0;
        }

        if (i == n + 1)
            return ways[n];

        if (d <= i) {
            ways[i] = ways[i] + ways[i - d];
        }
        return makeDenomSum(n, i + 1, d, ways);
    }

    public static void main(String[] args) {

        int[] arr = { 1, 2, 5, 10, 15 };
        System.out.println(numberOfWaysToMakeChange(20.arr));
    }

}

// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
// 1 ->1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
// 2 ->1 1 2 2 3 3 4 4