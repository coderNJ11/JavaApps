import java.util.Comparator;

public class Employee implements Comparable<Employee> {
     
    int id;
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Employee(int id , String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(Employee e){
        return this.id - e.id;   // 0 , -1 . 1 
    }

    public static Comparator<Employee> nameComparator = new Comparator<Employee>(){
        @Override
        public int compare(Employee e1, Employee e2){
            return e1.getName().compareTo(e2.getName());
        }

        
       
    };

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + "]";
    }

   
}

