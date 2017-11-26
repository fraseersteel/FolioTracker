package QuoteServer;

import java.util.HashMap;

public class MockQuoteServer implements QuoteServer {

    HashMap<String, String> quotes = new HashMap<>();

    public MockQuoteServer(){
        quotes.put("BT", "12.34");
        quotes.put("NKE", "12.34");
        quotes.put("A", "12.34");
        quotes.put("B", "12.34");
        quotes.put("C", "12.34");
        quotes.put("D", "12.34");
        quotes.put("E", "12.34");
        quotes.put("F", "12.34");
        quotes.put("G", "12.34");
        quotes.put("H", "12.34");
    }

    @Override
    public String getLastValue(String tickerSymbol) throws WebsiteDataException {
        if(quotes.containsKey(tickerSymbol)){
            return quotes.get(tickerSymbol);
        }
        throw new WebsiteDataException();
    }
}
