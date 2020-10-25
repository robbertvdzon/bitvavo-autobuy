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

Installation:
clone the code in ~/git

Add the following in the crontab
BITVAVO_API_KEY=[key]
BITVAVO_SECRET=[secret]
0 6 * * *  cd /Users/robbert/git/bitvavo-autobuy && ./autobuy.sh 

You also need to setup postfix in order to be able to send email messages.
Follow these steps to configure postfix:
https://firxworx.com/blog/it-devops/sysadmin/send-command-line-emails-from-macos-using-rackspace-smtp/

Then follow these steps to change the sender mail adress:
https://www.cyberciti.biz/tips/howto-postfix-masquerade-change-email-mail-address.html


