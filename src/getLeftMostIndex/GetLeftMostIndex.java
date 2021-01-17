package getLeftMostIndex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class GetLeftMostIndex {
    public static int SEQUENTIAL_CUTOFF;
    public static ForkJoinPool POOL = new ForkJoinPool();

    static class GetLeftMostIndexFork extends RecursiveTask<Integer> {
        char[] needle, haystack;
        int lo, hi;

        public GetLeftMostIndexFork(char[] needle, char[] haystack, int lo, int hi) {
            this.needle = needle;
            this.haystack = haystack;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected Integer compute() {
            if (hi - lo <= SEQUENTIAL_CUTOFF) {

                // For the length of the partition
                for (int i = lo; i < hi; i++) {

                    // If the needle length + i is <= the length of the entire haystack
                    if (needle.length + i <= haystack.length) {
                        boolean found = true;

                        // For each character in the needle
                        for (int j = 0; j < needle.length; j++) {

                            // Looking ahead at i + j index; comparing it to current char in needle
                            if (haystack[i + j] != needle[j]) {
                                // If char does not match, needle not found
                                found = false;
                                // Exit inner loop; go to next index of partition. If we
                                // don't break, just keep comparing even if it goes past
                                // the upper bound of the partition
                                break;
                            }
                        }

                        // If all chars have matched and needle is found, return index
                        if (found) {
                            return i;
                        }
                    }
                }

                // Not found; return -1
                return -1;

            } else {
                int mid = lo + (hi - lo) / 2;
                GetLeftMostIndexFork left = new GetLeftMostIndexFork(needle, haystack, lo, mid);
                GetLeftMostIndexFork right = new GetLeftMostIndexFork(needle, haystack, mid, hi);
                left.fork();
                int rightAns = right.compute();
                int leftAns = left.join();

                // If found on the left, return index from left; else return index from right
                return leftAns != -1 ? leftAns : rightAns;
            }
        }
    }

    public static int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutoff) {
        SEQUENTIAL_CUTOFF = sequentialCutoff;
        return POOL.invoke(new GetLeftMostIndexFork(needle, haystack, 0, haystack.length));

    }

    private static void usage() {
        System.err.println("USAGE: GetLeftMostIndex <needle> <haystack> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        char[] needle = args[0].toCharArray();
        char[] haystack = args[1].toCharArray();
        try {
            System.out.println(getLeftMostIndex(needle, haystack, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
