import com.alibaba.fastjson.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
    public HashMap<Note, JRadioButton> buttonHashMap = new HashMap<>();
    public List<Note> notes = new ArrayList<>();
    public List<Note> addedNotes = new ArrayList<>();
    public void read(){
        File file = new File("save.json");
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            notes = JSON.parseArray(content, Note.class);
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void save(){
        File file = new File("save.json");
        try {
            file.createNewFile();
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(JSON.toJSONString(notes));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
        }
    }
    public void add(Note note){
        notes.add(note);
    }
}
