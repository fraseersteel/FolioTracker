package model.QuoteServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class testClass {

    public static void main(String[] args) {
        try {

            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            System.out.print("Input a ticker symbol: ");
            while ((line = reader.readLine()) != null) {
                String str = StrathQuoteServer.getLastValue(line);
                System.out.println(line + " has a stock value of " + str);
                System.out.print("Input a ticker symbol: ");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}