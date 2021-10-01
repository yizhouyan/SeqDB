package interactive.mining.baseline.traditional;

import interactive.metadata.FreqPatternWrapup;
import interactive.metadata.SingleSeqWrapup;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;
import interactive.mining.baseline.top.utils.FileUtile;
import interactive.mining.baseline.top.utils.Toolbox;
import interactive.mining.prefixspan.FreqSequence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FSDetection {
	private LocalParameterSpaceWithTS localParameterSpace;
	private ArrayList<String> inputStringArray;

	public FSDetection(int minSupportLocal, int itemGap, int seqGap, long itemInterval, long sequenceInterval,
					   List<String> inputStringArray) {
		localParameterSpace = new LocalParameterSpaceWithTS(minSupportLocal, itemGap, seqGap, itemInterval, sequenceInterval);
		this.inputStringArray = (ArrayList<String>) inputStringArray;
	}

	/**
	 * deal with each String and generate frequent sequence
	 *
	 */
	public long generateLocalFrequentSequences(HashSet<String> globalFrequentElements) {
		long startTime = System.currentTimeMillis();
		HashMap<Integer, ArrayList<String>> localFreqPatterns = new HashMap<>();
		int count = 0;
		for (int i = 0; i < inputStringArray.size(); i++) {
			String inputStr = inputStringArray.get(i);
			count++;
			InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
			SinglePatternMiningTS obj = new SinglePatternMiningTS(inputSequence, localParameterSpace);
			ArrayList<String> curFreqPatterns = obj.findFreqSeqInOneString(globalFrequentElements);
			localFreqPatterns.put(i, curFreqPatterns);
			if(count % 1 == 0)
				System.out.println(count + " Finished" + i + " Proceessed");
//				System.out.println(i);
		}
		ComputeGlobalFrequentPatterns(localFreqPatterns);
//		System.out.println("Compute Global Frequent Sequence takes " + (System.currentTimeMillis() - startTime) / 1000
//				+ " seconds!");
		return (System.currentTimeMillis() - startTime);
	}

	public void ComputeGlobalFrequentPatterns(HashMap<Integer, ArrayList<String>> localFreqPatterns) {
		int globalSupport = 50;
		HashMap<String, Integer> globalFreqPattern = new HashMap<>();
		for (ArrayList<String> patternsForOne : localFreqPatterns.values()) {
			for (String pattern : patternsForOne) {
				if (globalFreqPattern.containsKey(pattern))
					globalFreqPattern.put(pattern, globalFreqPattern.get(pattern) + 1);
				else {
					globalFreqPattern.put(pattern, 1);
				}
			}
		}
		HashMap<String, Integer> finalGlobalFreqPattern = new HashMap<>();
		for (Map.Entry<String, Integer> pattern : globalFreqPattern.entrySet()) {
			if (pattern.getValue() >= globalSupport) {
				finalGlobalFreqPattern.put(pattern.getKey(), pattern.getValue());
			}
		}
		System.out.println("Size of Global Frequent Pattern: " + finalGlobalFreqPattern.size());


		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("globalFreqPattern"));
			for (String s : finalGlobalFreqPattern.keySet()) {
				writer.write(s + "\t" + finalGlobalFreqPattern.get(s));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public ArrayList<String> getInputStringArray() {
		return inputStringArray;
	}

	public void setInputStringArray(ArrayList<String> inputStringArray) {
		this.inputStringArray = inputStringArray;
	}

	public static void main(String[] args) {
//		String inputPath = "../data/realdata/CT_data_part_timestamp.csv";
//		String inputPath = "../data/realdata/logstream.csv";
		String inputPath = args[0];
		int globalMinSupport = 100;
		int itemgap = 0;
		int seqGap = 10;
		long itemInterval = 5000;
		long seqInterval = 60000;

		int LocalMinSupport = 50;
		System.out.println("Local Support: " + LocalMinSupport);
		ArrayList<String> inputStringArray = FileUtile.readInDataset(inputPath);
		System.out.println(inputStringArray.size());

		HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, globalMinSupport,
				LocalMinSupport);
		System.out.println(globalFrequentElements.size());
		System.out.println(globalFrequentElements.toString());
		FSDetection globalFS = new FSDetection(LocalMinSupport, itemgap, seqGap, itemInterval, seqInterval,
				inputStringArray);
		long curTime = globalFS.generateLocalFrequentSequences(globalFrequentElements);
		System.out.println("Maximum Pattern Mining takes " + curTime *1.0 /1000 + " seconds!");
	}

}
