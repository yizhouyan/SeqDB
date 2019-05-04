package interactive.mining.prefixspan;

public class ItemPair {
	// save each item in the sequence
	private short item;
	// save the global index in previous time sequence pattern
	private int index;
	
	public ItemPair(short item, int index){
		this.setItem(item);
		this.setIndex(index);
	}

	public short getItem() {
		return item;
	}

	public void setItem(short item) {
		this.item = item;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
  
}
