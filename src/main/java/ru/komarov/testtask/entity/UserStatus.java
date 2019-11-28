package ru.komarov.testtask.entity;

import java.util.NoSuchElementException;

public enum UserStatus {
    ONLINE ("ONLINE"),
    AWAY ("AWAY"),
    OFFLINE ("OFFLINE");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public static UserStatus valueOf(int status) {
        return UserStatus.values()[status];
    }

    public static UserStatus valueOfString(String status) throws IllegalArgumentException {
        switch (status.toUpperCase()) {
            case "ONLINE":
                return ONLINE;
            case "AWAY":
                return AWAY;
            case "OFFLINE":
                return OFFLINE;
            default: throw new IllegalArgumentException("Bad status!");
        }
    }
}
