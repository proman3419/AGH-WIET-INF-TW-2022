#!/bin/bash

mkdir -p results
mvn package

for N in 5 25; do
    for itersCount in 10 30; do
        for methodName in starving arbiter; do
            testName="$methodName"-"$N"p-"$itersCount"i-java
            savePath="results/$testName.csv"
            mainClassPath="org.example.Main"
            echo $testName
            java -cp target/cw3zd-1.0-SNAPSHOT.jar org.example.Main "$N" "$itersCount" "$methodName" false > "$savePath"
            cat "$savePath"
        done
    done
done
