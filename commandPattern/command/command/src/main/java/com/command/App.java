package com.command;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Command pattern ");

        Switcher switcher = new Switcher();

        Light light = new Light();
        TurnOnCommand turnOnCommand = new TurnOnCommand(light);
        TurnOFFCommand turnOFFCommand = new TurnOFFCommand(light);

        switcher.addCommand(turnOnCommand);
        switcher.addCommand(turnOFFCommand);

        switcher.executeCommands();
    }
}
