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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.lucene.document.DateTools;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author nplatis
 */
public class PostDialog extends JDialog
{
    // Η κλάση JDialog χρησιμοποιείται για διαλόγους (δευτερεύοντα παράθυρα)
    // ενώ η JFrame χρησιμοποιείται μόνο για το κύριο παράθυρο της εφαρμογής.

    private Post post = null;
    private TrashFrame trashFrame = null;
    private SimpleDateFormat formater = new SimpleDateFormat ("MM/dd/yy hh:mm:ss");
    private Box linkBox = new Box(BoxLayout.PAGE_AXIS);
    
    private JPanel statusLinkPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    private JLabel statusLinkLabel = new JLabel();
        
    private JPanel showLinkNamePanel = new JPanel(new BorderLayout());
    private Box showLinkNameBox = new Box(BoxLayout.LINE_AXIS);
    private JPanel showNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JLabel showLinkNameLabel = new JLabel();
    
    private JPanel editLinkNamePanel = new JPanel(new BorderLayout());
    private Box editLinkNameBox = new Box(BoxLayout.LINE_AXIS);
    private JPanel editNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JTextField editLinkNameTextField = new JTextField(100);
    
    private JTextArea messageTextArea = new JTextArea();
    private JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
    
    private Box filtersInfoBox = new Box(BoxLayout.PAGE_AXIS);
    
    private Boolean validDates = true;
    
    private JPanel editDatePanel = new JPanel();
    private JLabel editDateLabel = new JLabel("Date:");
    private UtilDateModel editDateModel = new UtilDateModel();
    
    private Box showFiltersInfoBox = new Box(BoxLayout.PAGE_AXIS);
    
    private JPanel showDateFilterInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    private JLabel showDateFilterInfoFieldLabel = new JLabel("Date: ");
    private JLabel showDateFilterInfoValueLabel = new JLabel();
    
    private List<JPanel> showFiltersInfoListPanel = new LinkedList<>();
    private List<JPanel> showFiltersInfoFieldListPanel = new LinkedList<>();
    private List<JLabel> showFiltersInfoFieldLabel = new LinkedList<>();
    private List<JPanel> showFiltersInfoValueListPanel = new LinkedList<>();
    private List<JLabel> showFiltersInfoValueLabel = new LinkedList<>();
    
    private Box editFiltersInfoBox = new Box(BoxLayout.PAGE_AXIS);
    private List<JPanel> editFiltersInfoListPanel = new LinkedList<>();
    private List<JPanel> editFiltersInfoLabelListPanel = new LinkedList<>();
    private List<JLabel> editFiltersInfoLabel = new LinkedList<>();
    private List<JPanel> editFiltersInfoTextListPanel = new LinkedList<>();
    private List<JTextField> editFiltersInfoListText = new LinkedList<>();
    
    private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    private JButton editButton = new JButton("Edit");
    private JButton moreButton = new JButton("More Like This");
    private JButton saveButton = new JButton("Save");
    private JButton discardButton = new JButton("Discard");
    
    private Box eastEmptyBox = new Box(BoxLayout.LINE_AXIS);
    
    public PostDialog(TrashFrame frame, Post post)
    {
        super(frame, "Post", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(800, 500);
        this.setLayout(new BorderLayout());
        this.post = post;
        this.trashFrame = frame;
        
        this.statusLinkPanel.add(this.statusLinkLabel);
        this.statusLinkPanel.setBorder(BorderFactory.createEtchedBorder());
         
        this.showNamePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.showNamePanel.add(this.showLinkNameLabel);
        this.showLinkNameBox.add(Box.createHorizontalGlue());
        this.showLinkNameBox.add(this.showNamePanel);
        this.showLinkNameBox.add(Box.createHorizontalGlue());
        this.showLinkNamePanel.add(showLinkNameBox, BorderLayout.CENTER);
        
        this.editNamePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.editNamePanel.add(this.editLinkNameTextField);
        this.editLinkNameBox.add(Box.createHorizontalGlue());
        this.editLinkNameBox.add(this.editNamePanel);
        this.editLinkNameBox.add(Box.createHorizontalGlue());
        this.editLinkNamePanel.add(editLinkNameBox, BorderLayout.CENTER);
        this.editLinkNameTextField.setColumns(25);
        
        this.linkBox.add(this.statusLinkPanel);
        this.linkBox.add(this.showLinkNamePanel);
        this.linkBox.add(this.editLinkNamePanel);
        
        this.messageTextArea.setLineWrap(true);
        this.messageTextArea.setWrapStyleWord(true);
        this.messageTextArea.setEditable(false);
        
        this.messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        this.showDateFilterInfoPanel.add(this.showDateFilterInfoFieldLabel);
        this.showDateFilterInfoPanel.add(this.showDateFilterInfoValueLabel);
        this.showDateFilterInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.showFiltersInfoBox.add(this.showDateFilterInfoPanel);
        
        this.showFiltersInfoFieldLabel.add(new JLabel("Reactions: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Comments: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Shares: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Likes: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Loves: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Wows: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Hahas: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Sads: "));
        this.showFiltersInfoFieldLabel.add(new JLabel("Angrys: "));
        
        for(int i=0;i < 9;i++){
            this.showFiltersInfoListPanel.add(new JPanel(new GridLayout()));
            this.showFiltersInfoFieldListPanel.add(new JPanel(new FlowLayout(FlowLayout.LEADING)));
            this.showFiltersInfoFieldListPanel.get(i).add(this.showFiltersInfoFieldLabel.get(i));
            this.showFiltersInfoValueLabel.add(new JLabel());
            this.showFiltersInfoValueListPanel.add(new JPanel(new FlowLayout(FlowLayout.TRAILING)));
            this.showFiltersInfoValueListPanel.get(i).add(this.showFiltersInfoValueLabel.get(i));
            this.showFiltersInfoListPanel.get(i).add(this.showFiltersInfoFieldListPanel.get(i));
            this.showFiltersInfoListPanel.get(i).add(this.showFiltersInfoValueListPanel.get(i));
            this.showFiltersInfoListPanel.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.showFiltersInfoBox.add(this.showFiltersInfoListPanel.get(i));
        }
        
        this.editDatePanel.add(this.editDateLabel);
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(this.editDateModel, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanelImpl,  new PostDialog.DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(110,30));
        this.editDatePanel.add(datePicker);
        
        this.editFiltersInfoBox.add(this.editDatePanel);
        
        this.editFiltersInfoLabel.add(new JLabel("Reactions: "));
        this.editFiltersInfoLabel.add(new JLabel("Comments: "));
        this.editFiltersInfoLabel.add(new JLabel("Shares: "));
        this.editFiltersInfoLabel.add(new JLabel("Likes: "));
        this.editFiltersInfoLabel.add(new JLabel("Loves: "));
        this.editFiltersInfoLabel.add(new JLabel("Wows: "));
        this.editFiltersInfoLabel.add(new JLabel("Hahas: "));
        this.editFiltersInfoLabel.add(new JLabel("Sads: "));
        this.editFiltersInfoLabel.add(new JLabel("Angrys: "));
        
        for(int i=0;i < 9;i++){
            this.editFiltersInfoListPanel.add(new JPanel(new GridLayout()));
            this.editFiltersInfoLabelListPanel.add(new JPanel(new FlowLayout(FlowLayout.LEADING)));
            this.editFiltersInfoLabelListPanel.get(i).add(this.editFiltersInfoLabel.get(i));
            this.editFiltersInfoListText.add(new JTextField(5));
            this.editFiltersInfoListText.get(i).addFocusListener(new FiltersFocusListener());
            this.editFiltersInfoListText.get(i).setBackground(Color.decode("#E2FFCE"));
            this.editFiltersInfoListText.get(i).setName("Y");
            this.editFiltersInfoTextListPanel.add(new JPanel(new FlowLayout(FlowLayout.TRAILING)));
            this.editFiltersInfoTextListPanel.get(i).add(this.editFiltersInfoListText.get(i));
            this.editFiltersInfoListPanel.get(i).add(this.editFiltersInfoLabelListPanel.get(i));
            this.editFiltersInfoListPanel.get(i).add(this.editFiltersInfoTextListPanel.get(i));
            this.editFiltersInfoBox.add(this.editFiltersInfoListPanel.get(i));
        }
        
        this.filtersInfoBox.add(this.showFiltersInfoBox);
        this.filtersInfoBox.add(this.editFiltersInfoBox);
        
        
        this.buttonsPanel.add(this.moreButton);
        this.buttonsPanel.add(this.editButton);
        this.buttonsPanel.add(this.saveButton);
        this.buttonsPanel.add(this.discardButton);
        
        this.moreButton.addActionListener(new ClickActionListener());
        this.editButton.addActionListener(new ClickActionListener());
        this.saveButton.addActionListener(new ClickActionListener());
        this.discardButton.addActionListener(new ClickActionListener());
        
        this.eastEmptyBox.add(Box.createRigidArea(new Dimension(100,100)));
        
        this.add(this.eastEmptyBox, BorderLayout.EAST);
        this.add(this.linkBox, BorderLayout.NORTH);
        this.add(this.messageScrollPane, BorderLayout.CENTER);
        this.add(this.filtersInfoBox, BorderLayout.WEST);
        this.add(this.buttonsPanel, BorderLayout.SOUTH);
        
        this.showMode();
    }
    
    private void editMode(){
        
        Date date = new Date(Long.parseLong(this.post.getPostField(LuceneConstants.CONSTANT.get(4))));
        this.editDateModel.setValue(date);
        
        this.editLinkNameTextField.setText(this.post.getPostField(LuceneConstants.CONSTANT.get(1)));
        
        for(int i=0;i < 9;i++){
            this.editFiltersInfoListText.get(i).setText(this.post.getPostField(LuceneConstants.CONSTANT.get(i+5)));
            this.editFiltersInfoListText.get(i).setBackground(Color.decode("#E2FFCE"));
            this.editFiltersInfoListText.get(i).setName("Y");
        }
        
        this.showLinkNamePanel.setVisible(false);
        this.editLinkNamePanel.setVisible(true);
        
        this.showFiltersInfoBox.setVisible(false);
        this.editFiltersInfoBox.setVisible(true);
        
        this.messageTextArea.setEditable(true);
        
        this.editButton.setVisible(false);
        this.saveButton.setVisible(true);
        this.moreButton.setVisible(false);
        this.discardButton.setVisible(true);
        
        SwingUtilities.updateComponentTreeUI(PostDialog.this);
    }
    
    private void showMode(){
        
        this.statusLinkLabel.setText("Link: " + this.post.getPostField(LuceneConstants.CONSTANT.get(3)));
        this.showLinkNameLabel.setText(this.post.getPostField(LuceneConstants.CONSTANT.get(1)));
        this.messageTextArea.setText(this.post.getPostField(LuceneConstants.CONSTANT.get(0)));
        this.showDateFilterInfoValueLabel.setText(formater.format(new Date(Long.parseLong(this.post.getPostField(LuceneConstants.CONSTANT.get(4))))));
        
        for(int i=0;i < 9;i++){
            this.showFiltersInfoValueLabel.get(i).setText(this.post.getPostField(LuceneConstants.CONSTANT.get(i+5)));
        }
        
        this.showLinkNamePanel.setVisible(true);
        this.editLinkNamePanel.setVisible(false);
        
        this.showFiltersInfoBox.setVisible(true);
        this.editFiltersInfoBox.setVisible(false);
        
        this.messageTextArea.setEditable(false);
        
        this.editButton.setVisible(true);
        this.saveButton.setVisible(false);
        this.moreButton.setVisible(true);
        this.discardButton.setVisible(false);
        
        SwingUtilities.updateComponentTreeUI(PostDialog.this);
    }
    
    private void saveMode(){
        
        try {
            this.post.setPostField(LuceneConstants.CONSTANT.get(0), this.messageTextArea.getText());
            this.post.setPostField(LuceneConstants.CONSTANT.get(1), this.editLinkNameTextField.getText());
            
            Long dateTime = DateTools.stringToTime(DateTools.dateToString(editDateModel.getValue(), DateTools.Resolution.MILLISECOND));
            
            this.post.setPostField(LuceneConstants.CONSTANT.get(4), String.valueOf(dateTime));
            
            for(int i=0;i < 9;i++){
                this.post.setPostField(LuceneConstants.CONSTANT.get(i+5), this.editFiltersInfoListText.get(i).getText());
            }
            trashFrame.indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);
            trashFrame.indexer.updateDoc(post);
            trashFrame.indexer.close();
            
            showMode();
        } catch (ParseException | IOException ex) {
            Logger.getLogger(PostDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class FiltersFocusListener implements FocusListener
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
                    JOptionPane.showMessageDialog(PostDialog.this, "     ~Invalid Input~\n!!!Value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                ((JTextField)e.getComponent()).setName("N");
                ((JTextField)e.getComponent()).setBackground(Color.decode("#FFCECE"));
                JOptionPane.showMessageDialog(PostDialog.this, "        ~No Input~\n!!!Value is not a Number!!!", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter{

        private String datePattern = "MM/dd/yy hh:mm:ss";
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
    
    private class ClickActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text = e.getActionCommand();
            
            if(text.equals("More Like This")){
                try {
                    trashFrame.searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
                    trashFrame.myListModel.removeAllElements();
                    trashFrame.postsResults.clear();
                    String str = post.getPostField(LuceneConstants.CONSTANT.get(0))
                            + " AND" + LuceneConstants.CONSTANT.get(1) + ": \""+ post.getPostField(LuceneConstants.CONSTANT.get(1)) + "\"";
                    trashFrame.search(str);
                    trashFrame.searcher.close();
                    dispose();
                } catch (IOException ex) {
                    Logger.getLogger(PostDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(text.equals("Edit")){
                editMode();
            }
            else if(text.equals("Save")){
                try {
                    for(int i=0;i<editFiltersInfoListText.size();i++){
                        if(editFiltersInfoListText.get(i).getName().compareToIgnoreCase("N")==0)
                            throw new Exception("Invalid Input in Filters");
                    }
                    saveMode();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(PostDialog.this, ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(text.equals("Discard")){
                showMode();
            }
        }
    }
}
