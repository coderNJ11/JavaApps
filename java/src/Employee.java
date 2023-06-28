import java.security.cert.CertPath;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Employee {

    String name;
    String city;
    Integer id;
    Integer salary;
    Integer department;

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }



    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", id=" + id +
                '}';
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public static void main(String[] args) {


        ArrayMultiplicationUsingStream.multiplyArray();

        System.out.println("++++++++++++++++++++++++++++++");

        Employee e1 = new Employee();
        e1.setName("Rishi");
        e1.setCity("Plano");
        e1.setId(1);
        e1.setSalary(7001);
        e1.setDepartment(2);

        Employee e2 = new Employee();
        e2.setName("Muffi");
        e2.setCity("Seattle");
        e2.setId(2);
        e2.setSalary(6000);
        e2.setDepartment(2);

        Employee e3 = new Employee();
        e3.setName("Sagar");
        e3.setCity("Chicago");
        e3.setId(3);
        e3.setSalary(2000);
        e3.setDepartment(3);

        Employee e4 = new Employee();
        e4.setName("Rhythm");
        e4.setCity("san jose");
        e4.setId(4);
        e4.setSalary(3000);
        e4.setDepartment(4);

        Employee e5 = new Employee();
        e5.setName("Nitish");
        e5.setCity("Plano");
        e5.setId(5);
        e5.setSalary(4000);
        e5.setDepartment(5);

        Employee e6 = new Employee();
        e6.setName("zakir");
        e6.setCity("Seattle");
        e6.setId(6);
        e6.setSalary(7000);
        e6.setDepartment(3);

        Employee e7 = new Employee();
        e7.setName("Amit");
        e7.setCity("Chicago");
        e7.setId(7);
        e7.setSalary(8000);
        e7.setDepartment(4);

        Employee e8 = new Employee();
        e8.setName("Jignesh");
        e8.setCity("san jose");
        e8.setId(8);
        e8.setSalary(1500);
        e8.setDepartment(5);

        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(e1);
        employeeList.add(e2);
        employeeList.add(e3);
        employeeList.add(e4);
        employeeList.add(e5);
        employeeList.add(e6);
        employeeList.add(e7);
        employeeList.add(e8);

        Map<String , List<Employee>> employeeNameMap = employeeList.stream().filter(e -> e!=null).collect(Collectors.groupingBy(e -> e.getName()));

        employeeNameMap.forEach((k,v)  -> System.out.println(k +":: "+ v));

        System.out.println("\n +++++++++++++++++ Sorted List By Name +++++++++++++++++++++++++++\n");
        List<Employee> sortedEmployee =  employeeList.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName()) ).collect(Collectors.toList());
        sortedEmployee.forEach(e -> System.out.println(e.toString()));

        System.out.println("\n +++++++++++++++++ Sorted List By ID +++++++++++++++++++++++++++\n");
        List<Employee> sortedListId = employeeList.stream().sorted((o1, o2) -> o1.getId() - o2.getId()).collect(Collectors.toList());
        sortedListId.forEach(e -> System.out.println(e.toString()));


        System.out.println("\n +++++++++++++++++ Sum of employee salary by department+++++++++++++++++++++++++++\n");
        Map<Integer , Integer> employeedDeptToSlaarySum =employeeList.stream().collect(Collectors.groupingBy(e -> e.getDepartment(), Collectors.summingInt(e ->e.getSalary())));
        employeedDeptToSlaarySum.forEach((k,v) -> System.out.println( "Department iD :: "+k+" Toatal salary "+v));

        System.out.println("\n +++++++++++++++++ Average of employee salary by department+++++++++++++++++++++++++++\n");
        Map<Integer, Double> deptAvgSalary = employeeList.stream().collect(Collectors.groupingBy(e -> e.getDepartment() , Collectors.averagingInt(e->e.getSalary())));
        deptAvgSalary.forEach((k,v) -> System.out.println(" Department "+k+ " avergae salary "+ v));

        System.out.println("\n +++++++++++++++++ Sorted Frequency +++++++++++++++++++++++++++\n");
        StringCharFreq.stringArrayFreq();

        System.out.println("\n +++++++++++++++++ Second max in the list  +++++++++++++++++++++++++++\n");
        SecondHighestNum.seconHighestNum();

        System.out.println("\n +++++++++++++++++ Nth smalleest num in the list  +++++++++++++++++++++++++++\n");
        SecondHighestNum.nthinInList();

        System.out.println("\n +++++++++++++++++ Nth largest num in the list  +++++++++++++++++++++++++++\n");
        SecondHighestNum.nthLargestInList();

        System.out.println("\n +++++++++++++++++ Max length string in the list  +++++++++++++++++++++++++++\n");
        LongestStringInArray.longestStringInArr();

        System.out.println("\n +++++++++++++++++ Reverse String using stack in Jvaa 8 +++++++++++++++++++++++++++\n");
        ReservseString.reverseString();

        System.out.println("\n +++++++++++++++++ print All elements start with 1+++++++++++++++++++++++++++\n");
        printAllElementsStartWith1.printAllEelementsStartWith1();

        System.out.println("\n +++++++++++++++++ print All number till 25 having 2's +++++++++++++++++++++++++++\n");
        printAllNumbersupto25Having2.printAllNumbersupto25Having2();

        System.out.println("\n +++++++++++++++++ String joining example+++++++++++++++++++++++++++\n");
        StringJoining.StringJoining();

        System.out.println("\n +++++++++++++++++ Threading example+++++++++++++++++++++++++++\n");
        ThreadingRunnable.threadinRun();
    }

    public class NameComparator implements Comparator<Employee> {

        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}

