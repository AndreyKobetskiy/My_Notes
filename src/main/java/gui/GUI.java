package gui;

import controlClasses.ANewButtonActionListener;
import controlClasses.ClosingWindowListener;
import controlClasses.DelButtonActionLIstner;
import controlClasses.OpenNoteAction;
import storageClasses.Data;
import storageClasses.Note;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
    private static final Logger guiAsemblyLogger = LogManager.getLogger("GUI.GUI assembly logger ");
    private final DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final JFrame mainFrame = new MainFrame();
    private final JTextArea textArea = new JTextArea();
    private Note lastNote = null;
    private final ButtonGroup bg = new ButtonGroup();
    private final JPanel sidePAnelCenter = new JPanel();
    private final JTextField nameField = new JTextField();
    private final JTextField creationDate = new JTextField();
    private final JTextField lastSeenDate = new JTextField();

    //init main frame
    public GUI(Data data) {
        mainFrame.addWindowListener(new ClosingWindowListener(this, data));
        mainFrame.getContentPane().add(BorderLayout.CENTER, assembleCenterPanel());
        mainFrame.getContentPane().add(BorderLayout.WEST, assembleSidePanel(data));
        mainFrame.setVisible(true);
        guiAsemblyLogger.info("Gui creation ended successfully");
    }

    //init panel that contains:
    //name field          - on the top
    //main note text area - in the center
    //date information    - at the bottom
    private JPanel assembleCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(BorderLayout.NORTH, assembleNamePanel());
        centerPanel.add(BorderLayout.CENTER, assembleScrollPane());
        centerPanel.add(BorderLayout.SOUTH, assembleDatePanel());
        guiAsemblyLogger.info("Center panel assembled successfully");
        return centerPanel;
    }

    //init panel that contains data information
    private JPanel assembleNamePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(new JLabel("Note name:"));
        nameField.setPreferredSize(new Dimension(200, 20));
        namePanel.add(nameField);
        return namePanel;
    }

    //init panel with main note text area
    private JScrollPane assembleScrollPane() {
        textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
        areaScrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return areaScrollPane;
    }

    //init panel with date information
    private JPanel assembleDatePanel() {
        JPanel DatePanel = new JPanel();
        DatePanel.setLayout(new FlowLayout());
        creationDate.setEditable(false);
        lastSeenDate.setEnabled(false);
        DatePanel.add(creationDate);
        DatePanel.add(lastSeenDate);
        return DatePanel;
    }



    //init panel that contains:
    //label                        - on the top
    //buttons for all active notes - in the center
    //control buttons              - at the bottom
    private JPanel assembleSidePanel(Data data) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(BorderLayout.NORTH, new JLabel("Notes"));
        sidePanel.add(BorderLayout.CENTER, assembleSideScroll(data));
        sidePanel.add(BorderLayout.SOUTH, assembleControlPanel(data));
        guiAsemblyLogger.info("Side panel assembled successfully");
        return sidePanel;
    }

    //init panel with control buttons
    //New - for creation & Del - for removal
    private JPanel assembleControlPanel(Data data) {
        JButton aNewBt = assembleANewButton(data);
        JButton delBt = assembleDelButton(data);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.add(aNewBt);
        controlPanel.add(delBt);
        return controlPanel;
    }

    public Note getLastNote() {
        return lastNote;
    }

    //init Del button for removal of a note
    //contains Del logic:
    //calls for removal to StorageClasses.Data & Gui components
    //frame revalidation & repainting
    private JButton assembleDelButton(Data data) {
        JButton delBt = new JButton("Delete");
        delBt.addActionListener(new DelButtonActionLIstner(this, data));
        return delBt;
    }

    //init New button for creation of a note
    //contains New logic:
    //  calls for addition to StorageClasses.Data & Gui components
    //  frame revalidation & repainting
    private JButton assembleANewButton(Data data) {
        JButton aNew = new JButton("New");
        aNew.addActionListener(new ANewButtonActionListener(this, data));
        return aNew;
    }

    //init buttons for all active notes & connecting them into button group
    private JScrollPane assembleSideScroll(Data data) {
        sidePAnelCenter.setLayout(new GridLayout(0, 1));
        for (Note note: data.getNotes()) {
            data.addButton(note, createNewNoteButton(note, data));
        }
        return new JScrollPane(sidePAnelCenter);
    }

    public void setLastNote(Note lastNote) {
        this.lastNote = lastNote;
    }

    //init a button for an active note
    //contains logic for it
    //  saving of previous redacted note
    //  displaying of chosen note & setting of its lastSeen Date
    //  frame revalidation & repainting
    public JRadioButton createNewNoteButton(Note note, Data data) {
        JRadioButton jrb = new JRadioButton(note.getName());
        addSidePanelButton(jrb);
        jrb.addActionListener(new OpenNoteAction(this, data, note));
        guiAsemblyLogger.debug( "to side panel added " + jrb);
        return jrb;
    }
    public void redraw(){
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void removeSidePanelButton(JRadioButton button){
        bg.remove(button);
        sidePAnelCenter.remove(button);
    }

    public void addSidePanelButton(JRadioButton button){
        bg.add(button);
        sidePAnelCenter.add(button);
    }

    public void clearNoteArea(){
        textArea.setText("");
        nameField.setText("");
        creationDate.setText("");
        lastSeenDate.setText("");
    }
    public void displayNote(Note note){
        creationDate.setText("Created: " + note.getCreation().format(myFormatObj));
        lastSeenDate.setText("Last seen: " + note.getLastSeen().format(myFormatObj));
        nameField.setText(note.getName());
        textArea.setText(note.getContent());
        //update last seen date after displaying
        //in order to display previous last seen date
        note.setLastSeen(LocalDateTime.now());
    }
    public String getTextAreaContent(){
        return textArea.getText();
    }
    public String getNameFieldContent(){
        return nameField.getText();
    }

}
