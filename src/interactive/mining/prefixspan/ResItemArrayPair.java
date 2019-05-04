package interactive.mining.prefixspan;

import java.util.ArrayList;

public class ResItemArrayPair {
	// save each letter in the sequence
	public short item;
	// save the global index list in previous time sequence pattern
	public ArrayList<Integer> index;

	public ResItemArrayPair(short letter) {
		this.item = letter;
		this.index = new ArrayList<Integer>();
	}
	public ResItemArrayPair(short letter, ArrayList<Integer> index){
		this.item = letter;
		this.index = index;
	}
	public void setIndexes(ArrayList<Integer> index){
		this.index.addAll(index);
	}
	public void addToIndex(int tempIndex) {
		index.add(tempIndex);
	}
}
