package interactive.query;

import interactive.metadata.QueryResult;
import interactive.metadata.QueryResultDisplay;

import java.util.*;

/**
 * Created by yizhouyan on 4/28/18.
 */
public class SequenceScan {
    private short [] sequence;
    private long [] timestamps;
    private short [] query;
    private int itemGap;
    private int seqGap;
    private long itemGapTS;
    private long seqGapTS;
    private ArrayList<Integer> newStartIndexes = new ArrayList<>();
    private HashMap<Short, WaitingList> waitingElements = new HashMap<Short, WaitingList>();

    public SequenceScan(short [] sequence, long [] timestamps, short [] query, int itemGap, int seqGap,
                        long itemGapTS, long seqGapTS){
        this.sequence = sequence;
        this.timestamps = timestamps;
        this.query = query;
        this.itemGap = itemGap;
        this.seqGap = seqGap;
        this.itemGapTS = itemGapTS;
        this.seqGapTS = seqGapTS;
        this.newStartIndexes = new ArrayList<>();
    }

    public QueryResult scanSequenceForMatch(){
        if(query.length == 0)
            return new QueryResult(0);
        else if(query.length == 1)
            return scanForSingleElement();
        else {
            for (int i = 0; i < sequence.length; i++) {
                short curEle = sequence[i];
                checkWaitingPatternLists(curEle, i);
            }
            return new QueryResultDisplay(newStartIndexes.size(), newStartIndexes);
        }
    }

    public QueryResult scanForSingleElement(){
        for(int i = 0; i < sequence.length; i++){
            if(sequence[i] == query[0]){
                this.newStartIndexes.add(i);
            }
        }
        return new QueryResultDisplay(newStartIndexes.size(), newStartIndexes);
    }

    public void checkWaitingPatternLists(short currentElement, int currentIndex) {
        // first check all sequences that are waiting for this element
        if (checkValidationOfWL(currentElement)) {
            LinkedList<WaitingPattern> waitingPatternList = waitingElements.get(currentElement).getWaitingPatternList();
            // need this element again, add to this bag
            LinkedList<WaitingPattern> tempBag = new LinkedList<WaitingPattern>();

            for (WaitingPattern wp : waitingPatternList) {
                // first update waiting pattern to accept this new element
                if (wp.updateWaitingPatternWithNew(currentElement, currentIndex, timestamps,
                        itemGap, seqGap, itemGapTS, seqGapTS)) {
                    if (wp.totalMatch(query.length)) {
                        // do merge
                        this.dealWithTotalMatchWaiting(wp);
                        this.waitingElements.clear();
                        return;
                    } // not deal with split here
                    else {
                        // save to the next list, not this one
                        short nextElement = wp.getNextWaitingElement(query);
                        if (nextElement == currentElement) {
                            tempBag.add(wp);
                        } else {
                            addToWaitingElementList(wp, nextElement);
                        }
                    }
                }
            }
            waitingPatternList.clear();
            waitingPatternList.addAll(tempBag);
        } // end not empty waiting list
        // generate new waiting patterns
        this.generateNewWaitingPatternToList(currentElement, currentIndex);
    }


    public void dealWithTotalMatchWaiting(WaitingPattern wp) {
        ArrayList<Integer> usedIndexes = wp.getMatchedIndexes();
        this.newStartIndexes.add(usedIndexes.get(0));
    }


    public void generateNewWaitingPatternToList(short currentElement, int currentIndex) {
        // add a new WaitlingElement to waitingList
        if(currentElement == query[0]) {
            WaitingPattern wp = new WaitingPattern(currentIndex);
            addToWaitingElementList(wp, query[1]);
        }
    }

    public boolean checkValidationOfWL(short currentItem) {
        if (waitingElements.size() == 0)
            return false;
        if (!waitingElements.containsKey(currentItem))
            return false;
        if (waitingElements.get(currentItem).getWaitingPatternList() == null)
            return false;
        if (waitingElements.get(currentItem).getWaitingPatternList().size() == 0)
            return false;
        return true;
    }

    public void addToWaitingElementList(WaitingPattern wp, short nextElement) {
        if (waitingElements.containsKey(nextElement)) {
            waitingElements.get(nextElement).addNewPatternToList(wp);
        } else {
            waitingElements.put(nextElement, new WaitingList(nextElement));
            waitingElements.get(nextElement).addNewPatternToList(wp);
        }
    }
}
