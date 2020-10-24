import com.bitvavo.api.Bitvavo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/*
 Run this code using: mvn compile exec:java -Dexec.mainClass="AutoBuy"
 */
public class AutoBuy {
    private static final DecimalFormat DECIMAL_FORMAT = getDecimalFormat();
    private static final double SPARE_EUROS = 1;
    private static final double MINIMUM_EUROS_AVAILABLE = 10;

    private final Bitvavo bitvavo;

    public static void main(String[] args){
        String apiKey = System.getenv("BITVAVO_API_KEY");
        String secret = System.getenv("BITVAVO_SECRET");
        if (apiKey==null) fail("BITVAVO_API_KEY env is not set");
        if (secret==null) fail("BITVAVO_SECRET env is not set");

        Bitvavo bitvavo = new Bitvavo(new JSONObject("{" +
                "APIKEY: '"+apiKey+"', " +
                "APISECRET: '"+secret+"', " +
                "RESTURL: 'https://api.bitvavo.com/v2'," +
                "WSURL: 'wss://ws.bitvavo.com/v2/'," +
                "ACCESSWINDOW: 10000, " +
                "DEBUGGING: false }"));
        new AutoBuy(bitvavo).start();
    }

    public AutoBuy(Bitvavo bitvavo) {
        this.bitvavo = bitvavo;
    }

    private void start() {
        double euroBalance = getEuroBalance();
        double btcBalance = getBtcBalance();
        System.out.println("Current euro balance:"+euroBalance);
        System.out.println("Current btc balance:"+btcBalance);

        if (euroBalance>MINIMUM_EUROS_AVAILABLE) {
            double btcPriceD = getBtcPriceD();
            String toBuyString = calculateAmountToBuy(euroBalance, btcPriceD);
            System.out.println("Buying " + toBuyString+" bitcoin, for a price of : "+btcPriceD);
            String buyResult = buyBtc(toBuyString);
            double newEuroBalance = getEuroBalance();
            double newBtcBalance = getBtcBalance();
            System.out.println("New euro balance:"+newEuroBalance);
            System.out.println("New btc balance:"+newBtcBalance);
            System.out.println("\n\nBuy result:"+buyResult);
        }
        else{
            System.out.println("Minimal "+MINIMUM_EUROS_AVAILABLE+" euro is needed for an automatic buy");
        }
        System.exit(0);
    }

    private double getEuroBalance() {
        JSONArray response = bitvavo.balance(new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            if (jsonObject.get("symbol").equals("EUR")) {
                String euros = jsonObject.get("available").toString();
                return Double.parseDouble(euros);
            }
        }
        throw new RuntimeException("Euro balance could not be found");
    }

    private double getBtcBalance() {
        JSONArray response = bitvavo.balance(new JSONObject());
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            if (jsonObject.get("symbol").equals("BTC")) {
                String euros = jsonObject.get("available").toString();
                return Double.parseDouble(euros);
            }
        }
        throw new RuntimeException("Euro balance could not be found");
    }

    private double getBtcPriceD() {
        JSONArray response = bitvavo.tickerPrice(new JSONObject("{ market: BTC-EUR }"));
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            if (jsonObject.get("market").equals("BTC-EUR")) {
                String btcPrice = jsonObject.get("price").toString();
                return Double.parseDouble(btcPrice);
            }
        }
        throw new RuntimeException("BTC price could not be found");
    }

    private String buyBtc(String toBuyString) {
        return bitvavo.placeOrder("BTC-EUR", "buy", "market", new JSONObject("{ amount: " + toBuyString + " }")).toString(2);
    }

    private String calculateAmountToBuy(double euroAvail, double btcPrice) {
        return DECIMAL_FORMAT.format((euroAvail-SPARE_EUROS)/ btcPrice);
    }

    private static DecimalFormat getDecimalFormat() {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        return new DecimalFormat("#.######",otherSymbols); // 1 miljoenste :  bij 1 miljoen: 1 euro
    }

    private static void fail(String s) {
        System.out.println(s);
        System.exit(-1);
    }

}
