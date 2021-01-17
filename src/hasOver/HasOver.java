package hasOver;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HasOver {
    public static int SEQUENTIAL_CUTOFF;
    public static ForkJoinPool POOL = new ForkJoinPool();

    static class HasOverFork extends RecursiveTask<Boolean> {
        int[] arr;
        int val, lo, hi;

        public HasOverFork(int[] arr, int val, int lo, int hi) {
            this.arr = arr;
            this.hi = hi;
            this.lo = lo;
            this.val = val;
        }

        @Override
        protected Boolean compute() {
            if (hi - lo <= SEQUENTIAL_CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    if (arr[i] > val) {
                        return true;
                    }
                }
                return false;
            } else {
                int mid = lo + (hi - lo) / 2;
                HasOverFork left = new HasOverFork(arr, val, lo, mid);
                HasOverFork right = new HasOverFork(arr, val, mid, hi);
                left.fork();
                Boolean rightAns = right.compute();
                Boolean leftAns = left.join();
                return rightAns || leftAns;
            }
        }
    }

    public static boolean hasOver(int val, int[] arr, int sequentialCutoff) {
        SEQUENTIAL_CUTOFF = sequentialCutoff;
        return POOL.invoke(new HasOverFork(arr, val, 0, arr.length));
    }

    private static void usage() {
        System.err.println("USAGE: HasOver <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        int val;
        int[] arr;

        try {
            val = Integer.parseInt(args[0]);
            String[] stringArr = args[1].replaceAll("\\s*",  "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                arr[i] = Integer.parseInt(stringArr[i]);
            }
            System.out.println(hasOver(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }

    }
}
