package com.iterator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        NameRepository repo = new NameRepository();
        for(Iterator it = repo.getIterator() ; it.hasNext();) {
            String name = (String) it.next();
            System.out.println(name);

        }
    }
}
