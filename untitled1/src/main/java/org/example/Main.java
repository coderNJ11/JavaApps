package org.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        /*
         * List<List<String>>    [[1,2,3] , [], [5,6]] => [1, 5]
         */


        List<List<Object>> listOfList = new ArrayList<List<Object>>();

        List<Object> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");

        listOfList.add(list1);

        List<Object> listOfFirst = listOfList.stream().filter(x -> x != null).map(x -> x.get(0))
                .collect(Collectors.toList());


        //jason.jinyoung.shin@citi.com


        String str ="abcdefgh";
        String reversed = str.chars().mapToObj(ch -> (char)ch).reduce("" , (a,b)-> b+a , (a2,b2)-> b2+a2);
        System.out.println(reversed);

        String reversed2 = Arrays.stream(str.split("")).reduce("", (a,b)->b+a, (a2,b2)-> b2+a2);
        System.out.println(reversed2);

        String reversed3 = Arrays.stream(str.split("")).reduce("", (a,b)->b+a);
        System.out.println(reversed3);

        String reversed4 = IntStream.range(0,str.length())
                .mapToObj(i -> str.charAt(str.length()-1 -i))
                .collect(StringBuilder::new , StringBuilder::append , StringBuilder::append)
                .toString();
        System.out.println(reversed4);

        String reversedBuilder = new StringBuffer(str).reverse().toString();
        System.out.println(reversedBuilder);


        // Check if string is palindrom
        String str2 = "ava";

        Boolean isPalindrom = IntStream.range(0, str2.length()/2).filter(i -> str2.charAt(i) != str2.charAt(str2.length() -1 -i)).findAny().isEmpty();

        System.out.println(isPalindrom);



        List<Employee> employees = new ArrayList<Employee>();
        Employee e1 = new Employee();
        e1.setAge(40);
        e1.setName("Raj");

        Employee e2 = new Employee();
        e2.setAge(42);
        e2.setName("Muf");

        Employee e3 = new Employee();
        e3.setAge(30);
        e3.setName("Sagar");

        Employee e4 = new Employee();
        e3.setAge(30);
        e3.setName("Sagar");


        employees.add(e1);
        employees.add(e2);
        employees.add(e3);
        employees.add(e3);
        employees.add(e4);
        employees.add(e4);





        List<Employee> emp25 = employees.stream().filter(e -> e.age>25)
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());

        emp25.forEach(e -> System.out.println(e));

        System.out.println("------------------------");
        //using Collectors.joining(demlitor);
        String emp25str = employees.stream().filter(e -> e.age>25)
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .map( e ->e.getName())
                .collect(Collectors.joining(","));


        System.out.println("------------------------");
        System.out.println(emp25str);

        //using reduce
        String emp25Strrrr = employees.stream().filter(e -> e.getAge()>25)
                .map(e -> e.getName())
                .sorted()
                .collect(Collectors.joining("," , "[" ,"]"));



        System.out.println("--------- Joing Delmit and Brackets---------------");
        System.out.println(emp25Strrrr);

        List<Employee> sortedByCollectionsFreq = employees.stream().filter(e -> Collections.frequency(employees, e) ==1).collect(Collectors.toList());


        System.out.println("------------DUPLICATES------------");
        System.out.println(sortedByCollectionsFreq);

        System.out.println("-----------COUNTING -------------");
        Map<Employee , Long> ecmployeeMap = employees.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()));
        System.out.println("------------------------");
        System.out.println(ecmployeeMap);

        System.out.println("-----------All Employee Summ age -------------");
        Long summingAgeMap = employees.stream().filter(e -> Collections.frequency(employees,e)>=1).collect(Collectors.summingLong(Employee::getAge));
        System.out.println(summingAgeMap);

        System.out.println("-----------unique Employee Summ age -------------");
        Long summingMap = employees.stream().filter(e -> Collections.frequency(employees,e)==1).collect(Collectors.summingLong(Employee::getAge));
        System.out.println(summingMap);
        //remove duplidates

        List<Employee> distinct = employees.stream().distinct().collect(Collectors.toList());
        System.out.println("------------------------");
        System.out.println(distinct);


        // remove Duplicate from AraylIst
        List<Integer> intList = Arrays.asList( 1, 2, 2, 3, 2, 1,11 , 8, 111, 31 ,0 , 5, 4, 200, 121);

        List<Integer> nonDuplicatesInList = intList.stream().filter(i -> Collections.frequency(intList, i) ==1).sorted().collect(Collectors.toList());
        System.out.println("------------------------");
        System.out.println(nonDuplicatesInList);


        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );

        System.out.println("------------------------");

        Map<BigDecimal , Set<Item>> mapItemSet = items.stream().collect(Collectors.groupingBy( item -> item.getPrice() , Collectors.mapping( e -> e , Collectors.toSet())));
        System.out.println(mapItemSet);
    }


}