package Controller;

import Exceptions.NoPortfoliosException;
import Model.Portfolios;
import Model.Stock;
import Model.Stocks;
import View.FolioView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class FolioController {

    private FolioView view;
    private Portfolios portfolios;
    private ArrayList<Portfolios> portfolioList;

    /**
     * Controller constructor. creates an instance of the view and an instance of the list of portfolios
     * */
    public FolioController() {
        view = new FolioView();
        portfolioList = new ArrayList<Portfolios>();
    }

    /**
     * Adds action listeners to all the buttons within the UI to provide the user with interactivity.
     * */
    public void initController(){

        /**
         * Allows users to buy shares using the ticker and amount fields and then clicking the buy button.
         * */
        view.getBuyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(portfolioList.size() > 0) {
                    int current = 0;
                    try {
                        current = view.getCurrentPortfolio();
                    }catch (Exception e){
                        view.showErrorMessage("Error getting current portfolio.");
                    }
                    if (view.getTickerField().getText().length() > 0) {
                        if (view.getAmountField().getText().length() > 0) {
                            try {
                                if(portfolioList.get(view.getCurrentPortfolio()).getStockList().containsKey(view.getTickerField().getText().trim())) {
                                    try {
                                        float lowest = portfolioList.get(view.getCurrentPortfolio()).getStock(view.getTickerField().getText().trim()).getLowest();
                                        float highest = portfolioList.get(view.getCurrentPortfolio()).getStock(view.getTickerField().getText().trim()).getHighest();
                                        buy(view.getTickerField().getText().trim(), Integer.parseInt(view.getAmountField().getText()), current, lowest, highest);
                                    } catch (NumberFormatException e) {
                                        view.showErrorMessage("Value in the 'Amount' field must be a number.");
                                    }
                                } else {
                                    try {
                                        int amount = Integer.parseInt(view.getAmountField().getText());
                                        if(amount > 0) {
                                            buy(view.getTickerField().getText().trim(), amount, current, -1, -1);
                                        } else {
                                            view.showErrorMessage("Value in the 'Amount' field must more than 0.");
                                        }
                                    } catch (NumberFormatException e) {
                                        view.showErrorMessage("Value in the 'Amount' field must be a number.");
                                    }
                                }
                            } catch (NoPortfoliosException e) {
                                e.printStackTrace();
                            }
                        } else {
                            view.showErrorMessage("You must fill out both 'Ticker' and 'Amount' fields.");
                        }
                    } else {
                        view.showErrorMessage("You must fill out both 'Ticker' and 'Amount' fields.");
                    }
                } else {
                    view.showErrorMessage("No portfolios open.");
                }
            }
        });

        /**
         * Removes shares highlighted in the table on button click.
         * */
        view.getRemoveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(portfolioList.size() > 0) {
                    removeStock();
                    update();
                } else {
                    view.showErrorMessage("No portfolios open.");
                }
            }
        });

        /**
         * Deletes the highlighted portfolio when the delete button is clicked and the field is filled out.
         * */
        view.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(portfolioList.size() > 0) {
                    try{
                        view.getTable().remove(view.getCurrentPortfolio());
                        portfolioList.remove(view.getCurrentPortfolio());
                    } catch (Exception e){
                        view.showErrorMessage("No portfolios open.");
                    }
                    view.getPortfolioTabs().removeTabAt(view.getPortfolioTabs().getSelectedIndex());
                    if(portfolioList.size() > 0) {
                        view.getPortfolioTabs().setSelectedIndex(view.getPortfolioTabs().getTabCount() - 1);
                    }
                    update();
                } else {
                    view.showErrorMessage("No portfolios open.");
                }
            }
        });

        /**
         * Renames a portfolio using the rename field and button.
         * */
        view.getRename().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(portfolioList.size() > 0) {
                    if(view.getPortfolioTabs().getTabCount() > 0 && view.getRenamefield().getText().length() > 0) {
                        String val = view.getRenamefield().getText().trim();
                        renameTab(val, view.getPortfolioTabs().getSelectedIndex());
                        try {
                            portfolioList.get(view.getCurrentPortfolio()).setName(val);
                        } catch (NoPortfoliosException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    view.showErrorMessage("No portfolios open.");
                }
            }
        });

        /**
         * The new tab button will add a new portfolio tab to the JTabbedPane in the view.
         * */
        view.getNewTabButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newTab();
            }
        });

        /**
         * Saves all portfolios to a file when the file menu save button is clicked.
         * */
        view.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                save(new File("PortfolioSave.txt"));
            }
        });

        /**
         * Loads all portfolios from file when the file menu load button is clicked.
         * */
        view.getLoad().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    File saveFile = new File("PortfolioSave.txt");
                    if (saveFile.createNewFile()) {
                    } else {
                        loadSave(saveFile);
                    }
                } catch (IOException e) {
                    view.showErrorMessage("Error loading from file.");
                }
            }
        });

        /**
         * Clears all portfolios saved in file when the file menu clear button is clicked.
         * */
        view.getClear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearFile(new File("PortfolioSave.txt"));
            }
        });

        /**
         * When the refresh button is clicked, all shares held in every portfolio are refreshed for updates.
         * */
        view.getRefresh().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < portfolioList.size(); i++) {
                    for (HashMap.Entry<String, Stock> entry : portfolioList.get(i).getStockList().entrySet()) {
                        entry.getValue().updateStockValue();
                        if(entry.getValue().getLowest() > entry.getValue().getStockValue()) {
                            entry.getValue().setLowest(entry.getValue().getStockValue());
                        }

                        if(entry.getValue().getHighest() < entry.getValue().getStockValue()) {
                            entry.getValue().setHighest(entry.getValue().getStockValue());
                        }
                        update();
                    }
                }
            }
        });
    }

    /**
     * Updates all tables and labels to ensure information is up to date.
     * */
    public void update(){
        try {
            if(portfolioList.size() > 0) {
                JTable tempTab = view.getTable().get(view.getCurrentPortfolio());
                DefaultTableModel model = (DefaultTableModel) tempTab.getModel();
                if(model.getRowCount() > 0) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        model.setValueAt((portfolioList.get(view.getCurrentPortfolio()).getStock(model.getValueAt(i, 0).toString()).getStockValue()), i, 1);
                        model.setValueAt(Float.parseFloat(model.getValueAt(i, 1).toString()) * Float.parseFloat(model.getValueAt(i, 2).toString()), i, 3);
                        model.setValueAt(portfolioList.get(view.getCurrentPortfolio()).getStock(model.getValueAt(i, 0).toString()).getLowest(), i, 4);
                        model.setValueAt(portfolioList.get(view.getCurrentPortfolio()).getStock(model.getValueAt(i, 0).toString()).getHighest(), i, 5);
                    }
                }
                portfolioList.get(view.getCurrentPortfolio()).updateTotalInvested();
                portfolioList.get(view.getCurrentPortfolio()).updateProfit();
                float totalInvested = 0;
                for (int i = 0; i < portfolioList.size(); i++) {
                    totalInvested += portfolioList.get(i).getTotalInvested();
                }

                view.updateCurrentPortfolioSpentLabel(portfolioList.get(view.getCurrentPortfolio()).getTotalInvested());
                view.updateSpentLabel(totalInvested);
            } else {
                view.updateCurrentPortfolioSpentLabel(0);
                view.updateSpentLabel(0);
            }
        } catch (Exception e){
            view.showErrorMessage("No portfolios open.");
            e.printStackTrace();
        }
    }

    /**
     * Buys the specified number of shares for a stock and adds to the current portfolio.
     * It checks if the user already owns shares for this stock, if so adds them to the existing and checks if the price has fluctuated,
     * if not, a new row in the table is added and the fluctuation is kept at '---' to indicate no change.
     *
     * @param ticker - Ticker string
     * @param amount - Amount to be purchased
     * @param current - Current portfolio index
     * @param lowest - If recorded in file, the lowest value recorded for this share
     * @param highest - If recorded in file, the highest value recorded for this share
     * */
    public void buy(String ticker, int amount, int current, float lowest, float highest){
        JTable table = view.getTable().get(current);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        try {
            if (new Stocks(ticker, 1).getInitialStockValue() != 0) {
                if (portfolioList.get(current).getStock(ticker) == null) {
                    portfolioList.get(current).addStock(ticker, amount);
                    float value = portfolioList.get(current).getStock(ticker).getStockValue();
                    if (lowest == -1 && highest == -1) {
                        lowest = value;
                        highest = value;
                    }

                    if (lowest < portfolioList.get(current).getStock(ticker).getLowest()) {
                        portfolioList.get(current).getStock(ticker).setLowest(lowest);
                    }

                    if (highest > portfolioList.get(current).getStock(ticker).getHighest()) {
                        portfolioList.get(current).getStock(ticker).setHighest(highest);
                    }

                    String fluctuation = "";
                    if (value > portfolioList.get(current).getStock(ticker).getPreviousPrice()) {
                        fluctuation = "^^^";
                    } else if (value < portfolioList.get(current).getStock(ticker).getPreviousPrice()) {
                        fluctuation = "vvv";
                    } else {
                        fluctuation = "---";
                    }

                    model.addRow(new Object[]{ticker, value, amount, value * amount, lowest, highest, fluctuation});
                } else {
                    int i = 0;
                    for (; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(ticker)) {
                            portfolioList.get(current).buyOwned(ticker, amount);
                            Stock stock = portfolioList.get(current).getStock(ticker);
                            model.removeRow(i);
                            amount = stock.getAmountOfStocks();
                            float value = portfolioList.get(current).getStock(ticker).getStockValue();
                            if (lowest < portfolioList.get(current).getStock(ticker).getLowest()) {
                                portfolioList.get(current).getStock(ticker).setLowest(lowest);
                            }

                            if (highest > portfolioList.get(current).getStock(ticker).getHighest()) {
                                portfolioList.get(current).getStock(ticker).setHighest(highest);
                            }

                            String fluctuation = "";
                            if (value > portfolioList.get(current).getStock(ticker).getPreviousPrice()) {
                                fluctuation = "^^^";
                            } else if (value < portfolioList.get(current).getStock(ticker).getPreviousPrice()) {
                                fluctuation = "vvv";
                            } else {
                                fluctuation = "---";
                            }

                            model.insertRow(i, (new Object[]{ticker, value, amount, value * amount, lowest, highest, fluctuation}));
                        }
                    }
                }
                update();
            } else {
                view.showErrorMessage("Error trying to get data from server for given ticker '" + ticker +"'.\nPlease ensure you are " +
                        "connected to the internet and have entered a valid ticker.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error trying to get data from server for given ticker '" + ticker +"'.\nPlease ensure you are " +
                    "connected to the internet and have entered a valid ticker.");
        }
    }

    /**
     * Removes the highlighted shares in a table.
     * */
    public void removeStock() {
        try {
            int current = view.getCurrentPortfolio();
            JTable table = view.getTable().get(current);
            int row = table.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            if (row >= 0) {
                String ticker = (String) model.getValueAt(row, 0);
                portfolioList.get(current).removeStock(ticker);
                model.removeRow(row);
            } else {
                view.showErrorMessage("Row not selected.");
            }

            update();
        }catch (Exception e){
            view.showErrorMessage("No portfolios open.");
        }
    }

    /**
     * Adds a new tab to the JTabbed pane and calls an update to update the labels at the bottom of the page.
     * */
    public void newTab(){
        view.getPortfolioTabs().addTab("Untitled", new JPanel());
        addPortfolio();
        view.addFolioTab((JPanel) view.getPortfolioTabs().getComponentAt(view.getPortfolioTabs().getTabCount()-1), view.getPortfolioTabs());
        JTable table = view.getTable().get(view.getPortfolioTabs().getTabCount()-1);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                if(tableModelEvent.getColumn() == 2 && tableModelEvent.getLastRow() >= 0) {
                    try {
                        int tempAmount = Integer.parseInt(model.getValueAt(tableModelEvent.getLastRow(), tableModelEvent.getColumn()).toString());
                        if (tempAmount >= 0) {
                            portfolioList.get(view.getPortfolioTabs().getSelectedIndex()).setAmountHeld((String) model.getValueAt(tableModelEvent.getLastRow(), 0), tempAmount);
                            update();
                        } else {
                            view.showErrorMessage("Value must be more than or equal to 0.");
                            model.setValueAt(portfolioList.get(view.getPortfolioTabs().getSelectedIndex()).getStock((String) model.getValueAt(tableModelEvent.getLastRow(), 0)).getAmountOfStocks(), tableModelEvent.getLastRow(), tableModelEvent.getColumn());
                            update();
                        }
                    } catch (NumberFormatException e) {
                        view.showErrorMessage("Value must be a number more than or equal to 0.");
                        model.setValueAt(portfolioList.get(view.getPortfolioTabs().getSelectedIndex()).getStock((String) model.getValueAt(tableModelEvent.getLastRow(), 0)).getAmountOfStocks(), tableModelEvent.getLastRow(), tableModelEvent.getColumn());
                        update();
                    }
                }
            }
        });
        view.getPortfolioTabs().setSelectedIndex(view.getPortfolioTabs().getTabCount()-1);
        view.getPortfolioTabs().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                update();
            }
        });
    }

    /**
     * Renames a tab.
     *
     * @param text - Title to be set.
     * @param index - Index of tab to change title of.
     * */
    public void renameTab(String text, int index){
        view.getPortfolioTabs().setTitleAt(index, text);
        view.getRenamefield().setText("");
    }

    /**
     * Renames a portfolio item.
     *
     * @param name - Name to be set.
     * @param index - Index of portfolio to change title of.
     * */
    public void renamePortfolio(String name, int index){
        portfolioList.get(index).setName(name);
    }

    /**
     * Adds a new portfolio the list.
     * */
    public void addPortfolio(){
        portfolios = (new Portfolios("Untitled"));
        portfolioList.add(portfolios);
    }

    /**
     * Loads all portfolios from file. If there are any tabs already opened, the new tabs are added to the end of the list of tabs.
     *
     * @param saveFile - File to be loaded from.
     * */
    public void loadSave(File saveFile){
        try {
            Scanner fileInput = new Scanner(saveFile);
            int k = 0;
            if(portfolioList.size() > 0 ) {
                k = portfolioList.size();
            }
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine();
                String[] portInfo = data.split(",");
                newTab();
                renamePortfolio(portInfo[0], k);
                renameTab(portInfo[0], k);
                int stockCount = (portInfo.length - 1);
                DefaultTableModel tempModel = (DefaultTableModel) view.getTable().get(k).getModel();
                String upArrow = "^^^";
                String downArrow = "vvv";
                for (int i = 1; i < stockCount; i=i+5){
                    buy(portInfo[i], Integer.parseInt(portInfo[i+1]), k, Float.parseFloat(portInfo[i+2]), Float.parseFloat(portInfo[i+3]));
                    System.out.println(portInfo[i+4]);
                    System.out.println(portfolioList.get(k).getStock(portInfo[i]));
                    portfolioList.get(k).getStock(portInfo[i]).setPreviousPrice(Float.parseFloat(portInfo[i+4]));
                    if((Float) tempModel.getValueAt(tempModel.getRowCount()-1, 1) >  portfolioList.get(k).getStock(portInfo[i]).getPreviousPrice()) {
                        tempModel.setValueAt(upArrow, tempModel.getRowCount()-1, 6);
                    } else if ((Float) tempModel.getValueAt(tempModel.getRowCount()-1, 1) <  portfolioList.get(k).getStock(portInfo[i]).getPreviousPrice()) {
                        tempModel.setValueAt(downArrow, tempModel.getRowCount()-1, 6);
                    } else {
                        tempModel.setValueAt("---", tempModel.getRowCount()-1, 6);
                    }
                }
                view.getPortfolioTabs().setSelectedIndex(k);
                k = k+1;

            }
            fileInput.close();
        } catch (FileNotFoundException e){
            view.showErrorMessage("File not found.");
        }
    }

    /**
     * Saves all open portfolios to a file.
     *
     * @param saveFile - File to be saved to.
     * */
    public void save(File saveFile){
        System.out.println(portfolioList.size());
        try {
            FileWriter fileInput = new FileWriter(saveFile, true);
            for (int j = 0; j < portfolioList.size(); j++){
                String printString = portfolioList.get(j).getName();
                JTable table = view.getPortfolios().get(j);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int stocksize = view.getPortfolios().get(j).getRowCount();
                for (int i = 0; i < stocksize; i++){
                    printString = printString + "," + model.getValueAt(i,0) + "," + model.getValueAt(i,2) + "," + model.getValueAt(i, 4) + ","
                            + model.getValueAt(i, 5) + "," + portfolioList.get(j).getStock(model.getValueAt(i,0).toString()).getPreviousPrice();
                }
                fileInput.write(printString+ "\n");
            }
            fileInput.close();
        } catch (IOException e) {
            view.showErrorMessage("Error saving to file.");
        }
    }

    /**
     * Clears a file.
     *
     * @param saveFile - File to be cleared.
     * */
    public void clearFile(File saveFile) {
        try {
            FileWriter fileWriter = new FileWriter(saveFile);
            fileWriter.write("");
            fileWriter.close();
        } catch (IOException e) {
            view.showErrorMessage("Error clearing file.");
        }
    }
    
    public ArrayList<Portfolios> getPortfoliosList() {
        return  portfolioList;
    }

    public FolioView getView(){
        return view;
    }

    /**
     * Main driver for the program.
     * */
    public static void main(String[] args){
        FolioController controller = new FolioController();
        controller.initController();
    }
}


