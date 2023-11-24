package controlClasses;

import gui.GUI;
import storageClasses.Data;
import storageClasses.Note;
import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//contains logic for "Delete" button on control panel

public class DelButtonActionListener implements ActionListener {
    private final GUI gui;
    private final Data data;
    public DelButtonActionListener(GUI gui, Data data){
        this.gui = gui;
        this.data = data;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonLogger.log.info("Del Button pressed");
        Note lastNote = gui.getLastNote();
        if (lastNote != null){
            JRadioButton deletedButton = data.getButton(lastNote);
            gui.removeSidePanelButton(deletedButton);
            data.remove(lastNote);
            gui.clearNoteArea();
            gui.setLastNote(null);
        }
        gui.redraw();
        ButtonLogger.log.info("Del Button processed successfully");
    }
}
