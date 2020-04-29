package View;

import Exceptions.NoPortfoliosException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

public class FolioView {
    private JFrame rootFrame;
    private JTabbedPane portfolioTabs;
    private JScrollPane stockPanel;
    private JTable stockTracker;
    private JMenuBar topMenuBar;
    private JTextField tickerField;
    private JLabel tickerFieldLabel;
    private JTextField amountField;
    private JLabel amountFieldLabel;
    private JButton buyButton;
    private JButton sellButton;
    private JMenuBar bottomMenuBar;
    private JLabel spentLabel;
    private JLabel totalSpentCurrentPortfolio;
    private ArrayList<JTable> tables;
    private JButton newTabButton;
    private JButton deleteButton;
    private JButton removeButton;
    private JButton rename;
    private JTextField renamefield;

    private JButton refresh;
    private JMenuItem save;
    private JMenuItem load;
    private JMenuItem clear;

    /**
     *  FolioView constructor, creates the user interface and instantiates the arraylist of tables.
     **/
    public FolioView(){
        tables = new ArrayList<JTable>();
        this.makeFrame();
    }

    /**
     * Creates the root frame, adding the items in various panels to the frame and then displaying.
     **/
    public void makeFrame() {
        rootFrame = new JFrame("Portfolio Tracker");
        FlowLayout layout = new FlowLayout();
        JPanel rootPanel = new JPanel(layout);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        load = new JMenuItem("Load Saved Portfolios");
        save = new JMenuItem("Save Open Portfolios");
        clear = new JMenuItem("Clear Saved Portfolios");
        menu.add(save);
        menu.add(load);
        menu.add(clear);
        menuBar.add(menu);
        rootFrame.setJMenuBar(menuBar);

        makeTopBar(rootPanel);

        newTabButton = new JButton("New Portfolio");
        deleteButton = new JButton("Delete Portfolio");
        portfolioTabs = new JTabbedPane();

        portfolioTabs.setPreferredSize(new Dimension(700, 450));

        rootPanel.add(newTabButton);
        rootPanel.add(deleteButton);

        rootPanel.add(portfolioTabs);

        renamefield = new JTextField();
        rename = new JButton("Rename Current Portfolio");
        renamefield.setPreferredSize(new Dimension(475, 40));
        removeButton = new JButton("Remove Selected Shares");
        refresh = new JButton("Refresh Shares");
        rootPanel.add(renamefield);
        rootPanel.add(rename);
        rootPanel.add(removeButton);
        rootPanel.add(refresh);
        makeBottomBar(rootPanel);
        rootFrame.add(rootPanel);
        rootFrame.setSize(750, 775);
        rootFrame.setResizable(false);
        rootFrame.setLocationRelativeTo(null);
        rootFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rootFrame.show();
    }

    /**
     * Creates the bar below the file menu bar, containing the ticker field and amount field.
     *
     * @param panel is the panel for containing the items.
     * */
    private void makeTopBar(JPanel panel) {
        topMenuBar = new JMenuBar();
        tickerFieldLabel = new JLabel("Ticker: ");
        topMenuBar.add(tickerFieldLabel);

        tickerField = new JTextField();
        topMenuBar.add(tickerField);

        amountFieldLabel = new JLabel("Amount: ");
        topMenuBar.add(amountFieldLabel);

        amountField = new JTextField();
        topMenuBar.add(amountField);

        buyButton = new JButton("Buy");
        topMenuBar.add(buyButton);

        topMenuBar.setPreferredSize(new Dimension(730, 100));
        panel.add(topMenuBar);
    }

    /**
     * Creates the bar at the bottom of the window containing the statistics regarding the overall and individual portfolio(s).
     *
     * @param panel is the panel for containing the items.
     * */
    private void makeBottomBar(JPanel panel) {
        bottomMenuBar = new JMenuBar();

        spentLabel = new JLabel("Total Amount Invested: 0 ");
        bottomMenuBar.add(spentLabel);

        totalSpentCurrentPortfolio = new JLabel("");
        bottomMenuBar.add(totalSpentCurrentPortfolio);

        bottomMenuBar.setPreferredSize(new Dimension(710, 50));
        panel.add(bottomMenuBar);
    }

    /**
     * Adds a new tab to the JTabbedPane, along with adding a table to that tab.
     *
     * @param panel is the panel for containing the items.
     * @param portfolioTabs is the JTabbedPane containing each tab.
     * */
    public void addFolioTab(JPanel panel, JTabbedPane portfolioTabs) {
        Object[] tableHeaders = {"Ticker", "Price", "Number of Shares", "Value of Holding", "Lowest Price", "Highest Price", "Fluctuation"};
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 2) {
                    return true;
                }
                return false;
            }
        };
        model.setColumnIdentifiers(tableHeaders);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        stockTracker = table;
        tables.add(table);
        TableColumn column = null;
        for(int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(60);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tabContainer = new JPanel(new BorderLayout());
        scrollPane.setPreferredSize(new Dimension(600, 350));
        tabContainer.add(scrollPane,BorderLayout.PAGE_START);
        panel.add(tabContainer);
    }

    /**
     * Used to display an error message to the user in a JOptionPane.
     *
     * @param message is the error message to be displayed.
     * */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Updates the label displaying the total amount invested across all portfolios
     *
     * @param amount is the error message to be displayed.
     * */
    public void updateSpentLabel(float amount) {
        spentLabel.setText("Total Amount Invested: " + amount + " ");
    }

    /**
     * Updates the label displaying the total amount invested in an individual portfolio, if the amount is != 0
     *
     * @param amount is the error message to be displayed.
     * */
    public void updateCurrentPortfolioSpentLabel(float amount) {
        if(amount == 0) {
            totalSpentCurrentPortfolio.setText("");
        } else {
            totalSpentCurrentPortfolio.setText("| Total of Current Portfolio: " + amount);
        }
    }

    public JButton getBuyButton() {
        return buyButton;
    }

    public JTextField getTickerField() {
        return tickerField;
    }

    public JTextField getAmountField() {
        return amountField;
    }

    public JButton getNewTabButton() {
        return newTabButton;
    }

    public JButton getRename() {
        return rename;
    }

    public JTextField getRenamefield() {
        return renamefield;
    }

    public int getCurrentPortfolio() throws NoPortfoliosException {
        try {
            return portfolioTabs.getSelectedIndex();
        } catch(Exception e){
            throw new NoPortfoliosException("No portfolios to buy into");
        }
    }

    public ArrayList<JTable> getTable() {
        return tables;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JTabbedPane getPortfolioTabs() {
        return portfolioTabs;
    }

    public ArrayList<JTable> getPortfolios(){ return tables;}

    public JButton getRefresh() {
        return refresh;
    }

    public JMenuItem getSave() {
        return save;
    }

    public JMenuItem getLoad() {
        return load;
    }

    public JMenuItem getClear(){ return clear;}
}
