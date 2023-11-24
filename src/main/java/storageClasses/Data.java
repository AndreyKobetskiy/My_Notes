package storageClasses;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.JRadioButton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
    private static final Logger dataLogger = LogManager.getLogger("StorageClasses.Data logger ");
    private final HashMap<Note, JRadioButton> buttonHashMap = new HashMap<>();

    private final List<Note> notes = new ArrayList<>();

    //tries to reed notes from save.json
    public Data() {
        try {
            File file = new File("save.json");
            if (file.isFile()){
                String content = new String(Files.readAllBytes(file.toPath()));
                notes.addAll(JSON.parseArray(content, Note.class));
                dataLogger.info("Notes from save.json imported");
            }else dataLogger.warn("No save.json file found");
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            dataLogger.error("File reading error" + e);
        }
    }

    //saves notes to save.json & creates this file if necessary
    public void save(){
        File file = new File("save.json");
        try {
            if (file.createNewFile()){
                dataLogger.info("New save.json file created");
            }
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(JSON.toJSONString(notes));
            myWriter.close();
            dataLogger.info("data saved");
        }
        catch (IOException e) {
                dataLogger.error("File write error; No save made" + e);
        }
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void add(Note note){
        notes.add(note);
        dataLogger.debug(note + " added to data");
    }

    public void addButton(Note note, JRadioButton bt){
        buttonHashMap.put(note, bt);
        dataLogger.debug("to " + note + " added " + bt);
    }

    public JRadioButton getButton(Note note){
        return buttonHashMap.get(note);
    }
    public void remove(Note note){
        buttonHashMap.remove(note);
        notes.remove(note);
        dataLogger.debug(note + " removed");
    }
}
