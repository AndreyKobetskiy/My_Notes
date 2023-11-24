package controlClasses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import gui.GUI;
import storageClasses.Data;
import storageClasses.Note;

//contains logic for "NEW" button on control panel

public class ANewButtonActionListener implements ActionListener{
    private final GUI gui;
    private final Data data;
    public ANewButtonActionListener(GUI gui, Data data){
        this.gui = gui;
        this.data = data;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonLogger.log.info("New Button pressed");
        Note addedNote = new Note();
        addedNote.setCreation(LocalDateTime.now());
        addedNote.setLastSeen(LocalDateTime.now());
        data.add(addedNote);
        data.addButton(addedNote, gui.createNewNoteButton(addedNote, data));
        gui.redraw();
        ButtonLogger.log.info("New Button processed successfully");
    }
}
