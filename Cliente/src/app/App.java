package app;

import license.License;

public class App {


    public static void main (String[] args ){
        init();

        // Programa com esteroides. LOW PRIORITY
        System.out.println("Hello World.");
    }

    private static void init(){
        License lic = new License("HelloWorld", "1.0.0");
    }
}
