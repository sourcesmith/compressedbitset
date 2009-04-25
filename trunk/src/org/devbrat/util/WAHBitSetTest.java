package org.devbrat.util;

/**
 * Created by IntelliJ IDEA.
 * User: ddash
 * Date: Nov 10, 2007
 * Time: 12:06:45 PM
 * To change this template use File | Settings | File Templates.
 */

import junit.framework.TestCase;

import java.util.Random;
import java.util.BitSet;
import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class WAHBitSetTest extends TestCase {
    WAHBitSet wahBitSet;

    public WAHBitSetTest() throws Exception {
    }

    public void startUp() throws Exception {
    }

    public void testWAHBitSet() throws Exception {
        WAHBitSet set = new WAHBitSet();
        set.set(10);
        set.set(20);
        set.set(2000);
        set.set(20000);

        assertEquals(set.cardinality(), 4);

        WAHBitSet set1 = new WAHBitSet();
        set1.set(1000);
        set1.set(20000);

        assertTrue(set1.andSize(set) > 0);
        WAHBitSet and = set.and(set1);
        WAHBitSet or = set.or(set1);

        assertEquals(and.cardinality(), 1);
        assertEquals(or.cardinality(), 5);

        or.set(20003);
        assertTrue(or.get(20000));
        assertTrue(or.get(20003));
    }

    public void testProblemBitSets() throws Exception {
        BitSet set1 = (BitSet) readObject("set1");
        BitSet set2 = (BitSet) readObject("set2");

        WAHBitSet wSet1 = new WAHBitSet(set1);
        WAHBitSet wSet2 = new WAHBitSet(set2);

        set1.and(set2);
        //DocSet set4 = set1.union(set2);

        WAHBitSet wSet3 = wSet1.and(wSet2);
        //DocSet wSet4 = wSet1.union(wSet2);

        checkEquality(set2, wSet3);
    }

    private Object readObject(String filename) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public void testWAHBitSetRandomly() throws Exception {
        double fills[] = new double[] { 0.001, 0.005, 0.01, 0.05, 0.1 };
        int maxSizes[] = new int [] { 1000, 10000, 100000, 1000000 };

        for (int i = 0; i < maxSizes.length; i++) {
            int maxSize = maxSizes[i];
            System.out.println("maxSize = " + maxSize);
            for (int j = 0; j < fills.length; j++) {
                double fill = fills[j];
                System.out.println("fill = " + fill);

                BitSet set1 = getBitSet(maxSize, fill);
                BitSet set2 = getBitSet(maxSize, fill);

                WAHBitSet wSet1 = new WAHBitSet(set1);
                WAHBitSet wSet2 = new WAHBitSet(set2);

                checkEquality(set1, wSet1);
                checkEquality(set2, wSet2);

                BitSet set3 = (BitSet) set1.clone();
                set3.and(set2);
                BitSet set4 = (BitSet) set1.clone();
                set4.or(set2);

                WAHBitSet wSet3 = wSet1.and(wSet2);
                WAHBitSet wSet4 = wSet1.or(wSet2);

                if(wSet3.cardinality() > 0) {
                    assertTrue(wSet1.andSize(wSet2) > 0);
                }
                assertEquals(wSet3.cardinality(), wSet1.andSize(wSet2));

                checkEquality(set3, wSet3);
                checkEquality(set4, wSet4);
            }
        }
    }

    private void checkEquality(BitSet set1, WAHBitSet set2) {
        assertEquals(set1.cardinality(), set2.cardinality());

        // slowww....
        Iterator iter1 = set2.iterator();

        int sizeInc = set1.size() / 100;
        int prev = 0, count = 0;
        while(iter1.hasNext()) {
            int doc = (Integer) iter1.next();
            boolean b = set1.get(doc);
            assertTrue(b);
            count ++;
            if(count - prev > sizeInc) {
                prev = count;
            }
        }

        assertTrue(!iter1.hasNext());
    }

    public void testAndBug() throws Exception {
        int[] s1 = new int[] { 99, 185, 240, 250, 265, 283, 312, 457, 488, 516,
                673, 775, 809, 954, 974 };
        int[] s2 = new int[] { 97, 160, 230, 237, 253, 254, 298, 309, 330, 358,
                383, 443, 481, 523, 589, 607, 748, 775, 941, 947 };
        WAHBitSet bs1 = fromInts(s1);
        WAHBitSet bs2 = fromInts(s2);
        WAHBitSet and = bs1.and(bs2);
        // should be 775, but get 868
        assertEquals(775, and.iterator().next());
    }

    private WAHBitSet fromInts(int[] ints) {
        WAHBitSet bitSet = new WAHBitSet();
        for (int i : ints) {
            bitSet.set(i);
        }
        return bitSet;
    }

    private BitSet getBitSet(int maxSize, double fill) {
        Random rand = new Random(System.currentTimeMillis());
        int size = (int) (fill * maxSize);
        BitSet obs = new BitSet(maxSize);
        for(int i = 0; i < size; i++) {
            obs.set(rand.nextInt(maxSize));
        }

        return obs;
    }
}