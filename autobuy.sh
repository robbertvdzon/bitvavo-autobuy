#!/bin/bash
cd "$(dirname "$0")"

./mvnw compile -q exec:java -Dexec.mainClass="AutoBuy" > autobuy.out 2>&1
status=$?
if test $status -eq 0
then
	echo "stuur mail"
  cat autobuy.out | mail -s "Bitvavo autobuy: new coins" robbert@vdzon.com
else
  if test $status -eq 50
  then
      echo "stuur mail met error"
      cat autobuy.out | mail -s "Error during Bitvavo autobuy" robbert@vdzon.com
  else
      echo "Niets gekocht"
  fi
fi
