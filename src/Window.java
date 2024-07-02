import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.awt.image.BufferedImage;
public class Window extends JFrame {
    private PicturePanel displayImage;
    public Window(){
        fileChooser();
   }

    private void fileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                BufferedImage image = ImageIO.read(file);
                 displayImage = new PicturePanel(image);

                this.add(displayImage);
                this.setSize(displayImage.getWidth(), displayImage.getHeight());
                this.setLocationRelativeTo(null);
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                this.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
