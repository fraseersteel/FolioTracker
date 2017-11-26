package QuoteServer;

public interface QuoteServer {

    String getLastValue(String tickerSymbol)throws WebsiteDataException;
}
