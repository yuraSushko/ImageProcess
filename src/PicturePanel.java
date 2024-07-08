import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class PicturePanel extends JPanel implements ActionListener ,  MouseMotionListener {
    private BufferedImage image;
    private JComboBox<String> comboBox;
    private BufferedImage imageWithFilter;
    private  String currFilter;
    private double percentScrrenToFilter;
    private PixelPoint up;
    private PixelPoint down;
    public PicturePanel(BufferedImage image) {
        setPictureTodisplay(image);
        setComboBox();
        this.up=new PixelPoint(Constans.COMBOBOX_WIDTH,0);
        this.down=new PixelPoint(Constans.COMBOBOX_WIDTH,this.getHeight());
        percentScrrenToFilter=0.0;
        this.addMouseMotionListener(this);
        printSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(this.imageWithFilter, Constans.COMBOBOX_WIDTH, 0, this.getWidth() - Constans.COMBOBOX_WIDTH, this.getHeight(), this);
        g.setColor(Color.green);
        this.down.setY(this.getHeight());
        g.drawLine(up.getX(),up.getY(),down.getX(),down.getY());
    }


    public void printSize(){
        new Thread(()->{
            while (true) {
                System.out.println(this.getWidth() + " ," + this.getHeight() + "panel");

                System.out.println(this.image.getWidth() + " ," + this.image.getHeight() + "pictuer") ;
                System.out.println("#######################################################");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            }
        }).start();


    }



    public boolean withinBoundsPerPixel(int currX){
        boolean within = false;
        double widthPanel = this.getWidth();
        percentScrrenToFilter=  (this.down.getX()-Constans.COMBOBOX_WIDTH) / (widthPanel-Constans.COMBOBOX_WIDTH);
        int xAtPicture=(int) (percentScrrenToFilter*this.image.getWidth());
        if(currX>= xAtPicture){within=true;}
        return within;

    }


    public void setPictureTodisplay(BufferedImage image) {
        this.setBounds(0, 0, image.getWidth(), image.getHeight());
        this.image = image;
        this.imageWithFilter = copyImage(image);

    }


    public BufferedImage copyImage(BufferedImage original) {
        BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return copy;
    }

    public void actionPerformed(ActionEvent e) {
        currFilter = comboBox.getSelectedItem().toString();
        for (int i = 0; i < Constans.PICTURE_FILTER_OPTIONS.length; i++) {
            if (currFilter.equals(Constans.PICTURE_FILTER_OPTIONS[i])) {
                this.imageWithFilter = copyImage(this.image);
                repaint();
            }
        }
        switchCaseFilterAplly();
       }


       public void  switchCaseFilterAplly(){
           switch (currFilter) {
               case "Negative":
                   negative();
                   break;
               case "Gray Scale":
                   grayscale();
                   break;
               case "Black And White":
                   blackAndWhite();
                   break;
               case "Tint":
                   tint();
                   break;
               case "Color Shift Left":
                   colorShiftLeft();
                   break;
               case "Color Shift Right":
                   colorShiftRight();
                   break;
               case "Mirror":
                   mirror();
                   break;
            case "Highlight Borders":
                showBorders();
                break;
               case "Pixelate":
                   pixelate();
                   break;
               case "Sepia":
                   sepia();
                   break;
               case "AddNoise":
                   addNoise();
                   break;

               case "Vintage":
                   vintage();
                   break;
               case "Original":
                   break;
               default:
                   break;
           }

           repaint();

       }


    public void setComboBox() {
        this.comboBox = new JComboBox<>(Constans.PICTURE_FILTER_OPTIONS);
        this.setLayout(null);
        this.comboBox.setBounds(0, 0, Constans.COMBOBOX_WIDTH, Constans.COMBOBOX_HIGHT);
        this.comboBox.addActionListener(this);
        this.add(comboBox);
    }


    public BufferedImage negative() {
        for (int x = 0; x < imageWithFilter.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < imageWithFilter.getHeight(); y++) {
                        Color currentColor = new Color(imageWithFilter.getRGB(x, y));
                        int red = 255 - currentColor.getRed();
                        int green = 255 - currentColor.getGreen();
                        int blue = 255 - currentColor.getBlue();
                        Color newColor = new Color(red, green, blue);
                        imageWithFilter.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }

    public BufferedImage grayscale() {

        for (int x = 0; x < image.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                        Color color = new Color(image.getRGB(x, y));
                        int red = color.getRed();
                        int green = color.getGreen();
                        int blue = color.getBlue();
                        int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                        Color grayColor = new Color(gray, gray, gray, color.getAlpha());
                        imageWithFilter.setRGB(x, y, grayColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }

    public BufferedImage blackAndWhite() {
        int mid = 128;
        for (int x = 0; x < image.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                        Color color = new Color(image.getRGB(x, y));
                        int red = color.getRed();
                        int green = color.getGreen();
                        int blue = color.getBlue();
                        int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                        int bw = (gray >= mid) ? 255 : 0;
                        Color bwColor = new Color(bw, bw, bw, color.getAlpha());
                        imageWithFilter.setRGB(x, y, bwColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }

    public BufferedImage tint() {
        Color tintColor = Color.CYAN;
        for (int x = 0; x < image.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color color = new Color(image.getRGB(x, y), true);
                    int red = (color.getRed() + tintColor.getRed()) / 2;
                    int green = (color.getGreen() + tintColor.getGreen()) / 2;
                    int blue = (color.getBlue() + tintColor.getBlue()) / 2;
                    int alpha = color.getAlpha();
                    Color newColor = new Color(red, green, blue, alpha);
                    imageWithFilter.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }


    public BufferedImage colorShiftRight() {
        for (int x = 0; x < image.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color color = new Color(imageWithFilter.getRGB(x, y), true);
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    int alpha = color.getAlpha();
                    // Shift colors: red -> green, green -> blue, blue -> red
                    Color newColor = new Color(blue, red, green, alpha);
                    imageWithFilter.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }

    public BufferedImage colorShiftLeft() {
        for (int x = 0; x < image.getWidth(); x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color color = new Color(imageWithFilter.getRGB(x, y), true);
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    int alpha = color.getAlpha();
                    // Shift colors: blue -> green, green -> red, red -> blue
                    Color newColor = new Color(green, blue, red, alpha);
                    imageWithFilter.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        return imageWithFilter;
    }


    public BufferedImage mirror() {
        for (int x = 0; x < image.getWidth() / 2; x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color colorleft = new Color(imageWithFilter.getRGB(x, y));
                    Color colorRight = new Color(imageWithFilter.getRGB(image.getWidth() - x - 1, y));
                    imageWithFilter.setRGB(image.getWidth() - x - 1, y, colorleft.getRGB());
                    imageWithFilter.setRGB(x, y, colorRight.getRGB());
                    }
            }
        }
        return imageWithFilter;
    }


    public BufferedImage showBorders() {
        int maxDif=30;
        for (int x = 0; x < image.getWidth() -1; x++) {
            if(withinBoundsPerPixel(x)) {
                for (int y = 0; y < image.getHeight() -1; y++) {
                    boolean border = false;
                    Color curr = new Color(imageWithFilter.getRGB(x, y));
                    Color right = new Color(imageWithFilter.getRGB(x + 1, y));
                    int redDif= Math.abs(curr.getRed()-right.getRed());
                    int blueDif= Math.abs(curr.getBlue()-right.getBlue());
                    int greenDif= Math.abs(curr.getGreen()-right.getGreen());
                    int totalDif=redDif+blueDif+greenDif;
                    if(totalDif>maxDif){
                        border=true;
                    }
                    else{
                        curr = new Color(imageWithFilter.getRGB(x, y));
                        Color down = new Color(imageWithFilter.getRGB(x , y+1));
                        redDif= Math.abs(curr.getRed()-down.getRed());
                        blueDif= Math.abs(curr.getBlue()-down.getBlue());
                        greenDif= Math.abs(curr.getGreen()-down.getGreen());
                        totalDif=redDif+blueDif+greenDif;
                    }
                    if(totalDif>maxDif){
                        border=true;
                    }
                    if(border){
                        imageWithFilter.setRGB(x,y,Color.BLACK.getRGB());
                    }
                    else{
                        imageWithFilter.setRGB(x,y,Color.WHITE.getRGB());
                    }
                }
            }
        }
        return imageWithFilter;
    }



    public BufferedImage pixelate() {
        int blockSize=10;
        for (int y = 0; y < image.getHeight(); y += blockSize) {
            for (int x = 0; x < image.getWidth(); x += blockSize) {
                if(withinBoundsPerPixel(x)) {
                    int redSum = 0, greenSum = 0, blueSum = 0, alphaSum = 0;

                    // Calculate the average color of the block
                    for (int pixley = 0; pixley < blockSize; pixley++) {
                        for (int pixlex = 0; pixlex < blockSize; pixlex++) {
                            int px = x + pixlex;
                            int py = y + pixley;
                            if (px < imageWithFilter.getWidth() && py < imageWithFilter.getHeight()) {
                                Color color = new Color(imageWithFilter.getRGB(px, py), true);
                                redSum += color.getRed();
                                greenSum += color.getGreen();
                                blueSum += color.getBlue();
                                alphaSum += color.getAlpha();
                            }
                        }
                    }

                    int redAvg = redSum / (blockSize*blockSize);
                    int greenAvg = greenSum / (blockSize*blockSize);
                    int blueAvg = blueSum / (blockSize*blockSize);
                    int alphaAvg = alphaSum / (blockSize*blockSize);
                    Color avgColor = new Color(redAvg, greenAvg, blueAvg, alphaAvg);

                    // Apply the average color to the block
                    for (int by = 0; by < blockSize; by++) {
                        for (int bx = 0; bx < blockSize; bx++) {
                            int px = x + bx;
                            int py = y + by;
                            if (px < imageWithFilter.getWidth() && py < imageWithFilter.getHeight()) {
                                imageWithFilter.setRGB(px, py, avgColor.getRGB());
                            }
                        }
                    }
                }
            }
        }
        return imageWithFilter;
    }



    public  BufferedImage sepia() {


        // Traverse each pixel in the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (withinBoundsPerPixel(x)) {
                    // Get current pixel's RGB values
                    Color pixel = new Color(imageWithFilter.getRGB(x, y));
                    int red = pixel.getRed();
                    int green = pixel.getGreen();
                    int blue = pixel.getBlue();

                    // Apply sepia formula
                    int newRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
                    int newGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
                    int newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

                    // Adjust values if they exceed 255
                    if (newRed > 255) newRed = 255;
                    if (newGreen > 255) newGreen = 255;
                    if (newBlue > 255) newBlue = 255;

                    // Set the new RGB value
                    Color newColor = new Color(newRed, newGreen, newBlue);
                    imageWithFilter.setRGB(x, y, newColor.getRGB());
                }
            }
        }

        return imageWithFilter;
    }
    public  BufferedImage addNoise() {

        Random random = new Random();

        // Traverse each pixel in the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (withinBoundsPerPixel(x)) {
                    // Get current pixel's RGB values
                    Color color = new Color(imageWithFilter.getRGB(x, y));
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    // Generate Gaussian noise for each channel
                    int newRed = addGaussian(red, 50, random);
                    int newGreen = addGaussian(green, 50, random);
                    int newBlue = addGaussian(blue, 50, random);

                    // Ensure the values are within the valid range (0-255)
                    newRed = Math.min(Math.max(newRed, 0), 255);
                    newGreen = Math.min(Math.max(newGreen, 0), 255);
                    newBlue = Math.min(Math.max(newBlue, 0), 255);

                    // Set the new RGB value in the noisy image
                    Color noisyColor = new Color(newRed, newGreen, newBlue);
                    imageWithFilter.setRGB(x, y, noisyColor.getRGB());
                }
            }
        }

        return imageWithFilter;
    }

    public int addGaussian(int value, double variance, Random random) {
        double noise = random.nextGaussian() * variance;
        return (int) Math.round(value + noise);
    }

    public BufferedImage vintage(){
        this.imageWithFilter = addNoise();
        BufferedImage imageWithNoiseAndSepia=sepia();
        return imageWithNoiseAndSepia;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        this.up.setX(x);
        this.down.setX(x);
        if(this.currFilter!=null){
            this.imageWithFilter = copyImage(this.image);
            repaint();
            switchCaseFilterAplly();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
