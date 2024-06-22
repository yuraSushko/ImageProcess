import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
public class Window extends JFrame {
    private PicturePanel picturePanel;
    private ComboBoxPanel comboBoxPanel;
    public Window(){
        fileChooser();
//        this.setSize(800, 600);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        picturePanel = new PicturePanel();
//        picturePanel.setVisible(true);
//        this.add(picturePanel);
//        comboBoxPanel = new ComboBoxPanel();
//        this.add(comboBoxPanel);
//
//
//        this.setVisible(true);






    }
    private void fileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        try {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                BufferedImage image = ImageIO.read(file);
                PicturePanel displayImage = new PicturePanel(image);

                this.add(displayImage);
                this.setSize(600, 400);
                this.setLocationRelativeTo(null);
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                this.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
