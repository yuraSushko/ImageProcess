import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PicturePanel extends JPanel implements ActionListener , MouseListener {
    private BufferedImage image; // change to orgImage; add private BufferedImage image
    private JComboBox<String> comboBox;
    private BufferedImage imageWithFilter;
    private JButton resetBox;
    private List<PixelPoint> locationsCliked;
    private int minBorderX;
    private int minBorderY;
    private int maxBorderX;
    private int maxBorderY;


    public PicturePanel(BufferedImage image) {
        setPictureTodisplay(image);
        setComboBoxg();
        locationsCliked = new ArrayList<>();

        this.minBorderX=0;
        this.minBorderY=0;
        this.maxBorderX=this.image.getWidth();
        this.maxBorderY=this.image.getHeight();
        createResetBorderButton();
        this.addMouseListener(this);
        printSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(this.imageWithFilter, 100, 0, this.getWidth() - 100, this.getHeight(), this);

        g.setColor(Color.ORANGE);
        for (PixelPoint pixel :locationsCliked ){
            g.fillOval(pixel.getX()+100,pixel.getY(), 10,10);
        }


    }

    private void printSize(){
       new Thread(()->{

           while (true){
               System.out.println("image " +this.image.getHeight() + ", "+ this.image.getWidth());

               System.out.println("filter " +this.imageWithFilter.getHeight() + ", "+ this.imageWithFilter.getWidth());
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }

       } ).start();
    }

    public void addLocationsCliked(int xPixel,int yPixel){
        if (this.locationsCliked.size()<Constans.NUMBER_OF_BORDERS_TO_CLICK){

            this.locationsCliked.add(new PixelPoint(xPixel-100,yPixel));

        }
    }

    public void resetLocationsCliked(){
        this.locationsCliked.clear();
    }
    public void createResetBorderButton(){
        this.resetBox = new JButton("Reset Filter Border");
        this.resetBox.setBounds(0,0  , 100,50);
        this.resetBox.setVisible(true);
        this.resetBox.addActionListener(this);
        this.add(resetBox);

    }

    public void setMaxMinBorderBoundsForFilter(){
        if (this.locationsCliked.size()==Constans.NUMBER_OF_BORDERS_TO_CLICK){
            int[] arrPixelPartX= new int[Constans.NUMBER_OF_BORDERS_TO_CLICK];
            int[] arrPixelPartY= new int[Constans.NUMBER_OF_BORDERS_TO_CLICK];
            // better to do at final check in filter aply but is expensive
            double panelWidth= this.getWidth(), panelHight=this.getHeight() ;
            for (int i = 0; i < Constans.NUMBER_OF_BORDERS_TO_CLICK; i++) {
                double xPixel =locationsCliked.get(i).getX();
                double yPixel =locationsCliked.get(i).getY();
                double percentX =( (xPixel) / (panelWidth-100)  );//-100
                double percentY = yPixel / panelHight;//-100
                int xAtPicture= (int) (percentX*this.image.getWidth());
                int yAtPicture= (int) (percentY*this.image.getHeight());
                arrPixelPartY[i]=yAtPicture;
                arrPixelPartX[i]=xAtPicture;
                //arrPixelPartY[i]=locationsCliked.get(i).getY();
               // arrPixelPartX[i]=locationsCliked.get(i).getX();
            }
            this.minBorderX=findMin(arrPixelPartX);
            System.out.println(minBorderX + " minBorderX");
            this.minBorderY=findMin(arrPixelPartY);
            System.out.println(minBorderY + " minBorderY");
            this.maxBorderX=findMax(arrPixelPartX);
            System.out.println(maxBorderX + " maxBorderX");
            this.maxBorderY=findMax(arrPixelPartY);
            System.out.println(maxBorderY + " maxBorderY");

        }
    }

    public boolean withinBoundsPerPixel(int currX, int currY){
        boolean within = false;
            if(minBorderX<=currX && maxBorderX>=currX){
                if(minBorderY<=currY && maxBorderY>=currY){
                    within=true;
                }
            }
        return within;

    }

    private int findMax(int[] arrPixelPart){
//        int max = arrPixelPart[0];
//        for (int i = 0; i < arrPixelPart.length; i++) {
//            for (int j = i+1; j < arrPixelPart.length; j++) {
//                if(max<arrPixelPart[j]){max=arrPixelPart[j];}
//            }
//        }
        return  Arrays.stream(arrPixelPart).reduce(Integer::max).orElse(0);
    }


    private int findMin(int[] arrPixelPart){
//        int min = arrPixelPart[0];
//        for (int i = 0; i < arrPixelPart.length; i++) {
//            for (int j = i+1; j < arrPixelPart.length; j++) {
//                if(min>arrPixelPart[j]){min=arrPixelPart[j];}
//            }
//        }
        return  Arrays.stream(arrPixelPart).reduce(Integer::min).orElse(0);
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

        try {


            if( ((JButton) e.getSource() ).getText().equals("Reset Filter Border") ){
                resetLocationsCliked();
            }
        } catch (Exception ex) {

        }


        String currFilter = comboBox.getSelectedItem().toString();
        System.out.println(currFilter + " curr selected");

        for (int i = 0; i < Constans.PICTURE_FILTER_OPTIONS.length; i++) {
            if (currFilter.equals(Constans.PICTURE_FILTER_OPTIONS[i])) {
                this.imageWithFilter = copyImage(this.image);
                repaint();
                System.out.println("changed");
            }
        }
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
//            case "Highlight Borders":
//
//                break;
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


    public void setComboBoxg() {
        this.comboBox = new JComboBox<>(Constans.PICTURE_FILTER_OPTIONS);
        this.setLayout(null);
        this.comboBox.setBounds(0, 55, 100, 50);
        this.comboBox.addActionListener(this);
        this.add(comboBox);
    }


    public BufferedImage negative() {
        for (int x = 0; x < imageWithFilter.getWidth(); x++) {
            for (int y = 0; y < imageWithFilter.getHeight(); y++) {
                if(withinBoundsPerPixel(x,y)) {
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
        return imageWithFilter;
    }

    public BufferedImage blackAndWhite() {


        final int THRESHOLD = 128;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int bw = (gray >= THRESHOLD) ? 255 : 0;
                Color bwColor = new Color(bw, bw, bw, color.getAlpha());
                imageWithFilter.setRGB(x, y, bwColor.getRGB());
            }
        }
        return imageWithFilter;
    }

    public BufferedImage tint() {

        Color tintColor = Color.CYAN;

        for (int x = 0; x < image.getWidth(); x++) {
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
        return imageWithFilter;
    }


    public BufferedImage colorShiftRight() {


        for (int x = 0; x < image.getWidth(); x++) {
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
        return imageWithFilter;
    }

    public BufferedImage colorShiftLeft() {


        for (int x = 0; x < image.getWidth(); x++) {
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
        return imageWithFilter;
    }


    public BufferedImage mirror() {

        for (int x = 0; x < image.getWidth() / 2; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color colorleft = new Color(imageWithFilter.getRGB(x, y), true);
                Color colorRight = new Color(imageWithFilter.getRGB(image.getWidth() - x - 1, y), true);
                imageWithFilter.setRGB(image.getWidth() - x - 1, y, colorleft.getRGB());
                imageWithFilter.setRGB(x, y, colorRight.getRGB());
            }
        }
        return imageWithFilter;
    }


    public BufferedImage pixelate() {


        for (int y = 0; y < image.getHeight(); y += 5) {
            for (int x = 0; x < image.getWidth(); x += 5) {
                int redSum = 0, greenSum = 0, blueSum = 0, alphaSum = 0;
                int count = 0;

                // Calculate the average color of the block
                for (int by = 0; by < 5; by++) {
                    for (int bx = 0; bx < 5; bx++) {
                        int px = x + bx;
                        int py = y + by;
                        if (px < imageWithFilter.getWidth() && py < imageWithFilter.getHeight()) {
                            Color color = new Color(imageWithFilter.getRGB(px, py), true);
                            redSum += color.getRed();
                            greenSum += color.getGreen();
                            blueSum += color.getBlue();
                            alphaSum += color.getAlpha();
                            count++;
                        }
                    }
                }

                int redAvg = redSum / count;
                int greenAvg = greenSum / count;
                int blueAvg = blueSum / count;
                int alphaAvg = alphaSum / count;
                Color avgColor = new Color(redAvg, greenAvg, blueAvg, alphaAvg);

                // Apply the average color to the block
                for (int by = 0; by < 5; by++) {
                    for (int bx = 0; bx < 5; bx++) {
                        int px = x + bx;
                        int py = y + by;
                        if (px < imageWithFilter.getWidth() && py < imageWithFilter.getHeight()) {
                            imageWithFilter.setRGB(px, py, avgColor.getRGB());
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
                // Get current pixel's RGB values
                Color pixel = new Color(imageWithFilter.getRGB(x, y));
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                // Apply sepia formula
                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                // Adjust values if they exceed 255
                if (tr > 255) tr = 255;
                if (tg > 255) tg = 255;
                if (tb > 255) tb = 255;

                // Set the new RGB value
                Color newColor = new Color(tr, tg, tb);
                imageWithFilter.setRGB(x, y, newColor.getRGB());
            }
        }

        return imageWithFilter;
    }
    public  BufferedImage addNoise() {

        Random random = new Random();

        // Traverse each pixel in the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Get current pixel's RGB values
                Color color = new Color(imageWithFilter.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                // Generate Gaussian noise for each channel
                int newR = addGaussian(r, 50, random);
                int newG = addGaussian(g, 50, random);
                int newB = addGaussian(b, 50, random);

                // Ensure the values are within the valid range (0-255)
                newR = Math.min(Math.max(newR, 0), 255);
                newG = Math.min(Math.max(newG, 0), 255);
                newB = Math.min(Math.max(newB, 0), 255);

                // Set the new RGB value in the noisy image
                Color noisyColor = new Color(newR, newG, newB);
                imageWithFilter.setRGB(x, y, noisyColor.getRGB());
            }
        }

        return imageWithFilter;
    }

    public int addGaussian(int value, double variance, Random random) {
        double noise = random.nextGaussian() * variance;
        return (int) Math.round(value + noise);
    }

    public BufferedImage vintage(){
        this.imageWithFilter = sepia();
        BufferedImage imageWithNoiseAndSepia=addNoise();
        return imageWithNoiseAndSepia;
    }

    @Override
    public void mouseClicked(MouseEvent e) {


        int clickedX=e.getX() ,clickedY= e.getY();
        if(clickedX>=100) {
            addLocationsCliked(clickedX,clickedY );
        }
        System.out.println(this.locationsCliked);
        setMaxMinBorderBoundsForFilter();

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }





}
