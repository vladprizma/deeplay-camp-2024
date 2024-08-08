package io.deeplay.camp.entity;

import io.deeplay.camp.enums.Color;

public class Bot extends User {
    public Bot(String id, Color color) {
        super(Integer.parseInt(id), "", "", 1, 1, "");
    }
}
