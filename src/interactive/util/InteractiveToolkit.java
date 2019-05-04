package interactive.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by yizhouyan on 6/20/18.
 */
public class InteractiveToolkit {

    public static String shortArrayToString(short [] freqPattern){
        String str = "";
        for(int i =0; i< freqPattern.length; i++){
            str += freqPattern[i] + ",";
        }
        str =  str.substring(0, str.length()-1);
        return str;
    }


    public static String shortArrayToString(short [] freqPattern, HashMap<Short, String> dictionary){
        String str = "";
        for(int i =0; i< freqPattern.length; i++){
            str += dictionary.get(freqPattern[i]) + ",";
        }
        str =  str.substring(0, str.length()-1);
        return str;
    }


    public static Integer[] computeRemovePositions(short [] longPattern, short [] shortPattern){
        ArrayList<Integer> removePositions = new ArrayList<Integer>();
        int i = 0;
        int j = 0;
        while(j<shortPattern.length){
            if(longPattern[i] == shortPattern[j]){
                i++;
                j++;
            }else{
                removePositions.add(i);
                i++;
            }
        }
        while(i<longPattern.length){
            removePositions.add(i);
            i++;
        }
        return removePositions.toArray(new Integer[removePositions.size()]);
    }


    public static int [] bitSetToIntArray(BitSet bitset){
        int [] bitsetInArray = new int[bitset.cardinality()];
        int i = 0;
        if(bitsetInArray.length > 0) {
            int pos = 0;
            while(pos < bitset.length()) {
                int nextSet = bitset.nextSetBit(pos);
                bitsetInArray[i++] = nextSet;
                pos = nextSet+1;
            }
        }
        return bitsetInArray;
    }

    public static ArrayList<Integer> bits2Ints(BitSet bs) {
        ArrayList<Integer> temp = new ArrayList<>();
        int pos = 0;
        while(pos < bs.length()) {
            int nextSet = bs.nextSetBit(pos);
            temp.add(nextSet);
            pos = nextSet+1;
        }
        return temp;
    }

    public static ArrayList<String> generateQuery(){
        ArrayList<String> queries = new ArrayList<>();
        ArrayList<String> prevQueries = new ArrayList<>();

        for(int i = 0; i< 5; i++){
            ArrayList<String> newQueries = new ArrayList<>();
            if(i == 0){
                for(int j = 0; j< 5; j++) {
                    newQueries.add(j+"");
                }
//                queries.addAll(newQueries);
                prevQueries = newQueries;
            }else{
                for(String str: prevQueries){
                    for(int j = 0; j< 5; j++) {
                        newQueries.add(str+","+j);
                    }
                }
                if(i >= 3) {
                    queries.addAll(newQueries);
                }
                prevQueries = newQueries;
            }
        }
//        System.out.println(queries);
        System.out.println(queries.size());
        return queries;
    }

    public static ArrayList<String> getQuerysFromFile(String filename){
        ArrayList<String> queryList = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str;
            while((str = br.readLine())!= null){
                queryList.add(str);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return queryList;
    }
}
