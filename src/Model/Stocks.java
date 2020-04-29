package Model;

public class Stocks implements Stock{
        private String ticker;
        private Float stockValue;
        private int totalNumber;
        private Float initialStockValue;
        private Float previousPrice;
        private Float highest;
        private Float lowest;

    public Stocks(String ticker,int totalNumber) {
        this.ticker = ticker;
        this.totalNumber = totalNumber;
        setInitialStockValue();
        updateStockValue();
        this.lowest = stockValue;
        this.highest = stockValue;
        this.previousPrice = stockValue;
    }

    @Override
    public String getTicker() {
        return ticker;
    }

    @Override
    public Float getStockValue() {
        return stockValue;
    }

    @Override
    public void updateInitialStock(){
        String temp = "0";
        try {
            temp = StrathQuoteServer.getLastValue(this.getTicker());
            temp = temp.replace(",","");
        } catch (WebsiteDataException | NoSuchTickerException e) {
            System.out.println("Error in attempting to get given ticker's data.");
        }
        this.initialStockValue += Float.parseFloat(temp);
    }
    @Override
    public void setInitialStockValue(){
        String temp = "0";
        try {
            temp = StrathQuoteServer.getLastValue(this.getTicker());
            temp = temp.replace(",","");
        } catch (WebsiteDataException | NoSuchTickerException e) {
            System.out.println("Error in attempting to get given ticker's data.");
        }
        this.initialStockValue = Float.parseFloat(temp);
    }
    @Override
    public Float getInitialStockValue(){
        return this.initialStockValue;
    }

    @Override
    public void updateStockValue(){
        String temp = "0";
        try {
            temp = StrathQuoteServer.getLastValue(this.getTicker());
            temp = temp.replace(",","");
        } catch (WebsiteDataException | NoSuchTickerException e) {
            System.out.println("Error in attempting to get given ticker's data.");
        }
        setPreviousPrice(this.stockValue);
        this.stockValue = Float.parseFloat(temp);
    }
    @Override
    public Float getPreviousPrice(){return previousPrice;}
    @Override
    public void setPreviousPrice(Float price){
        previousPrice = price;
    }
    @Override
    public int getAmountOfStocks() {
        return totalNumber;
    }

    @Override
    public void setAmountOfStocks(int amountOfStocks) {
        this.totalNumber = amountOfStocks;
    }

    @Override
    public Float getTotalWorth(){
        return stockValue * totalNumber;
    }

    @Override
    public Float getHighest() {
        return highest;
    }

    @Override
    public Float getLowest() {
        return lowest;
    }

    @Override
    public void setHighest(float val) {
        highest = val;
    }

    @Override
    public void setLowest(float val) {
        lowest = val;
    }

    @Override
    public Float getCurrentValue() {
        String temp = "0";
        try {
            temp = StrathQuoteServer.getLastValue(this.getTicker());
            temp = temp.replace(",","");
        } catch (WebsiteDataException | NoSuchTickerException e) {
            e.printStackTrace();
        }
        return Float.parseFloat(temp);
    }
}
