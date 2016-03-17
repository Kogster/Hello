package hello;

import java.awt.FlowLayout;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Gui {
	private JLabel lbl;
	private ImageIcon helloicon, notHelloicon, stolpskottIcon;
	
	public enum Tillstand{HELLO, STOLPSKOTT, INGET};
	
	public Gui() throws IOException{
        helloicon = new ImageIcon(ImageIO.read(getStreamFor("Hello.png")));
        notHelloicon = new ImageIcon(ImageIO.read(getStreamFor("notHello.png")));
        stolpskottIcon = new ImageIcon(ImageIO.read(getStreamFor("stolpskott.png")));
        JFrame frame=new JFrame();
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setLayout(new FlowLayout());
        frame.setSize(640, 400);
//        frame.setBackground(new Color(0,0,0,0));
        lbl=new JLabel();
        lbl.setIcon(notHelloicon);
        frame.add(lbl);
        frame.setUndecorated(true);
        frame.setLocation(new Point(100,100));
        frame.setVisible(true);
	}
	
	private InputStream getStreamFor(String imgName){
		if(Main.isJar()){
			return Gui.class.getClassLoader().getResourceAsStream(imgName);
		} else {
			try {
				return new FileInputStream(new File(Main.getRsrcsFolder() + File.separator + imgName));
			} catch (FileNotFoundException e) {
				System.err.println("Opsie failed to find an image file");
			}
		}
		return null;
	}
	
	public void setState(Tillstand state){
		switch(state){
		case HELLO:
			lbl.setIcon(helloicon);
			break;
		case INGET:
			lbl.setIcon(notHelloicon);
			break;
		case STOLPSKOTT:
			lbl.setIcon(stolpskottIcon);
			break;
		}
	}
}
