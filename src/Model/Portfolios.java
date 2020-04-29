package Model;

import java.util.HashMap;

public class Portfolios implements Portfolio {
    private String name;
    private Float ret;
    private Float totalInvested;
    private Float profit;

    private HashMap<String, Stock> stockList = new HashMap<>();

    public Portfolios(String name){
        this.name = name;
        totalInvested = 0f;
        profit = 0f;
        ret = 0f;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String Name) {
        this.name = Name;
    }

    @Override
    public Float getProfit() {
        return profit;
    }

    @Override
    public void updateProfit() {
        profit = ret - totalInvested;
    }
    @Override
    public void updateReturn(float sold){
       ret += sold;
    }
    @Override
    public Float getReturn() {
        return ret;
    }

    @Override
    public Float getTotalInvested() {
        return totalInvested;
    }

    @Override
    public void updateTotalInvested() {
        totalInvested = 0f;
        for (Stock stock : stockList.values()) {
            totalInvested += (stock.getStockValue() * stock.getAmountOfStocks());
        }
    }

    @Override
    public Stock getStock(String ticker) {
        return stockList.get(ticker);
    }

    @Override
    public void addStock(String ticker, int amount) {
        Stocks stock = new Stocks(ticker, amount);
        this.stockList.put(ticker, stock);
        totalInvested += stock.getInitialStockValue()*stock.getAmountOfStocks();
    }
    @Override
    public void sellStock(String ticker, int amount){
        updateReturn(stockList.get(ticker).getStockValue() * amount);
        stockList.get(ticker).setAmountOfStocks(stockList.get(ticker).getAmountOfStocks() - amount);
        stockList.get(ticker).updateStockValue();
    }
    public void buyOwned(String ticker, int amount){
        totalInvested += stockList.get(ticker).getInitialStockValue() * amount;
        updateProfit();
        stockList.get(ticker).setAmountOfStocks(stockList.get(ticker).getAmountOfStocks() + amount);
    }
    @Override
    public void removeStock(String ticker) {
        this.stockList.remove(ticker);
    }

    @Override
    public HashMap<String, Stock> getStockList() {
        return stockList;
    }

    @Override
    public void setAmountHeld(String ticker, int amount) {
        stockList.get(ticker).setAmountOfStocks(amount);
        updateTotalInvested();
    }
}
