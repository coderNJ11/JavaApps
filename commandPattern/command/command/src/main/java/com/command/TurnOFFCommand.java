package com.command;

import java.util.LinkedHashMap;

public class TurnOFFCommand implements Command{

    private Light light;

    public TurnOFFCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute() {
        this.light.turnOFF();
    }
    
}
