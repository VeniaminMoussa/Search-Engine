/*
 * Βενιαμίν Μούσσα
 * Α.Μ: 2022201700115
 * username: dit17115
 * email: dit17115@uop.gr
 *
 * https://sourceforge.net/projects/opencsv/files/latest/download
 *
 *https://commons.apache.org/proper/commons-lang/download_lang.cgi
 *
 *https://sourceforge.net/projects/jdatepicker/files/latest/download
 */
package trashluceneproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Lenovo
 */
public class TrashFrame extends JFrame{
    protected Boolean more = false;
    protected  Indexer indexer = null;
    protected  Searcher searcher = null;
    protected int indexNum = 0;
    
    //Elementes for the Menu
    private JMenu postsMenu;
    private JMenuItem insertMenuItem;
    private JMenuItem clearMenuItem;
    private JMenu helpMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;
    private JFileChooser fileChooser = new JFileChooser();
    
    //Elementes for the Search
    private Box trashNorthBox = new Box(BoxLayout.PAGE_AXIS);
    private Box searchBox = new Box(BoxLayout.PAGE_AXIS);
    
    private List<String> historySearch = new LinkedList<String>();
    private int searchIndex = -1;
    private JButton previousButton = new JButton("Previous");
    private JButton nextButton = new JButton("   Next   ");
    private JPanel historySearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
//    private JPanel historySearchPanel = new JPanel(new GridLayout());
//    private JPanel historyPreviousPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
//    private JPanel historyNextPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    
    private JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel searchBarFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel searchBarPanel = new JPanel(new BorderLayout());
    private JPanel searchTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    private JLabel logoLabel = new JLabel(new ImageIcon("Logos/trash.png"));
    private JTextField searchTextField = new JTextField(400);
    
    private JButton searchButton = new JButton("Search");
    
    private JToggleButton allButton = new JToggleButton("All");
    private JToggleButton statusButton = new JToggleButton("Status");
    private JToggleButton photoButton = new JToggleButton("Photo");
    private JToggleButton videoButton = new JToggleButton("Video");
    private JToggleButton linkButton = new JToggleButton("Link");
    
    private String type = null;
    
    //Elementes for the Filters
    private Boolean validDates = true;
    
    private Box filterItemsBox = new Box(BoxLayout.PAGE_AXIS);
    private Box filtersBox = new Box(BoxLayout.LINE_AXIS);
    private Box filterAreaBox = new Box(BoxLayout.LINE_AXIS);
    private JScrollPane filterScrollPane = new JScrollPane(filtersBox);
    private Box filtersWithHeaderBox = new Box(BoxLayout.PAGE_AXIS);
    private JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    private JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JTextField topTextField = new JTextField(8);
    
    private JPanel DatePanel = new JPanel(new GridLayout(0,1));
    
    private JPanel fromDatePanel = new JPanel();
    private JLabel fromDateLabel = new JLabel("From:");
    private UtilDateModel fromDateModel = new UtilDateModel();
    private JPanel toDatePanel = new JPanel();
    private JLabel toDateLabel = new JLabel("To:    ");
    private UtilDateModel toDateModel = new UtilDateModel();
    
    private List<JPanel> myToFilterListPanel = new LinkedList<>();
    private List<JLabel> myToFilterListLabel = new LinkedList<>();
    private List<JTextField> myToFilterListText = new LinkedList<>();
    
    private List<JPanel> myFromFilterListPanel = new LinkedList<>();
    private List<JLabel> myFromFilterListLabel = new LinkedList<>();
    private List<JTextField> myFromFilterListText = new LinkedList<>();
    
    private List<JPanel> myFilterListPanel = new LinkedList<>();
    
    //Elementes for the Results
    protected List<Post> postsResults = new LinkedList<Post>();
    private JPanel resultsPanel = new JPanel(new BorderLayout());
    protected DefaultListModel<String> myListModel = new DefaultListModel<>();
    private JList<String> myList = new JList<>(myListModel);
    private JScrollPane resultScrollPane = new JScrollPane(myList);
    
    //East empty space
    private Box eastBox = new Box(BoxLayout.LINE_AXIS);
    
    //Elements for the SOUNTH Area
    private JButton showButton = new JButton("Show Post");
    private JButton deleteButton = new JButton("Delete Post");
    private PostDialog postInfo;
    
    private JPanel postButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    public TrashFrame(){
        
        SwingUtilities.updateComponentTreeUI(TrashFrame.this);
        
        try {
            indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);
            indexer.close();
            searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
            indexNum = searcher.numDocs();
            searcher.close();
        } catch (IOException ex) {
            Logger.getLogger(TrashFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setSize(1200, 760);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Trump Automated Search Hub");
        this.setMenuItemsFrame();
        this.setLayout(new BorderLayout());
        
        //Inserting the NORTH Part's Elements of the window
        this.previousButton.addActionListener(new ClicHistorySearchListener());
        this.nextButton.addActionListener(new ClicHistorySearchListener());
        this.previousButton.setName("N");
        this.nextButton.setName("N");
        this.previousButton.setEnabled(false);
        this.nextButton.setEnabled(false);
        this.historySearchPanel.add(this.previousButton);
        this.historySearchPanel.add(this.nextButton);
        
        this.logoPanel.add(logoLabel);
        
        this.searchButton.addActionListener(new SearchButtonListener());
        this.searchTextField.setColumns(30);
             
        this.searchBarPanel.add(searchTextField,BorderLayout.CENTER);
        this.searchBarPanel.add(searchButton,BorderLayout.EAST);
        
        this.searchBarFlowPanel.add(searchBarPanel);

        this.allButton.addItemListener(new TypeItemListener());
        this.photoButton.addItemListener(new TypeItemListener());
        this.videoButton.addItemListener(new TypeItemListener());
        this.linkButton.addItemListener(new TypeItemListener());
        this.statusButton.addItemListener(new TypeItemListener());
        
        this.photoButton.setName("photo");
        this.videoButton.setName("video");
        this.linkButton.setName("link");
        this.statusButton.setName("status");
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allButton);
        buttonGroup.add(statusButton);
        buttonGroup.add(photoButton);
        buttonGroup.add(videoButton);
        buttonGroup.add(linkButton);
        buttonGroup.add(statusButton);
        
        this.searchTypePanel.add(allButton);
        this.searchTypePanel.add(statusButton);
        this.searchTypePanel.add(photoButton);
        this.searchTypePanel.add(videoButton);
        this.searchTypePanel.add(linkButton);
        
        
        this.allButton.doClick();
        
        this.searchBox.add(searchBarFlowPanel);
        this.searchBox.add(searchTypePanel);
                
        this.trashNorthBox.add(historySearchPanel);
        this.trashNorthBox.add(logoPanel);
        this.trashNorthBox.add(searchBox);
                
        this.add(trashNorthBox,BorderLayout.NORTH);
        
        //Inserting the CENTER Part's Elements of the window
        
        this.myList.setFixedCellHeight(35);
        this.myList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       
        this.resultsPanel.add(resultScrollPane);
        this.add(resultsPanel,BorderLayout.CENTER);
        
        this.myList.addListSelectionListener(new ResultsListSelectionListener());

        //Inserting the WEST Part's Elements of the window (Filters)
        this.topTextField.setText(String.valueOf(LuceneConstants.MAX_SEARCH));
        this.topTextField.setBackground(Color.decode("#E2FFCE"));
        this.topTextField.setName("Y");
        this.topTextField.addFocusListener(new TopFilterFocusListener());
        this.topPanel.add(this.topTextField);
        this.topPanel.setBorder(BorderFactory.createTitledBorder("Top Results"));
        
        this.filterItemsBox.add(this.topPanel);
        
        this.fromDatePanel.add(this.fromDateLabel);
        this.fromDateModel.addChangeListener(new DateChangeListener());
        JDatePanelImpl fromDatePanelImpl = new JDatePanelImpl(this.fromDateModel, new Properties());
        JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanelImpl,  new DateLabelFormatter());
        fromDatePicker.setPreferredSize(new Dimension(110,30));
        this.fromDatePanel.add(fromDatePicker);
        this.DatePanel.add(this.fromDatePanel);
        
        this.toDatePanel.add(this.toDateLabel);
        this.toDateModel.addChangeListener(new DateChangeListener());
        JDatePanelImpl toDatePanelImpl = new JDatePanelImpl(this.toDateModel, new Properties());
        JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanelImpl,  new DateLabelFormatter());
        toDatePicker.setPreferredSize(new Dimension(110,30));
        this.toDatePanel.add(toDatePicker);
        this.DatePanel.add(this.toDatePanel);
        
        this.DatePanel.setBorder(BorderFactory.createTitledBorder("Date"));
        
        this.filterItemsBox.add(DatePanel);
        
        for(int i=0;i < 9;i++){
            this.myFromFilterListPanel.add(new JPanel());
            this.myFromFilterListLabel.add(new JLabel("From:"));
            this.myFromFilterListText.add(new JTextField(5));
            
            this.myToFilterListPanel.add(new JPanel());
            this.myToFilterListLabel.add(new JLabel("To:"));
            this.myToFilterListText.add(new JTextField(5));
            
            this.myFilterListPanel.add(new JPanel(new GridLayout(1,0)));
            
            this.myFromFilterListPanel.get(i).add(this.myFromFilterListLabel.get(i));
            this.myFromFilterListPanel.get(i).add(this.myFromFilterListText.get(i));
            this.myFromFilterListText.get(i).addFocusListener(new FilterFocusListener());
            this.myFromFilterListText.get(i).setName("Y");
            this.myToFilterListPanel.get(i).add(this.myToFilterListLabel.get(i));
            this.myToFilterListPanel.get(i).add(this.myToFilterListText.get(i));
            this.myToFilterListText.get(i).addFocusListener(new FilterFocusListener());
            this.myToFilterListText.get(i).setName("Y");
            
            this.myFilterListPanel.get(i).add(this.myFromFilterListPanel.get(i));
            this.myFilterListPanel.get(i).add(this.myToFilterListPanel.get(i));
            
            this.filterItemsBox.add(this.myFilterListPanel.get(i));
        }
        
        this.myFilterListPanel.get(0).setBorder(BorderFactory.createTitledBorder("Reactions"));
        this.myFilterListPanel.get(1).setBorder(BorderFactory.createTitledBorder("Comments"));
        this.myFilterListPanel.get(2).setBorder(BorderFactory.createTitledBorder("Shares"));
        this.myFilterListPanel.get(3).setBorder(BorderFactory.createTitledBorder("Likes"));
        this.myFilterListPanel.get(4).setBorder(BorderFactory.createTitledBorder("Loves"));
        this.myFilterListPanel.get(5).setBorder(BorderFactory.createTitledBorder("Wows"));
        this.myFilterListPanel.get(6).setBorder(BorderFactory.createTitledBorder("Hahas"));
        this.myFilterListPanel.get(7).setBorder(BorderFactory.createTitledBorder("Sads"));
        this.myFilterListPanel.get(8).setBorder(BorderFactory.createTitledBorder("Angrys"));
        
        this.filtersBox.add(Box.createRigidArea(new Dimension(10,0)));
        this.filtersBox.add(filterItemsBox);
        this.filtersBox.add(Box.createRigidArea(new Dimension(23,0)));
        
        this.filterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.filterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.filterScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(13, 0));
        
        this.filterPanel.add(new JLabel("Filters"));
        this.filterPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.filtersWithHeaderBox.add(filterPanel);
        this.filtersWithHeaderBox.add(filterScrollPane);
        
        this.filterAreaBox.add(Box.createRigidArea(new Dimension(15,0)));
        this.filterAreaBox.add(filtersWithHeaderBox);
        this.filterAreaBox.add(Box.createRigidArea(new Dimension(15,0)));
        
        this.add(filterAreaBox,BorderLayout.WEST);
        
        //East empty space
        this.eastBox.add(Box.createRigidArea(new Dimension(200,200)));
        this.add(eastBox,BorderLayout.EAST);
        
        //Inserting the SOUTH Part's Elements of the window
        this.showButton.addActionListener(new ClickActionListener());
        this.deleteButton.addActionListener(new ClickActionListener());
        
        this.showButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        
        this.postButtonsPanel.add(this.showButton);
        this.postButtonsPanel.add(this.deleteButton);
        
        this.add(postButtonsPanel,BorderLayout.SOUTH);
    }

    public void setMenuItemsFrame(){
        
        insertMenuItem = new JMenuItem("Insert to index");
        insertMenuItem.addActionListener(new ClickMenuListener());
        
        clearMenuItem = new JMenuItem("Clear index");
        clearMenuItem.addActionListener(new ClickMenuListener());

        postsMenu = new JMenu("Posts");
        postsMenu.add(insertMenuItem);
        postsMenu.add(clearMenuItem);
        
        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(new ClickMenuListener());
        
        aboutMenuItem = new JMenuItem("About TRASH");
        aboutMenuItem.addActionListener(new ClickMenuListener());
      
        helpMenu = new JMenu("Help");
        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(postsMenu);
        menuBar.add(helpMenu);
        
        this.setJMenuBar(menuBar);
        
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(true); 
        this.fileChooser.setAcceptAllFileFilterUsed(false);
        this.fileChooser.addChoosableFileFilter((new FileNameExtensionFilter("Comma Separated Values", "csv")));
    }
    private class ClicHistorySearchListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = e.getActionCommand();
            
            if(text.contains("Previous")){
                searchIndex--;
                String search = historySearch.get(searchIndex);
                searchTextField.setText(search);
                previousButton.setName("Y");
                searchButton.doClick();
            }
            else if(text.contains("Next")){
                searchIndex++;
                String search = historySearch.get(searchIndex);
                searchTextField.setText(search);
                nextButton.setName("Y");
                searchButton.doClick();
            }
        }
    }
    
    private class ClickMenuListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = e.getActionCommand();
            
            if(text.equals("Insert to index")){
                
                if (fileChooser.showOpenDialog(TrashFrame.this) == JFileChooser.APPROVE_OPTION){
                    try {
                        String results = null;
                        File[] selectedFiles = fileChooser.getSelectedFiles();
                        
                        createIndex(selectedFiles);
                        
                        myListModel.removeAllElements();
                        postsResults.clear();
                        
                        for (File file : selectedFiles){
                            results+=file.getAbsolutePath()+"\n";
                            System.out.println("Selected file: " + file.getAbsolutePath());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TrashFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(TrashFrame.this, "No files has been selected", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if(text.equals("Clear index")){
                try {
                    indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);
                    indexer.clear();
                    indexer.close();
                } catch (IOException ex) {
                    Logger.getLogger(TrashFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                myListModel.removeAllElements();
                postsResults.clear();
                
                JOptionPane.showMessageDialog(rootPane, "Index has been cleared"
                        , "Information Message",JOptionPane.INFORMATION_MESSAGE);
            }
            else if(text.equals("Help")){
                JOptionPane.showMessageDialog(rootPane, "The TraSH engine (Trump Automated Search Hub), is a search engine that supports storage,\n" +
                        "index and search Facebook posts of former US President Donald J. Trump,\n" +
                        "as it is a pity (#not_really) to lose so shiny and torrent internet speech."
                        , "Information Message",JOptionPane.INFORMATION_MESSAGE);
            }
            else if(text.equals("About TRASH")){
                JOptionPane.showMessageDialog(rootPane, "The search engine \"TraSH\" was developed by the Students Benjamin Moussa and Alexandra Kanta\n"+
                                            "from The Department of Informatics and Telecomunications of The University of Peloponnese as a Project,\n"+
                                            "for the course Information retrieval and data mining, guided by the supervisor Assistant Professor Christos Tryfonopoulos.",
                                            "Information Message",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private class SearchButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            String str = searchTextField.getText();
            try{
                searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
                indexNum = searcher.numDocs();
                myListModel.removeAllElements();
                postsResults.clear();
                
                if(indexNum == 0)
                    throw new Exception("There hasn't been a File(*.csv) Imported to Index");
                
                if(topTextField.getName().compareToIgnoreCase("N")==0)
                        throw new Exception("No Top-Results Input in Filters");
                
                if (str.length() == 0)
                    throw new Exception("No Query Found");
                
                if(!validDates)
                    throw new Exception("Invalid Input in Filters");
                
                for(int i=0;i<myFromFilterListText.size();i++){
                    if(myFromFilterListText.get(i).getName().compareToIgnoreCase("N")==0)
                        throw new Exception("Invalid Input in Filters");
                    if(myToFilterListText.get(i).getName().compareToIgnoreCase("N")==0)
                        throw new Exception("Invalid Input in Filters");
                }
                
                if(previousButton.getName().compareToIgnoreCase("Y")==0){
                    previousButton.setName("N");
                    if(searchIndex==0){
                        previousButton.setEnabled(false);
                    }
                    nextButton.setEnabled(true);
                }
                else if(nextButton.getName().compareToIgnoreCase("Y")==0){
                    nextButton.setName("N");
                    if(searchIndex==historySearch.size()-1){
                        nextButton.setEnabled(false);
                    }
                    previousButton.setEnabled(true);
                }
                else{
                    searchIndex++;
                    if(searchIndex>0){
                        previousButton.setEnabled(true);
                    }
                    if(searchIndex != historySearch.size()){
                        searchIndex--;
                        historySearch.subList(searchIndex, historySearch.size()).clear();
                    }
                    
                    System.out.println(str);
                    historySearch.add(str);
                    nextButton.setEnabled(false);
                }
                
                if(str.contains("www")){
//                    str = LuceneConstants.CONSTANT.get(3) + ": " + "(((\"http://\") OR (\"https://\"))" + str + "*) OR " + str + "*";
                    str ="("+ LuceneConstants.CONSTANT.get(3) + ": " + "*" + str + "*)";
                }
                else{
                    String strText = str;
//                    String strText = "\"" + str + "\"";
                    String strTextTitle = "(" + strText + "^2 " + " AND " + LuceneConstants.CONSTANT.get(1) + ": " + strText + "^4)";
                    String strTextLink = "(" + strText + "^4" + " AND " + "(" + LuceneConstants.CONSTANT.get(3) + ": " + "*" + str + "*" + ")^2)";
                    
                    str = "(" + strText + "^6" + " OR " + strTextTitle + "^4" + " OR " + strTextLink + "^2" + ")";
                }
                
                if(type !=null){
                    str = str + " AND status_type: " + type;
                }
                
                if(fromDateModel.getValue()!=null && toDateModel.getValue()!=null){
                    Long fromdate = DateTools.stringToTime(DateTools.dateToString(fromDateModel.getValue(), DateTools.Resolution.DAY));
                    Long todate = DateTools.stringToTime(DateTools.dateToString(toDateModel.getValue(), DateTools.Resolution.DAY));
                    str+=" AND " + "status_published:[" 
                                + fromdate + " TO "
                                + todate + "]";
                }
                else if(fromDateModel.getValue()==null && toDateModel.getValue()!=null){
                    Long todate = DateTools.stringToTime(DateTools.dateToString(toDateModel.getValue(), DateTools.Resolution.DAY));
                    str+=" AND " + "status_published:[" 
                                + "0" + " TO "
                                + todate + "]";
                    
                }
                else if(fromDateModel.getValue()!=null && toDateModel.getValue()==null){
                    Long fromdate = DateTools.stringToTime(DateTools.dateToString(fromDateModel.getValue(), DateTools.Resolution.DAY));
                    str+=" AND " + "status_published:[" 
                                + fromdate + " TO "
                                + System.currentTimeMillis() + "]";
                }
                
                //Edit the string and Search with the new Query
                for(int i=0;i<myFromFilterListText.size();i++){
                    if(myFromFilterListText.get(i).getText().length()!=0
                        && myToFilterListText.get(i).getText().length()!=0){
                        str+=" AND " + LuceneConstants.CONSTANT.get(i+5) + ":[" 
                                + myFromFilterListText.get(i).getText() + " TO "
                                + myToFilterListText.get(i).getText() + "]";
                    }
                    else if(myFromFilterListText.get(i).getText().length()==0
                        && myToFilterListText.get(i).getText().length()!=0){
                        str+=" AND " + LuceneConstants.CONSTANT.get(i+5) + ":[" 
                                + "0" + " TO "
                                + myToFilterListText.get(i).getText() + "]";
                    }
                    else if(myFromFilterListText.get(i).getText().length()!=0
                        && myToFilterListText.get(i).getText().length()==0){
                        str+=" AND " + LuceneConstants.CONSTANT.get(i+5) + ":[" 
                                + myFromFilterListText.get(i).getText() + " TO "
                                + "99999999999999999999999999999999999999999999999999999" +"]";
                    }
                }

                search(str);
                searcher.close();
                
            }catch(Exception ex){
                JOptionPane.showMessageDialog(TrashFrame.this, ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class TypeItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie)
        {
            // Μέσω της παραμέτρου ItemEvent μπορούμε να βρούμε ποιο
            // γραφικό συστατικό προκάλεσε το γεγονός αυτό (μέθοδος getItemSelectable())
            ItemSelectable is = ie.getItemSelectable();
            JToggleButton button = (JToggleButton)is;
            
            // Επίσης μπορούμε να βρούμε (απευθείας) την κατάσταση στην 
            // οποία κατέληξε το συστατικό (επιλεγμένο ή όχι)
            if (ie.getStateChange() == ItemEvent.SELECTED){
                type = button.getName();
                button.setForeground(Color.decode("#A20000"));
            }
            else {
                button.setForeground(Color.BLACK);
            }
        }
    }
    
    private class TopFilterFocusListener implements FocusListener
    {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField)e.getComponent()).setBackground(Color.WHITE);
        }

        @Override
        public void focusLost(FocusEvent e){
            String str = ((JTextField)e.getComponent()).getText();
            if (str.length() != 0){
                try{
                    Integer.parseInt(str);
                    ((JTextField)e.getComponent()).setBackground(Color.decode("#E2FFCE"));
                    ((JTextField)e.getComponent()).setName("Y");
                    LuceneConstants.MAX_SEARCH = Integer.parseInt(str);
                }catch(NumberFormatException ex){
                    ((JTextField)e.getComponent()).setName("N");
                    ((JTextField)e.getComponent()).setBackground(Color.decode("#FFCECE"));
                    JOptionPane.showMessageDialog(TrashFrame.this, "     ~Invalid Input~\n!!!Value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                ((JTextField)e.getComponent()).setName("N");
                ((JTextField)e.getComponent()).setBackground(Color.decode("#FFCECE"));
                JOptionPane.showMessageDialog(TrashFrame.this, "        ~No Input~\n!!!Value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public class DateChangeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e){
            try {
                if((fromDateModel.getValue() != null)&&(toDateModel.getValue() != null)){
                    
                    Long fromdate = DateTools.stringToTime(DateTools.dateToString(fromDateModel.getValue(), DateTools.Resolution.MILLISECOND));
                    Long todate = DateTools.stringToTime(DateTools.dateToString(toDateModel.getValue(), DateTools.Resolution.MILLISECOND));
                    
                    if(fromdate<=todate){
                        validDates = true;
                    }
                    else{
                        validDates = false;
                        JOptionPane.showMessageDialog(TrashFrame.this, "Confusing Dates", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(TrashFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public class DateLabelFormatter extends AbstractFormatter{

        private String datePattern = "MM/dd/yy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException{
            return dateFormatter.parseObject(text);
        }
        
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }
    
    private class FilterFocusListener implements FocusListener
    {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField)e.getComponent()).setBackground(Color.WHITE);
        }

        @Override
        public void focusLost(FocusEvent e){
            String str = ((JTextField)e.getComponent()).getText();
            if (str.length() != 0){
                try{
                    Integer.parseInt(str);
                    
                    try{
                        JPanel fromPanel = (JPanel)(((e.getComponent().getParent()).getParent()).getComponent(0));
                        JPanel toPanel = (JPanel)(((e.getComponent().getParent()).getParent()).getComponent(1));
                        JTextField fromTextField = ((JTextField)(fromPanel.getComponent(1)));
                        JTextField toTextField = ((JTextField)(toPanel.getComponent(1)));
                        String fromText = fromTextField.getText();
                        String toText = toTextField.getText();
                        if((fromText.length() != 0)&&(toText.length() != 0)){
                            if(Integer.parseInt(fromText)<=Integer.parseInt(toText)){
                                fromTextField.setName("Y");
                                toTextField.setName("Y");
                                fromTextField.setBackground(Color.decode("#E2FFCE"));
                                toTextField.setBackground(Color.decode("#E2FFCE"));
                            }
                            else{
                                fromTextField.setName("N");
                                toTextField.setName("N");
                                fromTextField.setBackground(Color.decode("#FFCECE"));
                                toTextField.setBackground(Color.decode("#FFCECE"));
                                JOptionPane.showMessageDialog(TrashFrame.this, " The \"From\" value is Bigger than the value \"To\"", "Warning", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else{
                            ((JTextField)e.getComponent()).setName("Y");
                            ((JTextField)e.getComponent()).setBackground(Color.decode("#E2FFCE"));
                        }
                    }catch(NumberFormatException ex){
                        ((JTextField)e.getComponent()).setName("N");
                        ((JTextField)e.getComponent()).setBackground(Color.decode("#FFCECE"));
                        JOptionPane.showMessageDialog(TrashFrame.this, "     ~Invalid Input~\n!!!Second value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(NumberFormatException ex){
                    ((JTextField)e.getComponent()).setName("N");
                    ((JTextField)e.getComponent()).setBackground(Color.decode("#FFCECE"));
                    JOptionPane.showMessageDialog(TrashFrame.this, "     ~Invalid Input~\n!!!Value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                try{
                    
                    ((JTextField)e.getComponent()).setName("Y");
                            
                    JPanel fromPanel = (JPanel)(((e.getComponent().getParent()).getParent()).getComponent(0));
                    JPanel toPanel = (JPanel)(((e.getComponent().getParent()).getParent()).getComponent(1));
                    JTextField fromTextField = ((JTextField)(fromPanel.getComponent(1)));
                    JTextField toTextField = ((JTextField)(toPanel.getComponent(1)));
                    String fromText = fromTextField.getText();
                    String toText = toTextField.getText();
                        
                    if(fromText.length() != 0){
                        Integer.parseInt(fromText);
                        fromTextField.setName("Y");
                        fromTextField.setBackground(Color.decode("#E2FFCE"));
                    }
                    if(toText.length() != 0){
                        Integer.parseInt(toText);
                        toTextField.setName("Y");
                        toTextField.setBackground(Color.decode("#E2FFCE"));
                    }
                }catch(NumberFormatException ex){}
            }
        }
    }
    
    private class ResultsListSelectionListener implements ListSelectionListener
    {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false)  {
                switch (myList.getSelectedIndices().length) {
                    case 1:
                        showButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        break;
                    case 0:
                        showButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        break;
                    default:
                        showButton.setEnabled(false);
                        deleteButton.setEnabled(true);
                        break;
                }
            }
        }
    }
    
    private class ClickActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = e.getActionCommand();
            
            if(text.equals("Show Post")){
                postInfo = new PostDialog(TrashFrame.this, postsResults.get(myList.getSelectedIndex()));
                postInfo.setVisible(true);
            }
            else if(text.equals("Delete Post")){
                try {
                    indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);
                    for (int i = myList.getSelectedIndices().length-1; i >= 0; i--)  {
                        String id = postsResults.get(myList.getSelectedIndices()[i]).getPostField(LuceneConstants.ID);
                        indexer.deleteDoc(id);
                        postsResults.remove(myList.getSelectedIndices()[i]);
                        myListModel.remove(myList.getSelectedIndices()[i]);
                    }
                    indexer.close();
                } catch (IOException ex) {
                    Logger.getLogger(TrashFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void createIndex(File[] files) throws IOException {
        
        indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);
        
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(files, new CSVFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        
        searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
        indexNum = searcher.numDocs();
        searcher.close();
        JOptionPane.showMessageDialog(TrashFrame.this,
                numIndexed + " new Documents indexed,\n" +
                indexNum + " Documents indexed overall, time taken: " +
                (endTime-startTime) + " ms",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
   }
    
    public Boolean search(String searchQuery) throws IOException {
        
        try{
            long startTime = System.currentTimeMillis();
            TopDocs hits = searcher.search(searchQuery);
            long endTime = System.currentTimeMillis();
            
            JOptionPane.showMessageDialog(TrashFrame.this,
                hits.totalHits + " documents found. Time :" + (endTime - startTime) + " ms",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            
            System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime) + " ms");
            
            for(ScoreDoc scoreDoc : hits.scoreDocs) {
                Post post = new Post();
                Document doc = searcher.getDocument(scoreDoc);
                post.addPostField(LuceneConstants.ID, doc.get(LuceneConstants.ID));
                for(int i=0;i<LuceneConstants.CONSTANT.size();i++){
                    post.addPostField(LuceneConstants.CONSTANT.get(i), doc.get(LuceneConstants.CONSTANT.get(i)));
                }
                JTextField JTextField  = new JTextField(doc.get(LuceneConstants.CONSTANT.get(0)));
                int index = JTextField.getText().indexOf(searchTextField.getText());
                int first = 0 , last = JTextField.getText().length()-1;
                String str = JTextField.getText();
                if(str.length()!=0){
                    if(index-50 >= 0){
                        first = index - 50;
                    }
                    if(index+50 < str.length()-1){
                        last = index + 50;
                    }
                    str = str.substring(first, last);
                    
                }
                
                if(first != 0){
                    str = "..." + str;
                }
                if(last != JTextField.getText().length()-1){
                    str = str + "...";
                }
                
                postsResults.add(post);
                
                myListModel.addElement("[" + scoreDoc.score + "]: " + str);
            }
            if(hits.totalHits.value != 0){
                myList.setSelectedIndex(0);
            }
            
        }catch(org.apache.lucene.queryparser.classic.ParseException e){
            return false;
        }
        
        return true;
    }
}

