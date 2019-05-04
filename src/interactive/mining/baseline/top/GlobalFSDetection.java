package interactive.mining.baseline.top;


import interactive.mining.baseline.top.core.lts.local.noviolation.LocalFSDetection;
import interactive.mining.baseline.top.core.lts.local.noviolation.LocalFSDetectionWithTimeStamp;
import interactive.mining.baseline.top.inputs.InputSequence;
import interactive.mining.baseline.top.inputs.InputSequenceWithTS;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpace;
import interactive.mining.baseline.top.parameterspace.LocalParameterSpaceWithTS;
import interactive.mining.baseline.top.utils.FileUtile;
import interactive.mining.baseline.top.utils.Toolbox;

import javax.ejb.Local;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GlobalFSDetection {
	private LocalParameterSpace localParameterSpace;
	private ArrayList<String> inputStringArray;

	public GlobalFSDetection(int minSupportLocal, int itemGap, int seqGap,long itemInterval, long sequenceInterval,
			List<String> inputStringArray) {
		localParameterSpace = new LocalParameterSpaceWithTS(minSupportLocal, itemGap, seqGap, itemInterval, sequenceInterval);
		this.inputStringArray = (ArrayList<String>) inputStringArray;
	}

	/**
	 * deal with each String and generate frequent sequence
	 *
	 */
	public long generateLocalFrequentSequences(HashSet<String> globalFrequentElements) {
//		try {
//			BufferedWriter out = new BufferedWriter(
//					new FileWriter(new File("localFS-lts-" + localParameterSpace.getMinLocalSupport() + ".txt")));
		long startTime = System.currentTimeMillis();
			for (int i = 0; i < inputStringArray.size(); i++) {
				String inputStr = inputStringArray.get(i);
				InputSequence inputSequence = new InputSequenceWithTS(inputStr);
				LocalFSDetectionWithTimeStamp localFS = new LocalFSDetectionWithTimeStamp(localParameterSpace, inputSequence);
				localFS.Initialization(globalFrequentElements);
				HashMap<String, Integer> tempLocalFS = localFS.LocalFrequentSequenceMining(i);
//				System.out.println(tempLocalFS.size());
//				out.write("#" + i);
//				out.newLine();
//				for (String oneStr : tempLocalFS.keySet()) {
//					out.write(oneStr + "\t" );
//				}
//				out.newLine();
			}
		System.out.println("Compute Global Frequent Sequence takes " + (System.currentTimeMillis() - startTime) / 1000
				+ " seconds!");
			return (System.currentTimeMillis() - startTime);
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public ArrayList<String> getInputStringArray() {
		return inputStringArray;
	}

	public void setInputStringArray(ArrayList<String> inputStringArray) {
		this.inputStringArray = inputStringArray;
	}

	public static void main(String[] args) {
//		String inputPath = "../data/realdata/CT_data_part_timestamp.csv";
		String inputPath = "../data/realdata/logstream.csv";
		int globalMinSupport = 1;
		int itemgap = 0;
		int seqGap = 10;
		long itemInterval = 5000;
		long seqInterval = 60000;
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		long sumTime = 0;
		for(int i = 2; i< 30; i++){
			int LocalMinSupport = i;
			System.out.println("Local Support: " + LocalMinSupport);
			ArrayList<String> inputStringArray = FileUtile.readInDataset(inputPath);
			System.out.println(inputStringArray.size());

			HashSet<String> globalFrequentElements = Toolbox.getGlobalFrequentElements(inputStringArray, globalMinSupport,
					LocalMinSupport);
			System.out.println(globalFrequentElements.size());
			System.out.println(globalFrequentElements.toString());
			GlobalFSDetection globalFS = new GlobalFSDetection(LocalMinSupport, itemgap, seqGap, itemInterval, seqInterval,
					inputStringArray);
			long curTime = globalFS.generateLocalFrequentSequences(globalFrequentElements);
			minTime = Math.min(curTime, minTime);
			maxTime = Math.max(curTime, maxTime);
			sumTime += curTime;
		}
		System.out.println("Min Time: " + minTime * 1.0 /1000);
		System.out.println("Max Time: " + maxTime * 1.0 /1000);
		System.out.println("Sum Time: " + sumTime * 1.0/ 1000);
		System.out.println("Average Time: " + sumTime * 1.0/(1000*28));
	}

}
