package com.syl.dao;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int a[] = new int[n];
        int change = n/(2*m);
        int count = 0;
        for (int i = 0;i<n;i++){
            a[i]= i+1;
            count++;
            if (count == count){
                a[i] = -a[i];
                count=0;
            }
        }
        System.out.println(Arrays.toString(a));
    }
}
