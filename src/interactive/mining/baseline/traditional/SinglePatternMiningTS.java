package interactive.mining.baseline.traditional;

import interactive.mining.baseline.maximum.FreqSequence;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;

import java.util.*;

public class SinglePatternMiningTS {
	private InputSequenceWithTS inputSequence;
	private LocalParameterSpaceWithTS localParameterSpace;

	private ArrayList<FreqSequence> totalFrequentSeqs;
	private HashMap<ArrayList<String>, FreqSequence> FreqSeqsInMap;

	public SinglePatternMiningTS(InputSequenceWithTS inputSequence, LocalParameterSpaceWithTS localParameterSpace) {
	    this.inputSequence = inputSequence;
	    this.localParameterSpace = localParameterSpace;

		this.totalFrequentSeqs = new ArrayList<FreqSequence>();
		this.FreqSeqsInMap = new HashMap<ArrayList<String>, FreqSequence>();
	}

	/**
	 * This one is the current version that excludes more (results == long to
	 * short algorithm)
	 *
	 * @return
	 */
	public void findFreqSeqInOneString(HashSet<String> globalFrequentElements) {
		// first compute frequent sequences
		long start = System.currentTimeMillis();

		PrefixSpanTool pst = new PrefixSpanTool(inputSequence, localParameterSpace);
		totalFrequentSeqs = pst.prefixSpanCalculate(globalFrequentElements);
//		return FreqSeqsInMap.keySet();
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

		SinglePatternMiningTS obj = new SinglePatternMiningTS(inputSequence, localParameterSpace);

		String[] globalFE = {"A","B","C","D"};
		HashSet<String> globalFrequentElements = new HashSet(Arrays.asList(globalFE));
        obj.findFreqSeqInOneString(globalFrequentElements);
	}
}
