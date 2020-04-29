package Model;

public interface Stock {
    /**
     *
     * @return ticker value
     */
    String getTicker();

    /**
     * @return the total current value of a stock
     */
    Float getStockValue();

    //set up some way to deal with same stock of different prices being bought
    void updateInitialStock();

    /**
     * Sets initial value of stock. Should only be set when stock is first created.
     */
    void setInitialStockValue();

    /**
     *
     * @return the initial value of the stock.
     */
    Float getInitialStockValue();

    /**
     * updates current stocks worth.
     */
    void updateStockValue();

    /**
     *
     * @return the previous value of the stock.
     */
    Float getPreviousPrice();

    /**
     * sets the previous value of the stock
     */
    void setPreviousPrice(Float price);

    /**
     * @return the total amount invested of a portfolio
     */
    int getAmountOfStocks();
    /**
     * Assign the total amount invested in a portfolio
     *
     * @param amountOfStocks the amount of stocks.
     */
    void setAmountOfStocks(int amountOfStocks);

    /**
     *
     * @return the total worth of the stock * amount of stocks
     */
    Float getTotalWorth();

    /**
     *
     * @return highest recorded value of the share
     */
    Float getHighest();

    /**
     *
     * @return lowest recorded value of the share
     */
    Float getLowest();

    /**
     * Set the highest recorded value of a stock.
     *
     * @param val - Value to be set.
     * */
    void setHighest(float val);

    /**
     * Set the lowest recorded value of a stock.
     *
     * @param val - Value to be set.
     * */
    void setLowest(float val);

    /**
     * Get the current value of a stock.
     * */
    Float getCurrentValue();
}
