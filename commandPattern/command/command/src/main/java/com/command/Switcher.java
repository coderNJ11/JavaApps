package com.command;

import java.util.ArrayList;
import java.util.List;

public class Switcher {
    
    List<Command> commands;

    public Switcher(){
        this.commands = new ArrayList<>();
    }

    public void addCommand(Command command){
        this.commands.add(command);
    }

    public void executeCommands(){
        commands.stream().forEach(command -> command.execute());
    }    
}
