package interactive.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Toolbox {
	public static HashSet<Short> getGlobalFrequentElements(List<short[]> inputStringArray, int globalSupport,
                                                            int localSupport) {
		System.out.println("I'm new Function!! Revised One~~~");
		HashMap<Short, Integer> globalFrequency = new HashMap<Short, Integer>();

		for (short [] inputString : inputStringArray) {
			HashMap<Short, Integer> itemMap = new HashMap<>();
			for (Short temp : inputString) {
				if (!itemMap.containsKey(temp))
					itemMap.put(temp, 1);
				else
					itemMap.put(temp, itemMap.get(temp) + 1);
			}

			for (Map.Entry entry : itemMap.entrySet()) {
				short key = (short) entry.getKey();
				int count = (int) entry.getValue();

				if (count >= localSupport) {
					if (!globalFrequency.containsKey(key))
						globalFrequency.put(key, 1);
					else
						globalFrequency.put(key, globalFrequency.get(key) + 1);
				}
			}
		}
		HashSet<Short> frequentItems = new HashSet<Short>();
		for (Entry<Short, Integer> entry : globalFrequency.entrySet()) {
			if (entry.getValue() >= globalSupport) {
				frequentItems.add(entry.getKey());
			}
		}
		return frequentItems;
	}

	public static Long dateTypeToLong(String str){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		Date date = null;
		try {
			if (str.contains(".")) {
				date = formatter.parse(str);
			} else {
				date = formatter2.parse(str);
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
    
    public static long getMaxMemory(){
    	Runtime runtime = Runtime.getRuntime();
    	return  bytesToMegabytes(runtime.maxMemory());
    }
    public static long checkCurrentUsedMemory(long maxUsedMemory){
    	Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
//		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		if (bytesToMegabytes(memory) > maxUsedMemory) {
//			System.out.println("Used memory is bytes: " + memory);
//			System.out
//					.println("Used memory is megabytes: " + bytesToMegabytes(memory));
			maxUsedMemory = bytesToMegabytes(memory);
		}
		return maxUsedMemory;
    }
}
