import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
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

    public GUI(Data data) {
        this.data = data;
        mainFrame.addWindowListener(getWindowAdapter(data));
        mainFrame.getContentPane().add(BorderLayout.CENTER, assembleCenterPanel());
        mainFrame.getContentPane().add(BorderLayout.WEST, assembleSidePanel());
        mainFrame.setVisible(true);
    }

    private WindowAdapter getWindowAdapter(Data data) {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //System.out.println(lastNote);
                if (lastNote != null) {
                    lastNote.setContent(textArea.getText());
                    lastNote.setName(nameField.getText());
                }
                data.save();
                System.exit(0);
            }
        };
    }

    private JPanel assembleCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(BorderLayout.NORTH, assembleNamePanel());
        centerPanel.add(BorderLayout.CENTER, assembleScrollPane());
        centerPanel.add(BorderLayout.SOUTH, assembleDatePanel());
        return centerPanel;
    }

    private JPanel assembleNamePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(new JLabel("Note name:"));
        nameField.setPreferredSize(new Dimension(200, 20));
        namePanel.add(nameField);
        return namePanel;
    }

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


    private JPanel assembleDatePanel() {
        JPanel DatePanel = new JPanel();
        DatePanel.setLayout(new FlowLayout());
        creationDate.setEditable(false);
        lastSeenDate.setEnabled(false);
        DatePanel.add(creationDate);
        DatePanel.add(lastSeenDate);
        return DatePanel;
    }




    private JPanel assembleSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(BorderLayout.NORTH, new JLabel("Notes"));
        sidePanel.add(BorderLayout.CENTER, assembleSideScroll());
        sidePanel.add(BorderLayout.SOUTH, assembleControlPanel());
        return sidePanel;
    }

    private JPanel assembleControlPanel() {
        JButton aNewBt = assembleANewButton();
        JButton delBt = assembleDelButton();
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.add(aNewBt);
        controlPanel.add(delBt);
        return controlPanel;
    }

    private JButton assembleDelButton() {
        JButton delBt = new JButton("Delete");
        delBt.addActionListener(e -> {
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
        });
        return delBt;
    }

    private JButton assembleANewButton() {
        JButton aNew = new JButton("New");
        aNew.addActionListener(e -> {
            Note addedNote = new Note();
            addedNote.setCreation(LocalDateTime.now());
            addedNote.setLastSeen(LocalDateTime.now());
            data.add(addedNote);
            addNewNoteButton(addedNote);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
        return aNew;
    }

    private JScrollPane assembleSideScroll() {
        sidePAnelCenter.setLayout(new GridLayout(0, 1));
        for (Note note: data.getNotes()) {
            addNewNoteButton(note);
        }
        return new JScrollPane(sidePAnelCenter);
    }

    private void addNewNoteButton(Note note) {
        JRadioButton jrb = new JRadioButton(note.getName());
        data.addButton(note, jrb);
        bg.add(jrb);
        sidePAnelCenter.add(jrb);
        jrb.addActionListener(e -> {
            if (lastNote != null){
                lastNote.setContent(textArea.getText());
                lastNote.setName(nameField.getText());
                data.getButton(lastNote).setText(lastNote.getName());
                mainFrame.revalidate();
                mainFrame.repaint();
            }
            note.setLastSeen(LocalDateTime.now());
            creationDate.setText("Created: " + note.getCreation().format(myFormatObj));
            lastSeenDate.setText("Last seen: " + note.getLastSeen().format(myFormatObj));
            nameField.setText(note.getName());
            textArea.setText(note.getContent());
            lastNote = note;
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }



}
