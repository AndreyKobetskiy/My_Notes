import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI {
    final DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    JFrame mainFrame = new MainFrame();
    Data data;
    JTextArea textArea = new JTextArea();
    Note lastNote = null;
    ButtonGroup bg = new ButtonGroup();
    JPanel sidePAnelCenter = new JPanel();
    JTextField nameField = new JTextField();
    JTextField creationDate = new JTextField();
    JTextField lastSeenDate = new JTextField();

    public GUI(Data data) {
        this.data = data;
        mainFrame.addWindowListener(getWindowAdapter(data));
        mainFrame.getContentPane().add(BorderLayout.CENTER, assembleCenterPanel());
        mainFrame.getContentPane().add(BorderLayout.WEST, makeSidePanel());
        mainFrame.setVisible(true);
    }



    private JPanel assembleCenterPanel() {
        JPanel namePanel = getNamePanel();
        JPanel DatePanel = getDatePanel();
        JPanel centerPanel = getCenterPanel(namePanel, DatePanel);
        return centerPanel;
    }

    private WindowAdapter getWindowAdapter(Data data) {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println(lastNote);
                if (lastNote != null) {
                    lastNote.setContent(textArea.getText());
                    lastNote.setName(nameField.getText());
                }
                data.save();
                System.exit(0);
            }
        };
    }

    private JPanel getCenterPanel(JPanel namePanel, JPanel DatePanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(BorderLayout.NORTH, namePanel);
        centerPanel.add(BorderLayout.CENTER, makeScrollPane());
        centerPanel.add(BorderLayout.SOUTH, DatePanel);
        return centerPanel;
    }

    private JPanel getDatePanel() {
        JPanel DatePanel = new JPanel();
        DatePanel.setLayout(new FlowLayout());
        creationDate.setEditable(false);
        lastSeenDate.setEnabled(false);
        DatePanel.add(creationDate);
        DatePanel.add(lastSeenDate);
        return DatePanel;
    }

    private JPanel getNamePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(new JLabel("Note name:"));
        nameField.setPreferredSize(new Dimension(200, 20));
        namePanel.add(nameField);
        return namePanel;
    }


    private JPanel makeSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());


        sidePAnelCenter.setLayout(new GridLayout(0, 1));
        
        
        for (Note note: data.notes) {
            addNewNoteButton(note);
        }
        JScrollPane sideScroll = new JScrollPane(sidePAnelCenter);

        JButton aNew = new JButton("New");
        aNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Note addedNote = new Note();
                addedNote.setCreation(LocalDateTime.now());
                addedNote.setLastSeen(LocalDateTime.now());
                addNewNoteButton(addedNote);
                data.add(addedNote);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        JButton delBt = new JButton("Delete");
        delBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastNote != null){
                    JRadioButton deletedButton = data.buttonHashMap.get(lastNote);
                    bg.remove(deletedButton);
                    sidePAnelCenter.remove(deletedButton);
                    data.notes.remove(lastNote);
                    data.buttonHashMap.remove(lastNote);
                    textArea.setText("");
                    lastNote = null;
                }
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.add(aNew);
        controlPanel.add(delBt);

        JLabel sidePanelLabel = new JLabel("Notes");

        sidePanel.add(sidePanelLabel, BorderLayout.NORTH);
        sidePanel.add(sideScroll, BorderLayout.CENTER);
        sidePanel.add(controlPanel, BorderLayout.SOUTH);
        return sidePanel;
    }

    private void addNewNoteButton(Note note) {
        JRadioButton jrb = new JRadioButton(note.getName());
        data.buttonHashMap.put(note, jrb);
        bg.add(jrb);
        sidePAnelCenter.add(jrb);
        jrb.addActionListener(e -> {
            if (lastNote != null){
                lastNote.setContent(textArea.getText());
                lastNote.setName(nameField.getText());
                data.buttonHashMap.get(lastNote).setText(lastNote.getName());
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

    private JScrollPane makeScrollPane() {
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

}
