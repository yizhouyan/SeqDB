package inputdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class InputData {
    // save original input sequences
    private List<short []> inputArray = new ArrayList<>();
    private List<long []> inputTimeStamp = new ArrayList<>();
    private HashMap<String,Short> stringToShort;
    private short numEvents = 0;
    // save device ids
    private HashMap<Integer, String> deviceIdMap = new HashMap<>();
    // meta data mapping if available
    private HashMap<String, String> metaDataMapping;

    public InputData(){}

    public InputData(String inputFile){
        this.readInputFile(inputFile);
        System.out.println("Complete reading input file...");
    }

    public InputData(String inputFile, String metaFile){
        this.readInputFile(inputFile);
        this.readInMetaDataToMemory(metaFile);
        System.out.println("Complete reading input file and metadata file...");
    }

    public void printInputDataStatistics(){
        System.out.println("Total number of sequences: " + inputArray.size());
        System.out.println("Dictionary Size: " + stringToShort.size());
        System.out.println("Sample input: ");
        for(int i = 0; i< inputArray.get(0).length; i++){
            System.out.print(inputArray.get(0)[i] + "|" + inputTimeStamp.get(0)[i] + ",");
        }
        System.out.println();
    }

    public void readInMetaDataToMemory(String metaFile) {
        this.metaDataMapping = new HashMap<String, String>();
        File file = new File(metaFile);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                String [] subs = str.split("\t");
                if (subs.length < 2)
                    continue;
                metaDataMapping.put(subs[0], subs[1]);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        System.out.println("Number of meta data records: " + metaDataMapping.size());
    }

    public void readInputFile(String inputFile){
        this.stringToShort = new HashMap<>();
        // read input data from file
        File file = new File(inputFile);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            int count = 0;
            while ((str = in.readLine()) != null) {
                String [] subs = str.split("\t");
                deviceIdMap.put(count, subs[0]);
//                inputStringArray.add(subs[1]);
                String [] inputsInString = subs[1].split(",");
                short [] inputsInShorts = new short[inputsInString.length];
                long [] timeInLong = new long[inputsInString.length];
                for(int i = 0; i< inputsInString.length; i++){
                    String [] eventTimeSplit = inputsInString[i].split("\\|");
                    inputsInShorts[i] = getEventId(eventTimeSplit[0], stringToShort);
                    timeInLong[i]= Long.parseLong(eventTimeSplit[1]);
                }
                inputArray.add(inputsInShorts);
                inputTimeStamp.add(timeInLong);
                count++;
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        System.out.println("Number of devices: " + deviceIdMap.size() + ", number of sequences: " + inputArray.size());
    }

    public short getEventId(String str, HashMap<String, Short> stringToShort){
        if(stringToShort.containsKey(str))
            return stringToShort.get(str);
        else{
            stringToShort.put(str, numEvents);
            numEvents++;
            return stringToShort.get(str);
        }
    }

    public short [] translateQuery(String str){
        String [] query = str.split(",");
        short [] queryInShort = new short[query.length];
        for(int i = 0; i< query.length; i++){
            if(this.stringToShort.containsKey(query[i])) {
                queryInShort[i] = this.stringToShort.get(query[i]);
            }else{
                System.out.println("Query element does not exist, please try again");
                System.exit(0);
            }
        }
        return queryInShort;
    }

    public HashMap<Short, String> getDictionaryShortToString(){
        HashMap<Short, String> dictionary = new HashMap<>();
        for(Map.Entry<String, Short> entry: stringToShort.entrySet()){
            dictionary.put(entry.getValue(), entry.getKey());
        }
        return dictionary;
    }

    public List<short[] > getInputStringArray() {
        return inputArray;
    }

    public void setInputStringArray(List<short []> inputStringArray) {
        this.inputArray = inputStringArray;
    }

    public HashMap<Integer, String> getDeviceIdMap() {
        return deviceIdMap;
    }

    public void setDeviceIdMap(HashMap<Integer, String> deviceIdMap) {
        this.deviceIdMap = deviceIdMap;
    }

    public HashMap<String, String> getMetaDataMapping() {
        return metaDataMapping;
    }

    public void setMetaDataMapping(HashMap<String, String> metaDataMapping) {
        this.metaDataMapping = metaDataMapping;
    }

    public List<long[]> getInputTimeStamp() {
        return inputTimeStamp;
    }

    public void setInputTimeStamp(List<long[]> inputTimeStamp) {
        this.inputTimeStamp = inputTimeStamp;
    }
}
