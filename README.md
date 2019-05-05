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
java -cp ASAP_jar/ASAP.jar interactive.server.Startup
```
##### 2. Building up index
```bash
java -cp ASAP_jar/ASAP.jar interactive.client.IndexSequences [INPUT_DATA_FILE] [min_support]
```
Examples:
```bash
java -cp ASAP_jar/ASAP.jar interactive.client.IndexSequences "CT_data_ts.csv" 2
```
##### 3. Pattern Mining
```bash
java -cp ASAP_jar/ASAP.jar interactive.client.PatternMiningWithIndex [support] [close/max]
```
##### 4. Pattern Querying 

### License

This project is licensed under the MIT License.
