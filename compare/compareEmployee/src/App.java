import java.util.Arrays;


public class App {

    public static void main(String[] args){

        Employee empArr[] = new Employee[4];
        empArr[0] = new Employee(5, "John"); 
        empArr[1] = new Employee(9, "Paul'");
        empArr[2] = new Employee(1, "Amit");
        empArr[3] = new Employee(10, "Ame");
        Arrays.sort(empArr);
        System.out.println("Sorted array is "+Arrays.toString(empArr));
    }
}





