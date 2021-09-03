package ru.javawebinar.basejava;

public class testSingleton {
    private testSingleton() {
    }

    private static final testSingleton singleton = new testSingleton();
    private static testSingleton getInstance() {
        return singleton;
    }

}
