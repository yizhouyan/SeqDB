package interactive.generatedatasets;

import org.apache.commons.cli.*;

import java.util.ArrayList;

public class DataGenerator {

	private int alphabetSize = 2000;
	private int sequenceLength = 50000;
	private int maxLengthFreqPatterns = 10;
	private int numDevices = 100;
	private int numPatterns = 10000;
	private int numFreqPatterns = 2000;
	private int globalSupport = 100;
	private int localSupport = 2;
	private double noiseRate = 0.0;

	private String outputFilePath;
	private boolean verbose;
	private ArrayList<String> frequentSequences;
	private ArrayList<String> patternCandidates;

	public DataGenerator(int alphabetSize, int sequenceLength, int maxLengthFreqPatterns, double noiseRate,
			String outputFilePath, boolean verbose) {
		this.alphabetSize = alphabetSize;
		this.sequenceLength = sequenceLength;
		this.maxLengthFreqPatterns = maxLengthFreqPatterns;
		this.numDevices = numDevices;
		this.numPatterns = numPatterns;
		this.numFreqPatterns = sequenceLength/(2 * maxLengthFreqPatterns + 5);
		this.globalSupport = globalSupport;
		this.localSupport = localSupport;
		this.outputFilePath = outputFilePath;
		this.noiseRate = noiseRate;
		this.verbose = verbose;
		this.checkValidation();
		System.out.println("Alphabet Size:" + alphabetSize + " , sequence length: " + sequenceLength +
		" , max length: " + maxLengthFreqPatterns + " , numFreqPatterns: " + numFreqPatterns);
	}

	public void checkValidation() {
		if ((this.numFreqPatterns * this.globalSupport / this.numDevices + 1) * this.localSupport
				* this.maxLengthFreqPatterns > this.sequenceLength) {
			System.out.println("Invalid parameter setting!");
			System.exit(0);
		}
	}

	public void generatePatterns() {
		GeneratePatterns gp = new GeneratePatterns();
		this.patternCandidates = gp.generatePatternCandidates(this.numPatterns, this.maxLengthFreqPatterns,
				this.alphabetSize);
		gp.outputPatternCandidatesToFile(patternCandidates, "PatternCandidates.csv");
		this.frequentSequences = gp.generateFrequentPatterns(this.patternCandidates, this.numFreqPatterns);
		gp.outputFrequentPatternsToFile(frequentSequences, "FrequentPatterns.csv");
	}

	public void generateDevices() {
		GenerateDevices gd = new GenerateDevices(numDevices, localSupport, globalSupport, sequenceLength, verbose);
		gd.addFrequentPatterns(frequentSequences);
		gd.addPatternCandidates(patternCandidates, sequenceLength);
		gd.outputDeviceDataToFile(outputFilePath, 1-this.noiseRate);
	}

	public void generateData() {
		// build up a set of pattern candidates
		generatePatterns();
		generateDevices();
	}

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("a", true, "alphabet size");
		options.addOption("s", true, "sequence length");
		options.addOption("m", true, "max length of frequent patterns");
		options.addOption("o", true, "output file directory");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			int alphabetSize = Integer.parseInt(cmd.getOptionValue("a"));
			int sequenceLength = Integer.parseInt(cmd.getOptionValue("s"));
			int maxLengthFreqPatterns = Integer.parseInt(cmd.getOptionValue("m"));
			String outputFileDic = cmd.getOptionValue("o");

			String outputFilePath = outputFileDic + "synthetic-" + sequenceLength + "-"
					+ alphabetSize + "-" + maxLengthFreqPatterns + ".csv";
			boolean verbose = false;
			DataGenerator dataGenerator = new DataGenerator(alphabetSize, sequenceLength, maxLengthFreqPatterns,
					 0.0, outputFilePath,
					false);
			dataGenerator.generateData();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
