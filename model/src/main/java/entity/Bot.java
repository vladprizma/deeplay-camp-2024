package entity;

import enums.Color;

public class Bot extends User {
    public Bot(String id, Color color) {
        super(Integer.parseInt(id), "", "", 1, 1, "");
    }
}
