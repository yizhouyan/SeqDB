package interactive.util;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yizhouyan on 9/13/18.
 */
public class ProcessLogStreamData {
    private SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");
    private void processData(String inputFile, String outputFile){
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
            String str = "";
            br.readLine();
            int count = 0;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                String [] splits = str.split(",");
                if(splits.length > 1) {
                    Date date = formatter.parse(splits[0]);
                    sb.append(splits[1] + "|" + date.getTime() + ",");
                    count++;
                }
            }
            String outputStr = sb.toString();
            outputStr = outputStr.substring(0, outputStr.length()-1);
            for(int i = 0; i< 1000; i++){
                bw.write(i+ "\t" + outputStr);
                bw.newLine();
            }
            System.out.println("Number of events: " + count);
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        ProcessLogStreamData process = new ProcessLogStreamData();
        process.processData("../data/realdata/sample_logstream.csv", "../data/realdata/logstream.csv");
    }
}
