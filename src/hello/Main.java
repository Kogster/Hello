package hello;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Main {
	private static int numberOfFaces = 0;
	private static int cameraIndex = -1;

	static {
		System.load(getRsrcsFolder() + System.mapLibraryName(Core.NATIVE_LIBRARY_NAME));
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Hello");
		JPanel panel = new JPanel();
		JLabel number = new JLabel("XXXX");
		panel.add(number);
		panel.add(new JLabel("               :               "));
		int countInt = 0;
		JLabel count = new JLabel("XXXX");
		panel.add(count);
		JFrame frame = new JFrame("Ansikten i senaste : Antal frames");
		frame.add(panel);
		frame.setSize(400, 100);
		frame.setVisible(true);

		Gui g = new Gui();

		boolean detected = false;

		while (frame.isVisible()) {
			Mat m = null;
			m = takeAPicture();
			findFace(m);
			number.setText("" + numberOfFaces);
			if (numberOfFaces > 0 && !detected) {
				detected = true;
				g.setState(Gui.Tillstand.HELLO);
				if (new Random().nextInt() % 3 == 0 && countInt > 3) { // easter
																		// egg-ish
					playSound("Stolpskott");
					g.setState(Gui.Tillstand.STOLPSKOTT);
				} else {
					playSound("Hello");
				}
			} else if (numberOfFaces > 0 && detected) {

			} else {
				detected = false;
				g.setState(Gui.Tillstand.INGET);
			}
			count.setText("" + countInt++);
		}
		System.out.println("Done");
		System.exit(0);
	}

	public static void playSound(String which) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(getRsrcsFolder() + which + ".wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			System.out.println("Oj då audiofile");
		} catch (LineUnavailableException e) {
			System.err.println("Oj då LineUnavailable");
		} catch (IOException e) {
			System.err.println("Oj då IOException");
		}
	}

	public static Mat takeAPicture() {
		// If a camera is disconnected and reconnected it usually gets a
		// higher index and camera[0] will be null.
		VideoCapture camera = new VideoCapture(cameraIndex);
		if (cameraIndex == -1) {
			do {
				cameraIndex++;
				camera.open(cameraIndex);
			} while (!camera.isOpened() && cameraIndex < 10);
		} else {
			camera.open(cameraIndex);
		}
		Mat frame = null;
		frame = new Mat();
		camera.read(frame);
		camera.release();
		return frame;
	}

	public static void findFace(Mat face) {
		CascadeClassifier faceDetector = new CascadeClassifier(
				new File(getRsrcsFolder() + "lbpcascade_frontalface.xml").getAbsolutePath());
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(face, faceDetections);
		numberOfFaces = faceDetections.toArray().length;
	}

	public static String getRsrcsFolder() {
		String result = "";
		if (isJar()) { 
			result = new File(getExecPath().getParentFile().getAbsolutePath() + File.separator + "Resources")
					.getAbsolutePath() + File.separator;
		} else {// Not executing as jar, tries path that
			// would be if executed from eclipse
			result = getExecPath().getParentFile().getAbsolutePath() + File.separator + "lib" + File.separator;
		}
		return result;
	}

	public static boolean isJar() {
		return getExecPath().getAbsolutePath().endsWith(".jar");
	}

	public static File getExecPath() {
		try {
			return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			return null; // Not gonna happen.
		}
	}

}
