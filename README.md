## ASAP: A System for Exploring Sequential Patterns

In this work, we present a system, ASAP, that efficiently supports a rich variety of 
pattern exploration requests with varying pattern semantics and input parameter settings. 

ASAP employs an innovative index structure that succinctly summarizes sequence data using
 a small set of sequential patterns. Using the index, ASAP features effective execution 
 strategies for pattern query and mining. As a result, ASAP provides near-real time 
 responsiveness even on large sequence data sets. 
 
Our experimental evaluation using several real world and synthetic sequence data sets 
demonstratesthe versatility and efficiency of ASAP, showing it to be 3 orders of magnitude 
faster or more than the current state-of-the-art pattern query and mining techniques.

### Getting Started

##### 1. Start the server
```bash
java -cp out/artifacts/CompactInteractivePatternExploration_jar/CompactInteractivePatternExploration.jar interactive.server.Startup
```
##### 1. Building up index
```bash
java -cp main_philips.py --lr 1e-5 --resume inception-resnet-v2-model-resize-299-normalized/model_best.pth.tar -b 64 -e
```
##### 2. Pattern Mining

##### 3. Pattern Querying 

### License

This project is licensed under the MIT License.
