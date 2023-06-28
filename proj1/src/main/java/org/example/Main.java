package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public class Student{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private Integer age;
        private String gender;

    }


    public static void main(String[] args) {
        System.out.println("Hello world!");

        List<Student> students = new ArrayList<Student>();

        students.stream().sorted((o1,o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());

    }
}