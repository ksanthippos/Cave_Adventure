package com.mikaelr.textgameapp;

public class SubCommand {

    private String name;
    private String description;
    private Runnable action;


    public SubCommand(String name, String description, Runnable action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void execute() {
        action.run();
    }
}
