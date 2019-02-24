Group Course Project

Team members:
Hitesh Bhatia classid: #02 (011498318)
Hemalatha Palanivelu classid:#26 (011814517)

Project title:
IPBlacklist and Extension of the IP blacklist to dynamic type in SDN Controller


Tools used: Floodlight controller, mininet (for tesing using a simple topology)

The project implements IPBlacklist application in floodlight controller and extension of IPBlacklist to dynamic type is done using REST API.

COMMANDS USED TO TEST AND RUN THE APPLICATION:
1. java -jar target/floodlight.jar
 Runs the floodlight controller
2. "sudo mn --controller=remote,ip=controller_ip,port=6653 --switch ovsk,protocols=OpenFlow13

 Builds the simple mininet topology and connects it to the controller.
 The blacklist.txt file is populated accordingly taking into consideration a simple topology which has one switch and two hosts (h1 :10.0.0.1 and h2:10.0.0.2) These ipaddresses were assigned 
 for the hosts while testing the implementation in college network.

3. curl -X POST -d '{"ipaddress": "10.0.0.2"}' http://localhost/wm/ipblacklist/add/ipaddress/json 
 This curl command is used to add a new ipaddress to the blacklist text file used in the dynamic implementation using REST API

4. curl -s http://localhost/wm/ipblacklist/history/json
 This curl command is used to view the snapshot of the buffer which has all the dropped flows. 


