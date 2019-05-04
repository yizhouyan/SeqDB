package interactive.util;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yizhouyan on 9/13/18.
 */
public class ProcessLogData {
    private void processData(String inputFile, String outputFile){
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
            String str = "";
            int count = 0;
            int sumLength = 0;
            while ((str = br.readLine()) != null) {
                String [] splits = str.split("\t");
                if(splits[1].split(",").length <= 50000) {
                    bw.write(str);
                    bw.newLine();
                    sumLength += splits[1].split(",").length;
                    System.out.println(count++);
                }
            }
            System.out.println(sumLength * 1.0/count);
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        ProcessLogData process = new ProcessLogData();
        process.processData("../data/realdata/real10000data_timestamp_compressedDic.csv",
                "../data/realdata/real10000data_timestamp_less5w.csv");
    }
}
