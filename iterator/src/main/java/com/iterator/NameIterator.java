package com.iterator;

public class NameIterator implements Iterator{
    private String[] names;
    private int index=0;

    public NameIterator(String[] names2) {
        names =names2;
    }

    @Override
    public boolean hasNext() {
        return index < names.length;
    }

    @Override
    public Object next() {
        index+=1;

        if(hasNext())
            return names[index];

        return null;
    }
    
}
