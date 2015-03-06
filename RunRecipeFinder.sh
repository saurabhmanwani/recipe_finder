#!/bin/bash
  
LIB=./jars/
 
CP=.:${LIB}json-20140107.jar:${LIB}junit-4.11.jar
 
javac -cp "${CP}" com/pactera/recipefinder/RecipeFinder.java
java -cp "${CP}"  com.pactera.recipefinder.RecipeFinder