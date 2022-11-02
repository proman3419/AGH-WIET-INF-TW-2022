#!/bin/bash

mkdir -p results
mvn package

for N in 5 25; do
    for iters_count in 10 30; do
        for method_name in starving arbiter; do
            test_name="$method_name"-"$N"p-"$iters_count"i-java
            savePath="results/$test_name.csv"
            mainClassPath="org.example.Main"
            echo $test_name
            java -cp target/cw3zd-1.0-SNAPSHOT.jar org.example.Main "$N" "$iters_count" "$method_name" false > "$savePath"
            cat "$savePath"
        done
    done
done
