#!/bin/bash
# mining close patterns
arr=(5 20 30 40 50)
#arr=(5 20 30 40 50)
for i in "${arr[@]}";
do
	java -Xmx10g -cp ASAP_jar/ASAP.jar interactive.mining.baseline.close.CloseFSDetection "../data/realdata/real10000data_timestamp_less5w.csv" $i 10
done

# mining max patterns
for i in "${arr[@]}";
do
        java -Xmx10g -cp ASAP_jar/ASAP.jar interactive.mining.baseline.maximum.MaxFSDetection "../data/realdata/real10000data_timestamp_less5w.csv" $i 10
done
