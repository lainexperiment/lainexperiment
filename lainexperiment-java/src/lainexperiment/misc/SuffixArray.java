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

public class SuffixArray {

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
    int[] ASA;
    // suffix array
    int[] SA;
    
    SuffixArray(char[] a) {
        A = a;
        ASA = new int[a.length];
        suffixArray(a, 1);
        SA = invertedIndex(ASA);
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
        Prefix[] P = new Prefix[ASA.length];
        for (int i = 0; i < ASA.length; ++i) {
            Prefix p = new Prefix();
            p.a = ASA[i];
            int j = i + n / 2;
            if (j < A.length)
                p.b = n == 1? A[i]: ASA[j];
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
            ASA[P[i].p] = c;
            if (P[i].a == a && P[i].b == b)
                continue;
            ASA[P[i].p] = ++c;
            a = P[i].a;
            b = P[i].b;
        }
        suffixArray(A, n * 2);
    }
    
    public static void main(String[] args) {
        SuffixArray sa = null;
        
        sa = new SuffixArray("banana".toCharArray());
        assertEquals("[3, 2, 5, 1, 4, 0]", Arrays.toString(sa.ASA));
        assertEquals("[1, 2]", Arrays.toString(sa.find("an".toCharArray())));
        assertEquals("[3, 3]", Arrays.toString(sa.find("b".toCharArray())));
        assertEquals("[3, 3]", Arrays.toString(sa.find("ba".toCharArray())));
        assertEquals("[4, 5]", Arrays.toString(sa.find("n".toCharArray())));
        
        sa = new SuffixArray("bobocel".toCharArray());
        assertEquals("[0, 5, 1, 6, 2, 3, 4]", Arrays.toString(sa.ASA));
        
        sa = new SuffixArray("mississippi".toCharArray());
        assertEquals("[4, 3, 10, 8, 2, 9, 7, 1, 6, 5, 0]", Arrays.toString(sa.ASA));
        
        sa = new SuffixArray("aaaaaaa".toCharArray());
        assertEquals("[6, 5, 4, 3, 2, 1, 0]", Arrays.toString(sa.ASA));
        
        sa = new SuffixArray("attcatg".toCharArray());
        assertEquals("[1, 6, 4, 2, 0, 5, 3]", Arrays.toString(sa.ASA));
        
        sa = new SuffixArray("aacbaada".toCharArray());
        assertEquals("[1, 3, 6, 5, 2, 4, 7, 0]", Arrays.toString(sa.ASA));
        
        sa = new SuffixArray("aadcaabe".toCharArray());
        System.out.println(Arrays.toString(sa.ASA));
    }

}
