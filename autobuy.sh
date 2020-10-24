#!/bin/bash

mvn compile -q exec:java -Dexec.mainClass="AutoBuy" | mail -s "Bitvavo autobuy" robbert@vdzon.com
