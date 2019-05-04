package interactive.generatedatasets;


import interactive.util.StdRandom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class GenerateDevices {
	private int sequenceLength = 30000;
	private int numDevices = 100;
	private int globalSupport = 50;
	private int localSupport = 5;
	private String[] devices;
	private int[] numElementsInDevices;
	private boolean verbose = false;

	public GenerateDevices(int numDevices, int localSupport, int globalSupport, int sequenceLength, boolean verbose) {
		this.numDevices = numDevices;
		this.localSupport = localSupport;
		this.verbose = verbose;
		this.globalSupport = globalSupport;
		this.sequenceLength = sequenceLength;
		this.devices = new String[this.numDevices];
		for (int i = 0; i < this.numDevices; i++) {
			this.devices[i] = "";
		}
		this.numElementsInDevices = new int[this.numDevices];
	}


	public void addFrequentPatterns(ArrayList<String> frequentSequences) {
		int numFreqSeqs = frequentSequences.size();
		for (String currentFreqSeq : frequentSequences) {
			int currentFSLength = currentFreqSeq.split(",").length;
			// first randomly setup indexes that contains |mGS| device ids
			HashSet<Integer> usedDevices = new HashSet<Integer>();

			while (usedDevices.size() < this.globalSupport) {
				int deviceId = StdRandom.random.nextInt(numDevices);
				if (!usedDevices.contains(deviceId)) {
					usedDevices.add(deviceId);
//					int localS = StdRandom.randomNumber(this.localSupport, this.localSupport * 3);
					int localS = this.localSupport;
					for (int i = 0; i < localS; i++) {
						int pos = 0;
						if (devices[deviceId].contains("|")) {
							int indexOfBar = -1;
//							System.out.println(devices[deviceId]);
							do {
								int randomNum = StdRandom.random.nextInt(devices[deviceId].length());
								indexOfBar = devices[deviceId].indexOf("|", randomNum);
//								System.out.println(indexOfBar);
							} while (indexOfBar == -1);
							pos = indexOfBar + 1;
						}
						this.devices[deviceId] = this.devices[deviceId].substring(0, pos) + currentFreqSeq + "|"
								+ this.devices[deviceId].substring(pos, this.devices[deviceId].length());
						// this.devices[deviceId] += currentFreqSeq + ",";
						this.numElementsInDevices[deviceId] += currentFSLength;
					}
				}
			}
		}

		if (verbose) {
			System.out.println("Add frequent sequences");
			this.printCurrentDevices();
		}
	}

	public void addPatternCandidates(ArrayList<String> patternCandidates, int sequenceLength) {
		int numPatterns = patternCandidates.size();
		for (int i = 0; i < numDevices; i++) {
			int finalNumElementsInDevice = StdRandom.randomNumber((int) (sequenceLength * 0.9),
					(int) (sequenceLength * 1.1));
			while (this.numElementsInDevices[i] < finalNumElementsInDevice) {
				int selectedPC = StdRandom.gaussian(numPatterns - 1, 0, numPatterns / 2, numPatterns / 5);
				String currentPC = patternCandidates.get(selectedPC);
				int currentPCLength = currentPC.split(",").length;
				this.devices[i] += currentPC + "|";
				this.numElementsInDevices[i] += currentPCLength;
			}
		}
		if (verbose) {
			System.out.println("Add pattern candidates");
			this.printCurrentDevices();
		}
	}

	public void printCurrentDevices() {
		for (int i = 0; i < this.numDevices; i++) {
			System.out.println("# of elements: " + this.numElementsInDevices[i] + ",sequence: " + this.devices[i]);
		}
	}

	public void outputDeviceDataToFile(String outputFilePath, double probability) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFilePath)));
			for (int i = 0; i < devices.length; i++) {
				String str = devices[i].replaceAll("\\|", ",");
				if(str.endsWith(","))
					str = str.substring(0, str.length()-1);
				String[] items = str.split(",");
				if (items.length != this.numElementsInDevices[i])
					System.out.println("Error occurs....");

				writer.write(i+"\t");
				for(int j = 0; j< items.length-1; j++) {
					String item = items[j];
						if (StdRandom.bernoulli(probability)) {
							writer.write(item + "|0,");
						}
				}
				writer.write(items[items.length-1] + "|0");
				writer.newLine();
			} // end
				// for
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
