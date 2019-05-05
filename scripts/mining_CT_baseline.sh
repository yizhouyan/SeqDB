#!/bin/bash
# mining close patterns
arr=(2 5 10 15 20)
#arr=(5 20 30 40 50)
for i in "${arr[@]}";
do
	java -Xmx10g -cp ASAP_jar/ASAP.jar interactive.mining.baseline.close.CloseFSDetection "../data/realdata/CT_data_ts.csv" $i 10
done

# mining max patterns
for i in "${arr[@]}";
do
        java -Xmx10g -cp ASAP_jar/ASAP.jar interactive.mining.baseline.maximum.MaxFSDetection "../data/realdata/CT_data_ts.csv" $i 10
done
