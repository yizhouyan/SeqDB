package interactive.query;

import java.util.LinkedList;

public class WaitingList {
	private short waitingEle;
	private LinkedList<WaitingPattern> waitingPatternList = new LinkedList<WaitingPattern>();

	public WaitingList(short waitingEle, LinkedList<WaitingPattern> waitingPatternList){
		this.waitingEle = waitingEle;
		this.waitingPatternList = waitingPatternList;
	}

	public WaitingList(short waitingEle) {
		this.waitingEle = waitingEle;
	}

	public short getWaitingEle() {
		return waitingEle;
	}

	public void setWaitingEle(short waitingEle) {
		this.waitingEle = waitingEle;
	}

	public LinkedList<WaitingPattern> getWaitingPatternList() {
		return waitingPatternList;
	}

	public void setWaitingPatternList(LinkedList<WaitingPattern> waitingPatternList) {
		this.waitingPatternList = waitingPatternList;
	}

	public void addNewPatternToList(WaitingPattern wp) {
		this.waitingPatternList.add(wp);
	}

}
