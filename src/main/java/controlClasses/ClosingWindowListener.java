package controlClasses;

import gui.GUI;
import storageClasses.Data;
import storageClasses.Note;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClosingWindowListener extends WindowAdapter {
    private final GUI gui;
    private final Data data;
    public ClosingWindowListener(GUI gui, Data data){
        this.data = data;
        this.gui = gui;
    };
    @Override
    public void windowClosing(WindowEvent e) {
        ButtonLogger.log.info("App closing");
        Note lastNote = gui.getLastNote();
        if (lastNote != null) {
            lastNote.setContent(gui.getTextAreaContent());
            lastNote.setName(gui.getNameFieldContent());
        }
        data.save();
        System.exit(0);
    }
}
