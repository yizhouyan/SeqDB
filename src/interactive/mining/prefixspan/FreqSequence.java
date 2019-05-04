package interactive.mining.prefixspan;

import java.util.ArrayList;

public class FreqSequence implements Comparable {
	private ArrayList<ResItemArrayPair> itemPairList;

	private boolean isvalid = true;

	private int supportNum = 0;

	private ArrayList<Boolean> validIndexes;

	// only generate if used
	private ArrayList<ArrayList<Integer>> indexesForSequence = new ArrayList<ArrayList<Integer>>();
	
	public ArrayList<ArrayList<Integer>> getIndexesForSequence(){
		return indexesForSequence;
	}
	public boolean isEmptyIndexesForSequence(){
		return indexesForSequence.isEmpty();
	}
	public void generateIndexesForSequence(){
		for(int i = 0; i< supportNum; i++){
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			for(ResItemArrayPair resPair: itemPairList){
				tempList.add(resPair.index.get(i));
			}
			indexesForSequence.add(tempList);
		}
	}
	public ArrayList<ResItemArrayPair> getItemPairList() {
		return itemPairList;
	}

	public void setItemPairList(ArrayList<ResItemArrayPair> itemPairList) {
		this.itemPairList = itemPairList;
	}

	public FreqSequence() {
		itemPairList = new ArrayList<ResItemArrayPair>();
	}

	public FreqSequence(ArrayList<ResItemArrayPair> itemPairList) {
		this.itemPairList = itemPairList;
	}

	public void addItemToSequence(ResItemArrayPair newLetter) {
		itemPairList.add(newLetter);
	}

	/**
	 * before doing pruning (that is , if ABC is frequent, then AB AC BC should
	 * remove the support which appears the same position with ABC) We have to
	 * generate total number of support for each frequent sequence For each
	 * support, we set up an index indicating whether it is valid or not
	 */
	public void generateSupportNumAndInitIndexes() {
		this.supportNum = this.itemPairList.get(0).index.size();
		this.validIndexes = new ArrayList<Boolean>();
		for (int i = 0; i < supportNum; i++) {
			this.validIndexes.add(true);
		}
	}

	public int getItemNumInFreqSeq(){
		return this.itemPairList.size();
	}
	public String getFreqSeqInString() {
		String str = "";
		for (ResItemArrayPair curItem : this.itemPairList) {
			str += curItem.item + ",";
		}
		if(str.length()>0)
			str = str.substring(0, str.length()-1);
		return str;
	}

	public short[] getFreqSeqInShortArray() {
		short [] result = new short[itemPairList.size()];
		for(int i = 0; i< itemPairList.size(); i++){
			result[i] = itemPairList.get(i).item;
		}
		return result;
	}

	/**
	 * copy a frequent sequence
	 * 
	 * @return
	 */
	public FreqSequence copyFreqSeqence(ArrayList<Boolean> ifHasNewItemInPrevSeq) {
		FreqSequence copySeq = new FreqSequence();

		for (ResItemArrayPair itemPair : this.itemPairList) {
			ArrayList<Integer> deleteItemSets = new ArrayList<>();
			ResItemArrayPair tempItemPair;
			short tempLetter = itemPair.item;
			ArrayList<Integer> tempIndexes = new ArrayList<Integer>(itemPair.index);
			for (int i = 0; i < ifHasNewItemInPrevSeq.size(); i++) {
				if (!ifHasNewItemInPrevSeq.get(i))
					deleteItemSets.add(tempIndexes.get(i));
			}
			tempIndexes.removeAll(deleteItemSets);
			tempItemPair = new ResItemArrayPair(tempLetter, tempIndexes);
			copySeq.itemPairList.add(tempItemPair);
		}

		return copySeq;
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public int getSupportNum() {
		return supportNum;
	}

	public void setSupportNum(int supportNum) {
		this.supportNum = supportNum;
	}

	public ArrayList<Boolean> getValidIndexes() {
		return validIndexes;
	}

	public void setValidIndexes(ArrayList<Boolean> validIndexes) {
		this.validIndexes = validIndexes;
	}

	@Override
	public int compareTo(Object o) {
		if (this.getItemNumInFreqSeq() > ((FreqSequence) o).getItemNumInFreqSeq())
			return -1;
		else if (this.getItemNumInFreqSeq() < ((FreqSequence) o).getItemNumInFreqSeq())
			return 1;
		else
			return 0;
	}

	public void setInvalid() {
		this.isvalid = false;
	}
	public boolean isInvalid(int minSupport){
		if(isvalid == false)
			return true;
		int count = 0;
		for(Boolean i: this.validIndexes)
			if(i == true)
				count++;
		if(count < minSupport){
			isvalid = false;
			return true;
		}
		return false;
	}
	public boolean isSubArray(ArrayList<Integer> longArray, ArrayList<Integer> shortArray){
		for(int shortItem: shortArray){
			int startPos = longArray.indexOf(shortItem);
			if(startPos < 0)
				return false;
		}
		return true;
	}
	public void setPartInValid(FreqSequence longSeq) {
		// for each long sequence, set the part with the same index invalid
		// first find which position to check, eg: longSeq: ABC currentSeq: AC,
		// return {0,2}
		if(longSeq.isEmptyIndexesForSequence())
			longSeq.generateIndexesForSequence();
		if(this.isEmptyIndexesForSequence())
			this.generateIndexesForSequence();
		for(ArrayList<Integer> longArrayForIndexes: longSeq.getIndexesForSequence()){
			for(int i = 0; i< validIndexes.size(); i++){
				if(validIndexes.get(i) == true && isSubArray(longArrayForIndexes, this.indexesForSequence.get(i)))
					validIndexes.set(i, false);
			}
		}
		
	}
}
