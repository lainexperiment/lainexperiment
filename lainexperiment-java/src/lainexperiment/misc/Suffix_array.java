/* 
 *
 * This source file is a part of lainexperiment project.
 * Description for it can be found in ReadMe.txt.
 *
 */
/*
 * 
 * Date: 04/07/2015
 * 
 * Given a string build a suffix array for it.
 * 
 * Input Format
 * 
 * String
 * 
 * Output Format
 * 
 * Suffix array
 * 
 * Sample Input
 * 
banana
 * 
 * Sample Output
 * 
4, 3, 6, 2, 5, 1
 * 
 */

package lainexperiment.misc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

public class Suffix_array {

    static class Prefix implements Comparable<Prefix> {
        // a - index of the previous group
        // b - index of the current group
        int a, b;
        // index where prefix starts in A
        int p;
        @Override
        public int compareTo(Prefix o) {
            if (a == o.a) return b - o.b;
            return a - o.a;
        }
        @Override
        public String toString() {
            return String.format("%d<%d,%d>", p, a, b);
        }
    }
    
    char[] A;
    // maps suffixes of the input array A to their
    // positions in SA
    int[] RANK;
    // suffix array
    int[] SA;
    
    Suffix_array(char[] a) {
        A = a;
        RANK = new int[a.length];
        suffixArray(a, 1);
        SA = invertedIndex(RANK);
    }
    
    private int[] invertedIndex(int[] a) {
        int[] inv = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            inv[a[i]] = i;
        }
        return inv;
    }

    int[] find(char[] a) {
        return find(a, 0, 0, SA.length - 1);
    }
    
    private int[] find(char[] a, int i, int s, int e) {
        int[] range = findRange(a[i], i, s, e);
        if (range == null) return null;
        if (i == a.length - 1) return range;
        range = find(a, i + 1, range[0], range[1]);
        if (range == null) return range;
        return range;
    }
    
    private int[] findRange(char ch, int i, int s, int e) {
        if (e < s)
            return null;
        if (e == s)
            if (SA[e] + i < A.length && A[SA[e] + i] == ch)
                return new int[]{s, s};
            else
                return null;
        int m = (e - s) / 2 + s;
        char mch = SA[m] + i < A.length? A[SA[m] + i]: 0;
        if (mch < ch)
            return findRange(ch, i, m + 1, e);
        if (mch > ch)
            return findRange(ch, i, s, m - 1);
        if (mch == ch) {
            int[] l = null;
            if (m != 0)
                l = findRange(ch, i, s, m - 1);
            if (l == null)
                l = new int[]{m, m};
            int[] r = null;
            if (m != SA.length - 1)
                r = findRange(ch, i, m + 1, e);
            if (r == null)
                r = new int[]{m, m};
            return new int[]{l[0], r[1]};
        }
        return null;
    }
    
    /**
     * Prefix doubling implementation.
     * 
     * @param n length of a new prefix
     */
    private void suffixArray(char[] A, int n) {
        if (n >= A.length * 2) {
            return;
        }
        Prefix[] P = new Prefix[RANK.length];
        for (int i = 0; i < RANK.length; ++i) {
            Prefix p = new Prefix();
            p.a = RANK[i];
            int j = i + n / 2;
            if (j < A.length)
                p.b = n == 1? A[i]: RANK[j];
            else
                p.b = -i;
            p.p = i;
            P[i] = p;
        }
        Arrays.sort(P);
        int a = P[0].a;
        int b = P[0].b;
        int c = 0;
        for (int i = 0; i < P.length; ++i) {
            RANK[P[i].p] = c;
            if (P[i].a == a && P[i].b == b)
                continue;
            RANK[P[i].p] = ++c;
            a = P[i].a;
            b = P[i].b;
        }
        suffixArray(A, n * 2);
    }
    
    public static void main(String[] args) {
        Suffix_array sa = null;
        
        sa = new Suffix_array("banana".toCharArray());
        assertEquals("[3, 2, 5, 1, 4, 0]", Arrays.toString(sa.RANK));
        assertEquals("[1, 2]", Arrays.toString(sa.find("an".toCharArray())));
        assertEquals("[3, 3]", Arrays.toString(sa.find("b".toCharArray())));
        assertEquals("[3, 3]", Arrays.toString(sa.find("ba".toCharArray())));
        assertEquals("[4, 5]", Arrays.toString(sa.find("n".toCharArray())));
        
        sa = new Suffix_array("bobocel".toCharArray());
        assertEquals("[0, 5, 1, 6, 2, 3, 4]", Arrays.toString(sa.RANK));
        
        sa = new Suffix_array("mississippi".toCharArray());
        assertEquals("[4, 3, 10, 8, 2, 9, 7, 1, 6, 5, 0]", Arrays.toString(sa.RANK));
        
        sa = new Suffix_array("aaaaaaa".toCharArray());
        assertEquals("[6, 5, 4, 3, 2, 1, 0]", Arrays.toString(sa.RANK));
        
        sa = new Suffix_array("attcatg".toCharArray());
        assertEquals("[1, 6, 4, 2, 0, 5, 3]", Arrays.toString(sa.RANK));
        
        sa = new Suffix_array("aacbaada".toCharArray());
        assertEquals("[1, 3, 6, 5, 2, 4, 7, 0]", Arrays.toString(sa.RANK));
        
        sa = new Suffix_array("aadcaabe".toCharArray());
        System.out.println(Arrays.toString(sa.RANK));
    }

}
