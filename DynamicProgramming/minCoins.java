import java.util.*;

public class minCoins {

    public static int minNumberOfCoinsForChange(int n, int[] denoms) {

        int[] dp = new int[n+1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for(int denom : denoms){
            for(int i = denom ; i <=n ; i++){
                if(dp[i - denom] != Integer.MAX_VALUE){
                    dp[i] = Math.min(dp[i], dp[i - denom]+1);
                }
            }
        }

        return dp[n] != Integer.MAX_VALUE? dp[n] : -1;
    }

    public static void main(String[] args) {

        int[] arr = {1, 5, 10};
        int n =1;
        System.out.println(minNumberOfCoinsForChange(n, arr));
    }
}


// 7 [2,4]
//    inf inf
// 7-2 = 5    inf inf
// 7-4 = 3    inf inf