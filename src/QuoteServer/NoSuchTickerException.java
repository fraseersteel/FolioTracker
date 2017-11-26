package QuoteServer;

public class NoSuchTickerException extends Exception{


     public NoSuchTickerException() {
     }

     public NoSuchTickerException(String s) {
        super(s);
     }
}