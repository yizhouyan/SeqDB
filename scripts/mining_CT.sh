#!/bin/bash
# mining close patterns
#arr=(2 5 10 15 20)
arr=(5 20 30 40 50)
for i in "${arr[@]}";
do
	java -cp ASAP_jar/ASAP.jar interactive.client.PatternMiningWithIndex $i close
done

# mining max patterns
for i in "${arr[@]}";
do
        java -cp ASAP_jar/ASAP.jar interactive.client.PatternMiningWithIndex $i max
done
