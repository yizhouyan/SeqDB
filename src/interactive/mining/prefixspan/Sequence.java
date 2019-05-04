package interactive.mining.prefixspan;

import java.util.ArrayList;

public class Sequence {
	// a sequence is an array of itempairs
	private ArrayList<ItemPair> itemPairList;

	public Sequence() {
		itemPairList = new ArrayList<ItemPair>();
	}

	public Sequence(ArrayList<ItemPair> itemPairList) {

		this.itemPairList = itemPairList;
	}

	public ArrayList<ItemPair> getItemPairList() {
		return itemPairList;
	}

	public void setItemPairList(ArrayList<ItemPair> itemPairList) {
		this.itemPairList = (ArrayList<ItemPair>) itemPairList.clone();
	}

	/**
	 * check if the sequence contains the given string
	 *
	 * @return
	 */
	public boolean strIsContained(short checkStr, ArrayList<Integer> tempFS, boolean addToFS) {
		boolean isContained = false;
		ArrayList<Integer> tempIndexes = new ArrayList<Integer>();

		for (ItemPair itemPair : itemPairList) {
			isContained = false;
			if (checkStr == itemPair.getItem()) {
				isContained = true;
				if (addToFS)
					tempFS.add(itemPair.getIndex());
				break;
			}
		}
		return isContained;
	}

	/**
	 * check if the sequence contains the given string
	 * 
	 * @param checkStr
	 *            given string
	 * @return
	 */
	public boolean strIsContained(short checkStr, ArrayList<Integer> tempFS, boolean addToFS, int previousIndex,
                                  int firstIndex, long [] timestamps, int itemGap, int seqGap, long itemGapTS, long seqGapTS) {
		boolean isContained = false;
		ArrayList<Integer> tempIndexes = new ArrayList<Integer>();

		for (ItemPair itemPair : itemPairList) {
			isContained = false;
			if (itemPair.getIndex() - previousIndex > itemGap +1 ||
					itemPair.getIndex() - firstIndex > seqGap + 1 ||
					timestamps[itemPair.getIndex()]-timestamps[previousIndex] > itemGapTS + 1 ||
					timestamps[itemPair.getIndex()]-timestamps[firstIndex] > seqGapTS + 1)
				break;
			if (checkStr == itemPair.getItem()) {
				isContained = true;
				if (addToFS)
					tempFS.add(itemPair.getIndex());
				break;
			}
		}
		return isContained;
	}

	/**
	 * the new sequence after string "s"
	 * 
	 * @param s
	 *            extract items after string s
	 */
	public Sequence extractItem(short s) {
		Sequence extractSeq = this.copySeqence();
		ArrayList<ItemPair> deleteItemSets = new ArrayList<>();
		ArrayList<String> tempItems = new ArrayList<>();

		for (int k = 0; k < extractSeq.itemPairList.size(); k++) {
			ItemPair itemPair = extractSeq.itemPairList.get(k);
			if (itemPair.getItem() == s) {
				extractSeq.itemPairList.remove(k);
				break;
			} else {
				deleteItemSets.add(itemPair);
			}
		}
		extractSeq.itemPairList.removeAll(deleteItemSets);
		return extractSeq;
	}

	/**
	 * copy a sequence
	 * 
	 * @return
	 */
	public Sequence copySeqence() {
		Sequence copySeq = new Sequence();
		ItemPair tempItemPair;

		for (ItemPair itemPair : this.itemPairList) {
			tempItemPair = new ItemPair(itemPair.getItem(), itemPair.getIndex());
			copySeq.getItemPairList().add(tempItemPair);
		}

		return copySeq;
	}

	// /**
	// * check sub array
	// *
	// * @param strList1
	// * @param strList2
	// * @return
	// */
	// public boolean strArrayContains(ArrayList<String> strList1,
	// ArrayList<String> strList2) {
	// boolean isContained = false;
	//
	// for (int i = 0; i < strList1.size() - strList2.size() + 1; i++) {
	// isContained = true;
	//
	// for (int j = 0, k = i; j < strList2.size(); j++, k++) {
	// if (!strList1.get(k).equals(strList2.get(j))) {
	// isContained = false;
	// break;
	// }
	// }
	//
	// if (isContained) {
	// break;
	// }
	// }
	//
	// return isContained;
	// }
}
