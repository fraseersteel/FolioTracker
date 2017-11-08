package model;

public interface IStock extends Model {


    public String getTicketSymbol();

    public String getStockName();

    public int getNumShares();

    public double getPricePerShare();

    public double getValueOfHolding();

    public void setNumShares(int numShares);

    public void setPricePerShare(int pricePerShare);

    public void setValueOfHolding(int valueOfHolding);

}
