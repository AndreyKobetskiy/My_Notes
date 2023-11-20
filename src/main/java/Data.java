import com.alibaba.fastjson.JSON;
import javax.swing.JRadioButton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
    private final HashMap<Note, JRadioButton> buttonHashMap = new HashMap<>();

    private final List<Note> notes = new ArrayList<>();

    public Data() {

        try {
            File file = new File("save.json");
            if (file.isFile()){
                String content = new String(Files.readAllBytes(file.toPath()));
                notes.addAll(JSON.parseArray(content, Note.class));
            }
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
        }
        catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
        }
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void add(Note note){
        notes.add(note);
    }

    public void addButton(Note note, JRadioButton bt){
        buttonHashMap.put(note, bt);
    }

    public JRadioButton getButton(Note note){
        return buttonHashMap.get(note);
    }
    public void remove(Note note){
        buttonHashMap.remove(note);
        notes.remove(note);
    }
}
