#! /bin/bash  

s2i build -e PROFILE=docker -e WAR_NAME=$1 -e INCREMENTAL=true --incremental $2 $3 $4 -r $5