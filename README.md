Fork from https://github.com/bitvavo/python-bitvavo-api with own Autobuy script.

Usecase:
- Periodically deposit euro money to bitvavo using a periodic bank transfer
- use a script to automatically buy bitcoins from the euro balance using the 

To use:
- Create a bitvavo account
- Generate an API KEY
- Store the api key in the BITVAVO_API_KEY environment variable
- Story the secret in the BITVAVO_SECRET environment variable
- Run the autobuy.sh script
- The script will check the euro balance of your account, and if that is more then 10 euro, it will buy BTC from all the euro's
- An email will be send with the result 

When you run this script every day, and periodically stransfer money to bitvavo, the bitcoins will be bought automatically
