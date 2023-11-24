package controlClasses;

import gui.GUI;
import storageClasses.Data;
import storageClasses.Note;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenNoteAction implements ActionListener {
    private final GUI gui;
    private final Data data;
    private final Note connectedNote;
    public OpenNoteAction(GUI gui, Data data, Note connectedNote){
        this.gui = gui;
        this.data = data;
        this.connectedNote = connectedNote;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonLogger.log.info("Note Button pressed");

        Note lastNote = gui.getLastNote();
        if (lastNote != null){
            lastNote.setContent(gui.getTextAreaContent());
            lastNote.setName(gui.getNameFieldContent());
            data.getButton(lastNote).setText(lastNote.getName());
        }
        gui.displayNote(connectedNote);
        gui.setLastNote(connectedNote);
        gui.redraw();

        ButtonLogger.log.info("Note Button processed successfully");
    }

}
