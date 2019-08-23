package com.mikaelr.textgameapp;

import java.util.LinkedHashMap;
import java.util.Map;

public class Rooms {

    private Map<Integer, String> descriptions;

    public Rooms() {
        this.descriptions = new LinkedHashMap<>();

        descriptions.put(1, "You enter a room!");
        descriptions.put(2, "You enter a room!");
        descriptions.put(3, "You enter a room!");
        descriptions.put(4, "You enter a room!");
        descriptions.put(5, "You enter a room!");
        descriptions.put(6, "You enter a room!");
        descriptions.put(7, "You enter a room!");
        descriptions.put(8, "You enter a room!");
        descriptions.put(9, "You enter a room!");
        descriptions.put(0, "Dragon room!");

    }

    public String getDescription(int ID) {
        return descriptions.get(ID);
    }



}
