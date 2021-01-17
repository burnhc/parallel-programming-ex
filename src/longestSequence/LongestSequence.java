package longestSequence;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class LongestSequence {
    public static int SEQUENTIAL_CUTOFF;
    public static ForkJoinPool POOL = new ForkJoinPool();

    static class LongestSequenceFork extends RecursiveTask<SequenceRange> {
        int[] arr;
        int val, lo, hi;

        public LongestSequenceFork(int[] arr, int val, int lo, int hi) {
            this.arr = arr;
            this.val = val;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected SequenceRange compute() {
            if (hi - lo <= SEQUENTIAL_CUTOFF) {
                return getMaxRange(arr, val, lo, hi);

            } else {
                int mid = lo + (hi - lo) / 2;
                LongestSequenceFork left = new LongestSequenceFork(arr, val, lo, mid);
                LongestSequenceFork right = new LongestSequenceFork(arr, val, mid, hi);
                left.fork();
                SequenceRange rightSeq = right.compute();
                SequenceRange leftSeq = left.join();

                int combinedMatchOnRight = rightSeq.matchingOnRight;
                int combinedMatchOnLeft = leftSeq.matchingOnLeft;
                int combinedSeqLength = rightSeq.sequenceLength + leftSeq.sequenceLength;

                // combines right side with left side if there is no gap
                // between the right sequence for a cumulative right sequence
                if (rightSeq.matchingOnRight == rightSeq.sequenceLength) {
                    combinedMatchOnRight += leftSeq.matchingOnRight;
                }

                // combines left side with right side if there is no gap
                // between the left sequence for a cumulative left sequence
                if (leftSeq.matchingOnLeft == leftSeq.sequenceLength) {
                    combinedMatchOnLeft += rightSeq.matchingOnLeft;
                }

                int combinedLongestRange = Math.max(
                                           Math.max(leftSeq.matchingOnRight + rightSeq.matchingOnLeft,
                                           Math.max(leftSeq.longestRange, rightSeq.longestRange)),
                                           Math.max(combinedMatchOnRight, combinedMatchOnLeft));

                return new SequenceRange(combinedMatchOnLeft, combinedMatchOnRight,
                                         combinedLongestRange, combinedSeqLength);
            }
        }

        private SequenceRange getMaxRange(int[] arr, int val, int lo, int hi) {
            int count = 0;
            int maxCount = 0;
            int countFromLeft = 0;
            int countFromRight = 0;

            // Get longest sequence in between range
            for (int i = lo; i < hi; i++) {
                if (arr[i] == val) {
                    count++;
                } else {
                    // Else branch may not trigger
                    maxCount = Math.max(count, maxCount);
                    count = 0;
                }
            }

            // Get count from the lower bound
            for (int i = lo; i < hi; i++) {
                if (arr[i] == val) {
                    countFromLeft++;
                } else {
                    break;
                }
            }

            // Get count from the upper bound
            for (int i = hi - 1; i >= lo; i--) {
                if (arr[i] == val) {
                    countFromRight++;
                } else {
                    break;
                }
            }

            // Get maximum of the three values
            maxCount = Math.max(maxCount,
                       Math.max(countFromLeft, countFromRight));

            return new SequenceRange(countFromLeft,
                    countFromRight, maxCount, hi - lo);
        }
    }

    public static int getLongestSequence(int val, int[] arr, int sequentialCutoff) {
        SEQUENTIAL_CUTOFF = sequentialCutoff;
        SequenceRange range = POOL.invoke(new LongestSequenceFork(arr, val, 0, arr.length));
        return range.longestRange;
    }

    private static void usage() {
        System.err.println("USAGE: LongestSequence <number> <array> <sequential cutoff>");
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
            System.out.println(getLongestSequence(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}