#!/bin/bash

mkdir -p plots

for file in results/*; do
    file_name="$(basename -- $file)"
    test_case="${file_name%.*}"
    data=$(cat "$file")

    echo "$test_case"

    IFS=':' read -ra ADDR <<< "$data"
    i=0
    for vals in "${ADDR[@]}"; do
        if [[ $i -ne 0 ]]; then
            IFS2=',' read -ra ADDR2 <<< "$vals"
            touch tmp.dat
            j=0
            for val_raw in "${ADDR2[@]}"; do
                echo $j $j ${val_raw/%?/} >> tmp.dat
                ((j=j+1))
            done
            gnuplot -c plot.gp "$test_case" "plots/$test_case".png
            cat tmp.dat
            rm tmp.dat
        fi
        ((i=i+1))
    done
done
