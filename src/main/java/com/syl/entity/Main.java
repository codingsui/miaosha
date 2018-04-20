package com.syl.entity;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        Character c = new Character('a');
        Character c1 = new Character('z');
        System.out.println((int)c1.charValue() + "  "+(int)c.charValue());
        print(str);
    }

    private static void print(String str) {

    }

}
