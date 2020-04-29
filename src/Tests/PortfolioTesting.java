package Tests;

import Controller.FolioController;
import Model.Portfolios;
import Model.Stock;
import Model.Stocks;
import View.FolioView;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class PortfolioTesting {

   //Includes some basic testing for portofolios class
    @Test
    public void portfolioTest(){
        Portfolios portfolio1 = new Portfolios("Portfolio1");
        assertEquals("Portfolio1",   portfolio1.getName());  //Test for getName

        portfolio1.addStock("MSFT",100);
        assertEquals("MSFT",portfolio1.getStock("MSFT").getTicker()); //Test for getStock

        portfolio1.removeStock("MSFT");
        assertNull(portfolio1.getStock("MSFT"));   //Test for removeStock

    }


    //Tests that buying more shares for a share you already own stacks and doesn't add a new stock object
    @Test
    public void buyOwnedTest() {
        Portfolios portfolio1 = new Portfolios("Portfolio1");
        assertEquals("Portfolio1",   portfolio1.getName());
        portfolio1.addStock("MSFT",100);
        assertEquals("MSFT",portfolio1.getStock("MSFT").getTicker());
        assertEquals(1, portfolio1.getStockList().size());

        portfolio1.buyOwned("MSFT", 100);
        assertEquals(1, portfolio1.getStockList().size());
        assertEquals(200, portfolio1.getStockList().get("MSFT").getAmountOfStocks());
    }

    /*
        This test checks the total amount invested by mocking Stocks class
        Here mocking is done to get the current values of Stocks 
    */
   @Mock
    private Stocks MSFT;
    private Stocks AAPL;

    @Test
    public void totalInvestedTest() {
        Portfolios portfolio1 = new Portfolios("Portfolio1");
        assertEquals("Portfolio1",   portfolio1.getName());
        portfolio1.addStock("MSFT",100);
        portfolio1.addStock("AAPL", 50);

        Float total = 0f;

        MSFT=Mockito.mock(Stocks.class);
        AAPL=Mockito.mock(Stocks.class);

        MSFT = new Stocks("MSFT", 100);
        AAPL = new Stocks("AAPL", 50);

        total += (MSFT.getStockValue()*MSFT.getAmountOfStocks()) + (AAPL.getStockValue()*AAPL.getAmountOfStocks());
        assertEquals(total, portfolio1.getTotalInvested());
    }
    
    //Test class to check removal of stock(s) from the current portfolio    
    @Test
    public void TestRemoveStock(){
        Portfolios portfolio1 = new Portfolios("Portfolio1");
        portfolio1.addStock("MSFT",100);
        portfolio1.addStock("AAPL",20);
        portfolio1.removeStock("MSFT");

        assertEquals(1,portfolio1.getStockList().size());
    }
    
    
    //Test to check the removal of portfolio 
    @Test
    public void TestRemovePortfolio() {

        FolioView view;
        FolioController controller=new FolioController();

        Portfolios portfolio1 = new Portfolios("Portfolio1");
        Portfolios portfolio2 = new Portfolios("Portfolio2");

        controller.getPortfoliosList().add(portfolio1);
        controller.getPortfoliosList().add(portfolio2);

        view=controller.getView();
        view.getTable().remove(portfolio1);
        controller.getPortfoliosList().remove(portfolio1); //Here getPortfoiloList contains all the existing portfolios 

        int size= controller.getPortfoliosList().size();

        assertEquals(1,size);
    }


    
    //Test to check the add stocks functionality 
    @Test
    public void addStockTest() {
        Portfolios p = new Portfolios("P1");
        Float zero = 0f;
        assertEquals(0, p.getStockList().size());
        assertEquals(zero, p.getTotalInvested());

        p.addStock("MSFT", 1);
        assertEquals(1, p.getStockList().size());
        Float initialVal = new Stocks("MSFT", 1).getCurrentValue();
        assertEquals(initialVal, p.getTotalInvested());
    }

    @Test
    public void sellStockTest() {
        Portfolios p = new Portfolios("P1");
        p.addStock("MSFT", 2);
        Float zero = 0f;
        Float stockVal = new Stocks("MSFT", 1).getCurrentValue();
        assertEquals(zero, p.getReturn());
        assertEquals(2, p.getStockList().get("MSFT").getAmountOfStocks());
        p.sellStock("MSFT", 1);
        assertEquals(stockVal, p.getReturn());
        assertEquals(1, p.getStockList().get("MSFT").getAmountOfStocks());
    }

}
