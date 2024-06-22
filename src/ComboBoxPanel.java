import javax.swing.*;
import java.awt.*;

public class ComboBoxPanel extends JPanel {
    private JComboBox<String> comboBox;
    public ComboBoxPanel(){
        String[] options = new String[10];
        for (int i = 0; i < 10; i++) {
            options[i] = "Option " + (i + 1);
        }

        comboBox = new JComboBox<>(options);
        this.setBounds(0, 0, 200,200);
        this.setBackground(Color.BLACK);

        this.setLayout(null);
        this.add(comboBox);
    }
}
