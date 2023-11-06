package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        Map<String, Integer> dallasDistance = new HashMap<>();
        dallasDistance.put("Austin", 100);
        dallasDistance.put("okalhoma", 120);

        CitysConnect dallas = new CitysConnect("dallas", dallasDistance);


        Map<String, Integer> AustinDistance = new HashMap<>();
        dallasDistance.put("Dallas", 100);
        dallasDistance.put("Houston", 60);

        CitysConnect AustinConnect = new CitysConnect("Austin", AustinDistance);
        List<CitysConnect> listConnect = new ArrayList<>();
        listConnect.add(dallas);
        listConnect.add(AustinConnect);

        Connections connections = new Connections(listConnect);





    }

}

/*

students:  id , name , dob
Subjects: id , subName
ref_sub_student : student id , subject id , marks

{
    "id": <student_id>,
    "Student_name": <student_name>
    "sub_id": <sub_id>,
    "subject_name": <name>,
    marks: <Int>
}

//cities  ,distance

cities - connected


 */