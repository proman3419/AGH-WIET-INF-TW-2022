#!/bin/bash

mkdir -p results

for N in 5 25; do
    for itersCount in 10 30; do
        # omijamy naiwną implementację ponieważ prowadzi do zakleszczenia
        for methodName in asym arbiter pickupboth; do
            testName="$methodName"_"$N"p_"$itersCount"i_js
            savePath="results/$testName.csv"
            echo $testName
            node ./philosophers.js "$N" "$itersCount" "$methodName" false >> "$savePath"
            cat "$savePath"
        done
    done
done
