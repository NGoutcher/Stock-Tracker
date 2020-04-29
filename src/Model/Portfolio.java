package Model;

import java.util.HashMap;

public interface Portfolio {
    /**
     * @return the name of a portfolio
     */
    String getName();

    /**
     * Assign the value of name to portfolio
     *
     * @param Name the name of the portfolio.
     */
    void setName(String Name);
    /**
     * @return the total current value of a portfolio
     */
    Float getProfit();
    /**
     * update the total value of a portfolio
     *
     */
    void updateProfit();

    /**
     * updates the total profit of the portfolio
     * @param sold  the price of the sold stock.
     */
    void updateReturn(float sold);

    /**
     * @return total profit gained.
     */
    Float getReturn();

    /**
     * @return the total amount invested of a portfolio
     */
    Float getTotalInvested();

    /**
     *  updates the amount the porfolio is currently worth.
     */
    void updateTotalInvested();

    /**
     * @return a stock from the portfolio.
     */
    Stock getStock(String ticker);

    /**
     * Add new stock with ticker and amount
     *
     * @param amount the amount of stocks.
     * @param ticker the ticker of the stock.
     */
    void addStock(String ticker, int amount);

    /**
     * Sell stock using ticker and amount
     *
     * @param amount the amount of stocks.
     * @param ticker the ticker of the stock.
     */
    void sellStock(String ticker, int amount);

    /**
     * remove stock from portfolio
     *
     * @param ticker the ticker of the stock.
     */
    void removeStock(String ticker);

    /**
     * get the ticker and stock hashmap
     */
    HashMap<String, Stock> getStockList();

    /**
     * set the amount held of a stock
     *
     * @param ticker the ticker of the stock.
     * @param amount the amount to update it to
     */
    void setAmountHeld(String ticker, int amount);

}
