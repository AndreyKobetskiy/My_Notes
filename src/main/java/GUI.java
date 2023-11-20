import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
    private static final Logger GUI_ASSEMBLY_LOG = LogManager.getLogger("Gui logger");
    private static final Logger BUTTON_LOGGER = LogManager.getLogger("Button logger");
    private final DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final JFrame mainFrame = new MainFrame();
    private final Data data;
    private final JTextArea textArea = new JTextArea();
    private Note lastNote = null;
    private final ButtonGroup bg = new ButtonGroup();
    private final JPanel sidePAnelCenter = new JPanel();
    private final JTextField nameField = new JTextField();
    private final JTextField creationDate = new JTextField();
    private final JTextField lastSeenDate = new JTextField();

    //init main frame
    public GUI(Data data) {
        this.data = data;
        mainFrame.addWindowListener(getWindowAdapter(data));
        mainFrame.getContentPane().add(BorderLayout.CENTER, assembleCenterPanel());
        mainFrame.getContentPane().add(BorderLayout.WEST, assembleSidePanel());
        mainFrame.setVisible(true);
        GUI_ASSEMBLY_LOG.info("Gui creation ended successfully");
    }

    //declares on window closing action
    //of saving data & exiting program
    private WindowAdapter getWindowAdapter(Data data) {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                GUI_ASSEMBLY_LOG.info("Window closing");
                if (lastNote != null) {
                    lastNote.setContent(textArea.getText());
                    lastNote.setName(nameField.getText());
                }
                data.save();
                System.exit(0);
            }
        };
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
        GUI_ASSEMBLY_LOG.info("Center panel assembled successfully");
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
    private JPanel assembleSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(BorderLayout.NORTH, new JLabel("Notes"));
        sidePanel.add(BorderLayout.CENTER, assembleSideScroll());
        sidePanel.add(BorderLayout.SOUTH, assembleControlPanel());
        GUI_ASSEMBLY_LOG.info("side panel assembled successfully");
        return sidePanel;
    }

    //init panel with control buttons
    //New - for creation & Del - for removal
    private JPanel assembleControlPanel() {
        JButton aNewBt = assembleANewButton();
        JButton delBt = assembleDelButton();
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.add(aNewBt);
        controlPanel.add(delBt);
        return controlPanel;
    }

    //init Del button for removal of a note
    //contains Del logic:
    //calls for removal to Data & Gui components
    //frame revalidation & repainting
    private JButton assembleDelButton() {
        JButton delBt = new JButton("Delete");
        delBt.addActionListener(e -> {
            BUTTON_LOGGER.info("Del Button pressed");
            if (lastNote != null){
                JRadioButton deletedButton = data.getButton(lastNote);
                bg.remove(deletedButton);
                sidePAnelCenter.remove(deletedButton);
                data.remove(lastNote);
                textArea.setText("");
                nameField.setText("");
                lastNote = null;
            }
            mainFrame.revalidate();
            mainFrame.repaint();
            BUTTON_LOGGER.info("Del Button processed successfully");
        });
        return delBt;
    }

    //init New button for creation of a note
    //contains New logic:
    //  calls for addition to Data & Gui components
    //  frame revalidation & repainting
    private JButton assembleANewButton() {
        JButton aNew = new JButton("New");
        aNew.addActionListener(e -> {
            BUTTON_LOGGER.info("New Button pressed");
            Note addedNote = new Note();
            addedNote.setCreation(LocalDateTime.now());
            addedNote.setLastSeen(LocalDateTime.now());
            data.add(addedNote);
            addNewNoteButton(addedNote);
            mainFrame.revalidate();
            mainFrame.repaint();
            BUTTON_LOGGER.info("New Button processed successfully");
        });
        return aNew;
    }

    //init buttons for all active notes & connecting them into button group
    private JScrollPane assembleSideScroll() {
        sidePAnelCenter.setLayout(new GridLayout(0, 1));
        for (Note note: data.getNotes()) {
            addNewNoteButton(note);
        }
        return new JScrollPane(sidePAnelCenter);
    }

    //init a button for an active note
    //contains logic for it
    //  saving of previous redacted note
    //  displaying of chosen note & setting of its lastSeen Date
    //  frame revalidation & repainting
    private void addNewNoteButton(Note note) {
        JRadioButton jrb = new JRadioButton(note.getName());
        data.addButton(note, jrb);
        bg.add(jrb);
        sidePAnelCenter.add(jrb);
        jrb.addActionListener(e -> {
            BUTTON_LOGGER.info("Note Button pressed");

            //saving of previous redacted note
            if (lastNote != null){
                lastNote.setContent(textArea.getText());
                lastNote.setName(nameField.getText());
                data.getButton(lastNote).setText(lastNote.getName());
                mainFrame.revalidate();
                mainFrame.repaint();
            }

            //setting new lastSeen Date
            note.setLastSeen(LocalDateTime.now());

            //  displaying of chosen note
            creationDate.setText("Created: " + note.getCreation().format(myFormatObj));
            lastSeenDate.setText("Last seen: " + note.getLastSeen().format(myFormatObj));
            nameField.setText(note.getName());
            textArea.setText(note.getContent());
            lastNote = note;

            mainFrame.revalidate();
            mainFrame.repaint();
            BUTTON_LOGGER.info("Note Button processed successfully");
        });
        GUI_ASSEMBLY_LOG.info( "to side panel added " + jrb);
    }
}
