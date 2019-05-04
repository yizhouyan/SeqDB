package interactive.mining.baseline.close;

import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;
import interactive.mining.baseline.top.utils.FileUtile;
import interactive.mining.baseline.top.utils.Toolbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CloseFSDetection {
	private LocalParameterSpaceWithTS localParameterSpace;
	private ArrayList<String> inputStringArray;

	public CloseFSDetection(int minSupportLocal, int itemGap, int seqGap, long itemInterval, long sequenceInterval,
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
		for (int i = 0; i < inputStringArray.size(); i++) {
			String inputStr = inputStringArray.get(i);
			InputSequenceWithTS inputSequence = new InputSequenceWithTS(inputStr);
			SingleClosePatternMiningTS obj = new SingleClosePatternMiningTS(inputSequence, localParameterSpace);
			obj.findFreqSeqInOneString(globalFrequentElements);
			if(i % 10 == 0)
				System.out.println(i + " Finished");
//				System.out.println(i);
		}
//		System.out.println("Compute Global Frequent Sequence takes " + (System.currentTimeMillis() - startTime) / 1000
//				+ " seconds!");
		return (System.currentTimeMillis() - startTime);
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
		int globalMinSupport = 1;
		int itemgap = 0;
		int seqGap = Integer.parseInt(args[2]);
		long itemInterval = 5000;
		long seqInterval = 60000;

		int LocalMinSupport = Integer.parseInt(args[1]);
		System.out.println("Local Support: " + LocalMinSupport);
		ArrayList<String> inputStringArray = FileUtile.readInDataset(inputPath);
		System.out.println(inputStringArray.size());

		HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, globalMinSupport,
				LocalMinSupport);
		System.out.println(globalFrequentElements.size());
		System.out.println(globalFrequentElements.toString());
		CloseFSDetection globalFS = new CloseFSDetection(LocalMinSupport, itemgap, seqGap, itemInterval, seqInterval,
				inputStringArray);
		long curTime = globalFS.generateLocalFrequentSequences(globalFrequentElements);
		System.out.println("Maximum Pattern Mining takes " + curTime *1.0 /1000 + " seconds!");
	}

}
