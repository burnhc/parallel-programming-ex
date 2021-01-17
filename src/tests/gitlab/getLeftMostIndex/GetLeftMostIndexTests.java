package tests.gitlab.getLeftMostIndex;
import java.util.Random;

import getLeftMostIndex.GetLeftMostIndex;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetLeftMostIndexTests {
    public static Random RANDOM = new Random(332134);

    public static final int FULLY_SEQUENTIAL = Integer.MAX_VALUE;
    public static final int REASONABLE_CUTOFF = 1000;
    public static final int FULLY_PARALLEL = 1;

    public static final int NUM_SMALL_HAYSTACKS = 250;
    public static final int NUM_SMALL_NEEDLE_SIZES = 5;
    public static final int SMALL_HAYSTACK_SIZE  = 10;

    public static final int NUM_LARGE_HAYSTACKS = 10;
    public static final int LARGE_NEEDLE_SIZE = 10;
    public static final int LARGE_HAYSTACK_SIZE  = 100000;

    @Test(timeout = 25000)
    public void checkSmallSequential() {
        for (int i = 0; i < NUM_SMALL_HAYSTACKS; i++) {
            for (int j = 1; j <= NUM_SMALL_NEEDLE_SIZES; j++) {
                test(makeInput(i, SMALL_HAYSTACK_SIZE), FULLY_SEQUENTIAL, j);
            }
        }
    }

    @Test(timeout = 25000)
    public void checkSmallParallel() {
        for (int i = 0; i < NUM_SMALL_HAYSTACKS; i++) {
            for (int j = 1; j <= NUM_SMALL_NEEDLE_SIZES; j++) {
                test(makeInput(i, SMALL_HAYSTACK_SIZE), FULLY_PARALLEL, j);
            }
        }
    }

    @Test(timeout = 25000)
    public void checkLarge() {
        for (int i = 0; i < NUM_LARGE_HAYSTACKS; i++) {
            test(makeRandomInput(LARGE_HAYSTACK_SIZE), REASONABLE_CUTOFF, LARGE_NEEDLE_SIZE);
        }
    }

    private static int sweep(String needle, String haystack) {
        int currStart = 0;
        int needleIndex = 0;
        while (currStart < haystack.length()) {
            while (needleIndex <= needle.length()) {
                if (needleIndex == needle.length()) {
                    return currStart;
                }
                else if (currStart + needleIndex >= haystack.length()) {
                    return -1;
                }
                else if (needle.charAt(needleIndex) == haystack.charAt(currStart + needleIndex)) {
                    needleIndex++;
                }
                else {
                    break;
                }
            }
            currStart++;
            needleIndex = 0;
        }
        return -1;
    }

    private String makeInput(int num, int size) {
        StringBuilder arr = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            arr.append("" + (char)('0' + (char)((num >> i) & 1)));
        }
        return arr.toString();
    }

    private String makeRandomInput(int size) {
        StringBuilder arr = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            arr.append("" + (char)('0' + (RANDOM.nextBoolean() ? 1 : 0)));
        }
        return arr.toString();
    }


    private void test(String haystack, int cutoff, int needleSize) {
        for (int i = 0; i < (1 << needleSize); i++) { 
            String needle = makeInput(i, needleSize);
            int actual = GetLeftMostIndex.getLeftMostIndex(needle.toCharArray(), haystack.toCharArray(), cutoff);
            int expected = sweep(needle, haystack);
            assertEquals(expected, actual);
        }
    }
}
