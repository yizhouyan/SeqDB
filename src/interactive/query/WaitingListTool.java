package interactive.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by yizhouyan on 9/10/18.
 */
public class WaitingListTool {
    public static int checkWaitingPatternLists(HashMap<Short, WaitingList> waitingElements, short [] queryPattern,
                                                short [] originalSeq, long [] originalTS,
                                                short currentElement, int currentIndex, int itemGap, int seqGap,
                                                long itemGapTS, long seqGapTS) {
        // first check all sequences that are waiting for this element
        if (checkValidationOfWL(waitingElements, currentElement)) {
            LinkedList<WaitingPattern> waitingPatternList = waitingElements.get(currentElement).getWaitingPatternList();
            // need this element again, add to this bag
            LinkedList<WaitingPattern> tempBag = new LinkedList<WaitingPattern>();

            for (WaitingPattern wp : waitingPatternList) {
                // first update waiting pattern to accept this new element
                if (wp.updateWaitingPatternWithNew(currentElement, currentIndex, originalTS,
                        itemGap, seqGap, itemGapTS, seqGapTS)) {
                    if (wp.totalMatch(queryPattern.length)) {
                        // do merge
                        return dealWithTotalMatchWaiting(wp);
                    } // not deal with split here
                    else {
                        // save to the next list, not this one
                        short nextElement = wp.getNextWaitingElement(queryPattern);
                        if (nextElement == currentElement) {
                            tempBag.add(wp);
                        } else {
                            addToWaitingElementList(waitingElements, wp, nextElement);
                        }
                    }
                }
            }
            waitingPatternList.clear();
            waitingPatternList.addAll(tempBag);
        } // end not empty waiting list
        // generate new waiting patterns
        generateNewWaitingPatternToList(waitingElements, currentElement, currentIndex, queryPattern);
        return -1;
    }


    public static int dealWithTotalMatchWaiting(WaitingPattern wp) {
        ArrayList<Integer> usedIndexes = wp.getMatchedIndexes();
        return usedIndexes.get(0);
    }


    public static void generateNewWaitingPatternToList(HashMap<Short, WaitingList> waitingElements,
                                                short currentElement, int currentIndex, short[] query) {
        // add a new WaitlingElement to waitingList
        if(currentElement == query[0]) {
            WaitingPattern wp = new WaitingPattern(currentIndex);
            addToWaitingElementList(waitingElements, wp, query[1]);
        }
    }

    public static boolean checkValidationOfWL(HashMap<Short, WaitingList> waitingElements,short currentItem) {
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

    public static void addToWaitingElementList(HashMap<Short, WaitingList> waitingElements, WaitingPattern wp, short nextElement) {
        if (waitingElements.containsKey(nextElement)) {
            waitingElements.get(nextElement).addNewPatternToList(wp);
        } else {
            waitingElements.put(nextElement, new WaitingList(nextElement));
            waitingElements.get(nextElement).addNewPatternToList(wp);
        }
    }
}
