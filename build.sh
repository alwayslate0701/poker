#!/bin/bash

mkdir -p bin
javac -d bin src/com/*.java

echo "Manifest-Version: 1.0" > bin/MANIFEST.MF
echo "Main-Class: src.com.Poker" >> bin/MANIFEST.MF
echo "" >> bin/MANIFEST.MF  

# jar
cd bin
jar cvfm ../my-poker-solution.jar MANIFEST.MF src/com/
cd ..
