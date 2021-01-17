package tests.gitlab.filterEmpty;
import filterEmpty.FilterEmpty;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

public class FilterEmptyTests {

    public static Random RANDOM = new Random(332134);

    public static final int NUM_SMALL = 250;
    public static final int SMALL_SIZE  = 10;

    public static final int NUM_LARGE = 10;
    public static final int LARGE_SIZE  = 100000;

    public static final int MAX_STRING_LENGTH = 30;

    @Test(timeout = 15000)
    public void testSmall() {
        for (int i = 0; i < NUM_SMALL; i++) {
            String[] input = makeInput(i, SMALL_SIZE);
            int[] output = filter(input);
            runTest(input, output);
        }
    }

    @Test(timeout = 15000)
    public void testLarge() {
        for (int i = 0; i < NUM_LARGE; i++) {
            String[] input = makeRandomInput(LARGE_SIZE);
            runTest(input, filter(input));
        }
    }

    private String makeStringOfLength(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append("" + ('a' + RANDOM.nextInt(26)));
        }
        return str.toString();
    }

    private String[] makeInput(int num, int size) {
        String[] input = new String[size];
        for (int i = size - 1; i >= 0; i--) {
            input[i] = ((num >> i) & 1) == 1 ? makeStringOfLength(RANDOM.nextInt(MAX_STRING_LENGTH) + 1) : "";
        }
        return input;
    }

    private int[] filter(String[] input) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < input.length; i++) {
            if (input[i].length() > 0) {
                list.add(input[i].length());
            }
        }
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    private String[] makeRandomInput(int size) {
        String[] input = new String[size];
        for (int i = size - 1; i >= 0; i--) {
            input[i] = RANDOM.nextBoolean() ? makeStringOfLength(RANDOM.nextInt(MAX_STRING_LENGTH) + 1) : "";
        }
        return input;
    }

    private void runTest(String[] input, int[] expected) {
        String inputString = Arrays.toString(input);
        if (inputString.length() > 0) {
            inputString = inputString.substring(1, inputString.length() - 1);
        }
        String[] arr = inputString.replaceAll("\\s*", "").split(",");
        int[] result = FilterEmpty.filterEmpty(arr);
        assertNotNull(result);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }
}
