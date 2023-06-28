import java.util.Arrays;
import java.util.List;

public class StringJoining {

    public static void StringJoining(){

        List<String> nos = Arrays.asList("1", "2" , "3" , "4" , "5" ,"6");

        String results = String.join("," , nos);
        System.out.println(" joined String "+ results);

    }
}
