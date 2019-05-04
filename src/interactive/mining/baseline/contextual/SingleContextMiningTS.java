package interactive.mining.baseline.contextual;

import interactive.mining.baseline.close.PrefixSpanToolClose;
import interactive.mining.baseline.maximum.FreqSequence;
import interactive.mining.baseline.maximum.ResItemArrayPair;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;

import java.util.*;

public class SingleContextMiningTS {
	private InputSequenceWithTS inputSequence;
	private LocalParameterSpaceWithTS localParameterSpace;

	private ArrayList<FreqSequence> totalFrequentSeqs;
	private HashMap<String, FreqSequence> FreqSeqsInMap;
	private boolean[] available;

	public SingleContextMiningTS(InputSequenceWithTS inputSequence, LocalParameterSpaceWithTS localParameterSpace) {
		this.inputSequence = inputSequence;
		this.localParameterSpace = localParameterSpace;

		this.totalFrequentSeqs = new ArrayList<FreqSequence>();
		this.FreqSeqsInMap = new HashMap<String, FreqSequence>();
		this.available = new boolean[this.inputSequence.getInputString().split(",").length];
		for (int i = 0; i < this.available.length; i++) {
			available[i] = false;
		}
	}

	public void updateSequenceLongToShort() {
		if (totalFrequentSeqs.size() == 0)
			return;
		int currentLength = totalFrequentSeqs.get(0).getItemNumInFreqSeq();
		int currentIndexInFS = 0;
		while (currentLength > 1 && currentIndexInFS < this.totalFrequentSeqs.size()) {

			// deal with all fs with size = currentLength;
			HashSet<Integer> tempUnavailableIndexes = new HashSet<Integer>();

			while (currentIndexInFS < this.totalFrequentSeqs.size()
					&& this.totalFrequentSeqs.get(currentIndexInFS).getItemNumInFreqSeq() == currentLength) {
				// check each index to see its new support value, if support >=
				// minSupport
				FreqSequence curFSObj = this.totalFrequentSeqs.get(currentIndexInFS);
				ArrayList<ResItemArrayPair> indexesForCurFS = curFSObj.getItemPairList();
				int curFinalSupport = 0;
				int curSupportTotal = indexesForCurFS.get(0).index.size();
				for (int i = 0; i < curSupportTotal; i++) {
					for (int j = 0; j < indexesForCurFS.size(); j++) {
						if (this.available[indexesForCurFS.get(j).index.get(i)]) {
							curFinalSupport++;
							break;
						}
					}
				}
//				System.out.println(curFSObj.getFreqSeqInString() + "\t" + curFinalSupport);
				// add to results and add all indexes into tempUnavailable
				if (curFinalSupport >= localParameterSpace.getMinLocalSupport()) {
					this.FreqSeqsInMap.put(curFSObj.getFreqSeqInString(), curFSObj);
					curFSObj.setSupportNum(curFinalSupport);
					for (ResItemArrayPair resultPair : indexesForCurFS) {
						for (Integer deleteIndex : resultPair.index) {
							tempUnavailableIndexes.add(deleteIndex);
						}
					}
				}
				currentIndexInFS++;
			}

			for (Integer ii : tempUnavailableIndexes) {
				this.available[ii] = false;
			}
			if (currentIndexInFS < this.totalFrequentSeqs.size())
				currentLength = this.totalFrequentSeqs.get(currentIndexInFS).getItemNumInFreqSeq();
			else
				break;
		}
	}


	public Set<String> findFreqSeqInOneString(HashSet<String> globalFrequentElements) {
		// first compute frequent sequences
		long start = System.currentTimeMillis();
		PrefixSpanToolClose pst = new PrefixSpanToolClose(inputSequence, localParameterSpace);
		totalFrequentSeqs = pst.prefixSpanCalculate(globalFrequentElements);
		// then sort by length of the string
		Collections.sort(totalFrequentSeqs);
		for (FreqSequence fs : totalFrequentSeqs) {
			for (ResItemArrayPair resultItemPair : fs.getItemPairList()) {
				for (Integer tempIndex : resultItemPair.index) {
					this.available[tempIndex] = true;
				}
			}
		}
		// init support number and a list of boolean for each sequence
		this.updateSequenceLongToShort();
		return FreqSeqsInMap.keySet();
	}



	public static void main(String[] args) {
		// String s = "A,B,C,A,B,C,A,B,A,C,B,A,B";
		String s= "A|1473902373698,B|1473902433758,C|1473902433815,A|1473902433843,B|1473902485507,C|1473902485507,A|1473902485507,C|1473902485512,B|1473902485512,C|1473902485532," +
				"A|1473902485574,C|1473902485628,B|1473902485633,A|1473902486252,C|1473902487718,D|1473902487072";
		int localSupport = 2;
		int itemGap = 0;
		int seqGap = 10;
		long itemInterval = 10000;
		long sequenceInterval = 60000;
		LocalParameterSpaceWithTS localParameterSpace = new LocalParameterSpaceWithTS(localSupport, itemGap, seqGap, itemInterval, sequenceInterval);
		InputSequenceWithTS inputSequence = new InputSequenceWithTS(s);

		SingleContextMiningTS obj = new SingleContextMiningTS(inputSequence, localParameterSpace);

		String[] globalFE = {"A","B","C","D"};
		HashSet<String> globalFrequentElements = new HashSet(Arrays.asList(globalFE));
		Set<String> freqPatterns = obj.findFreqSeqInOneString(globalFrequentElements);
		System.out.println(freqPatterns);
	}
}
