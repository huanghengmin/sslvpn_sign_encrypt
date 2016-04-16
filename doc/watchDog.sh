#!/bin/bash                                                                 
while true
do
   ps -ef|grep -v grep|grep openvpn
   if [ $? -ne 0 ]; then
     service openvpn restart
   fi
    sleep 10
done