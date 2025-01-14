package application;

import java.util.Properties;
public class Runtime {
    public interface Runnable {
        void run(Properties properties, String[] args);
    }

    public static void run(String[] args) {
        Properties properties = new Properties();
        new Application().run(properties, args);
    }
} 