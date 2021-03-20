package com.weldbit.scout.logging;

import java.io.IOException;

public class Log {
    private Log() {
    }

    public static void log(String... args) {
        for (String string : args) {
            System.out.println(string);
        }
    }

    public static void log(IOException e) {
        e.printStackTrace();
    }
}
