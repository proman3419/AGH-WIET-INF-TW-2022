#!/bin/bash

mkdir -p results

for N in 5 25; do
    for iters_count in 10 30; do
        # omijamy naiwną implementację ponieważ prowadzi do zakleszczenia
        for method_name in asym arbiter pickupboth; do
            test_name="$method_name"-"$N"p-"$iters_count"i-js
            save_path="results/$test_name.csv"
            echo $test_name
            node ./philosophers.js "$N" "$iters_count" "$method_name" false > "$save_path"
            cat "$save_path"
        done
    done
done
