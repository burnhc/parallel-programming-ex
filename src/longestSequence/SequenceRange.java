package longestSequence;

public class SequenceRange {
    public int matchingOnLeft, matchingOnRight;
    public int longestRange, sequenceLength;

    public SequenceRange(int left, int right, int longest, int length) {
        this.matchingOnLeft = left;
        this.matchingOnRight = right;
        this.longestRange = longest;
        this.sequenceLength = length;
    }
}
