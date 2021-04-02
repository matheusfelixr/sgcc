package com.matheusfelixr.sgcc.model.enums;

public enum ControlAccessPointEnum {

    INPUT("Entrada"),
    OUTPUT("Saida");

    private String description;

    private ControlAccessPointEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
