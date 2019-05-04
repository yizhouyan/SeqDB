package interactive.query;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WaitingPattern {
	private ArrayList<Integer> matchedIndexes;
	private int currentPos;

	public WaitingPattern(int firstIndex) {
		this.matchedIndexes = new ArrayList<>();
		this.matchedIndexes.add(firstIndex);
		currentPos = 0;
	}

	public boolean totalMatch(int matchPatternLength) {
		if (currentPos == matchPatternLength - 1)
			return true;
		else
			return false;
	}

	public short getNextWaitingElement(short[] matchPattern) {
		return matchPattern[currentPos + 1];
	}

	public boolean updateWaitingPatternWithNew(short newElement, int currentIndex, long [] timestamps, int itemGap,
											   int seqGap, long itemGapTS, long seqGapTS) {
		if ((currentIndex - matchedIndexes.get(0) > seqGap + 1)
				|| (currentIndex - matchedIndexes.get(matchedIndexes.size()-1) > itemGap + 1)
				|| (timestamps[currentIndex] - timestamps[matchedIndexes.get(0)] > seqGapTS + 1)
				|| (timestamps)[currentIndex] - timestamps[matchedIndexes.get(matchedIndexes.size()-1)] > itemGapTS + 1){
			// cannot accept this new pattern because it violates the gap constraint
			return false;
		}
		this.currentPos++;
		this.matchedIndexes.add(currentIndex);
		return true;
	}
	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}

	public ArrayList<Integer> getMatchedIndexes() {return this.matchedIndexes; }
}
