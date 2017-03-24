/* 
 *
 * This source file is a part of lainexperiment project.
 * Description for it can be found in ReadMe.txt.
 *
 */

/*
 * Date: 13/08/2015
 * 
 * Problem
 * 
 * Print factorials up to 100 mod 10 000 007.
 * 
 */

package lainexperiment.misc;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Calculate_factorials_mod_K {

    static Object fac(int n) {
        if (n <= 1) return 1;
        return IntStream.range(1, n + 1).reduce((r, i) -> {
            r *= i;
            return r % 10_000_007;
        }).getAsInt();
    }

    static void printFactorials() {
        System.out.println(
                IntStream.range(1, 101).mapToObj((i) -> String.valueOf(fac(i))).collect(Collectors.joining(", "))
        );
    }
    
    public static void main(String[] args) {
        printFactorials();
    }

}