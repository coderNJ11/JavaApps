package com.iterator;

public class NameRepository  {

    private String[] names = {"Adam" , "Tom" , "Ana" , "Michael" };

    public Iterator getIterator(){
        return new NameIterator(names);
    }

    
}
