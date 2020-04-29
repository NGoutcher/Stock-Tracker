package Tests;

import Model.Stocks;
import Model.StrathQuoteServer;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class StocksTesting {
    
    /*
        Test to check the current value of the Stock.
        Here the StrathQuoteServer is mocked to get the live value of the given Stock
    */
    @Mock
    private StrathQuoteServer server;
    @Test
    public void TestGetCurrentValue(){

        Stocks x = new Stocks("MSFT",1);
        server = Mockito.mock(StrathQuoteServer.class);

        try {
            Float expectedValue= Float.parseFloat(server.getLastValue(x.getTicker()));
            assertEquals(expectedValue, x.getCurrentValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //When a new object is made, previous price should == current stock value, then can be changed by Stock.setPreviousPrice(float)
    @Test
    public void setPreviousPriceStocksTest() {
        Stocks x = new Stocks("MSFT", 1);
        Float newVal = 123.123f;
        assertEquals(x.getStockValue(), x.getPreviousPrice());
        x.setPreviousPrice(newVal);
        assertEquals(x.getPreviousPrice(), newVal);
    }

    //Some basic test for Class Stocks
    @Test
    public void stockTest(){
        
        Stocks stock1 = new Stocks("MSFT",100);
        assertEquals("MSFT", stock1.getTicker()); //Test to check the name of the Stock
        assertEquals(100, stock1.getAmountOfStocks()); //Test to check the amount of selected stock(s)
        
        stock1.setAmountOfStocks(200);
        stock1.updateStockValue();
        assertEquals(200,   stock1.getAmountOfStocks());  //Test to check the update stock value functionality
        
        Float totalVal = stock1.getStockValue() * stock1.getAmountOfStocks();
        assertEquals(totalVal, stock1.getTotalWorth());  //Test to check the overall value of a stock which is value of a stock*amount of stocks
        
        assertEquals(stock1.getInitialStockValue(), stock1.getStockValue()); 
        assertEquals(totalVal,stock1.getTotalWorth());
    }

    //Test to check the lowest the value of the Stock
    @Test
    public void lowestValueTest() {
        Stocks x = new Stocks("MSFT", 1);
        assertEquals(x.getLowest(), x.getStockValue());
        x.setLowest(123f);
        Float lowest = 123f;
        assertEquals(lowest, x.getLowest());
    }

    //Test to check the highest the value of the Stock
    @Test
    public void highestValueTest() {
        Stocks x = new Stocks("MSFT", 1);
        assertEquals(x.getHighest(), x.getStockValue());
        x.setHighest(123f);
        Float lowest = 123f;
        assertEquals(lowest, x.getHighest());
    }

}
