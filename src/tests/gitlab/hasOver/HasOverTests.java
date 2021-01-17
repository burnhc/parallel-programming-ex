package tests.gitlab.hasOver;
import java.util.Arrays;
import java.util.Random;

import cse332.exceptions.NotYetImplementedException;
import hasOver.HasOver;
import org.junit.Test;
import static org.junit.Assert.*;

public class HasOverTests {
    public static int[] TEST_ARRAY_0 = new int[] {2, 17, 19, 8, 21, 17, 35, 0, 4, 1};
    public static int[] TEST_ARRAY_1 = new int[] {-2, -17, -19, -8, -21, -17, -35, -4, -1};
    public static Random RANDOM = new Random(332134);

    public static final int FULLY_SEQUENTIAL = Integer.MAX_VALUE;
    public static final int REASONABLE_CUTOFF = 100;
    public static final int FULLY_PARALLEL = 1;

    public static final int MEDIUM_SIZE = 1000;
    public static final int MEDIUM_MAX  = 100;

    public static final int LARGE_SIZE  = 2000000;
    public static final int LARGE_MAX   = 1000;

    public static final int HUGE_SIZE   = 6000000;
    public static final int ALLOWED_TIME = 19000;
    
    @Test(timeout = ALLOWED_TIME)
    public void tiny1() { 
        runTest(17, TEST_ARRAY_0, true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void tiny2() { 
        runTest(35, TEST_ARRAY_0, false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void tiny3() { 
        runTest(40, TEST_ARRAY_0, false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void negative1() {
        runTest(-17, TEST_ARRAY_1, true);
    }

    @Test(timeout = ALLOWED_TIME)
    public void negative2() { 
        runTest(-1, TEST_ARRAY_1, false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void negative3() { 
        runTest(0, TEST_ARRAY_1, false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium1() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, -1), false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium2() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, 0), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium3() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, MEDIUM_SIZE - 1), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium4() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, MEDIUM_SIZE / 2), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium5() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, MEDIUM_SIZE / 4 - 1), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium6() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, MEDIUM_SIZE / 2 + 17), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium7() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, 17), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void medium8() { 
        runTest(MEDIUM_MAX, create(MEDIUM_SIZE, MEDIUM_MAX, -1), false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large1() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, -1), false); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large2() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, 0), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large3() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, LARGE_SIZE - 1), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large4() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, LARGE_SIZE / 2), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large5() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, LARGE_SIZE / 4 - 1), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large6() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, LARGE_SIZE / 2 + 17), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large7() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, 17), true); 
    }

    @Test(timeout = ALLOWED_TIME)
    public void large8() { 
        runTest(LARGE_MAX, create(LARGE_SIZE, LARGE_MAX, -1), false); 
    }

    public int[] create(int size, int max, int where) {
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(max);
        }

        // Place one value > max
        if (where > -1) {
            array[where] = max + 1;
        }

        return array;
    }

    private void runTest(int num, int[] array, boolean expected) {
        boolean actual = HasOver.hasOver(num, array, REASONABLE_CUTOFF);
        assertEquals(expected, actual);
    }
}
