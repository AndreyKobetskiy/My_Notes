import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        data.read();
        GUI gui = new GUI(data);

        //data.save();
        //gui.draw(data);

    }
}
