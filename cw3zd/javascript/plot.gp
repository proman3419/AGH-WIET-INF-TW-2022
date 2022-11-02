#!/usr/local/bin/gnuplot --persist

set boxwidth 0.5
set style fill solid
set terminal png size 640,480 enhanced
set title ARG1
set xlabel "f(x): Avg wait time [ms]\nx: Philosopher id"
set output ARG2

plot "tmp.dat" using 1:3:xtic(2) with boxes notitle lt rgb "#376abd"
