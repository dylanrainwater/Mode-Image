import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main {
	private static int width, height;
	private static BufferedImage input;
	private static BufferedImage output;
	private static File inputFile;
	private static File outputFile;
	private static boolean printAll = true;
	private static int iterations = 50;
	private static int iteration = 1;

	/*
	 * Takes input picture and makes it blurrier with
	 * each iteration. The more iterations, the more blur.
	 */
	
	public static void main(String[] args) {
		inputFile = new File("input.png");
		outputFile = new File("output" + iteration + ".png");

		while (iteration <= iterations) {
			try {
				// after the first iteration, we use the
				// previous iterations to computer blur
				if (iteration > 1) {
					if (printAll) {
						input = ImageIO.read(outputFile);
					} else {
						input = output;
					}
				} else {
					input = ImageIO.read(inputFile);
				}



				width = input.getWidth();
				height = input.getHeight();
				output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						output.setRGB(i, j, getModeRGB(i, j));
					}
				}

				if (printAll || iteration == iterations) {
					outputFile = new File("output" + iteration + ".png");
					ImageIO.write(output, "png", outputFile);
				}
				iteration++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Complete.");
	}

	/*
	 * Returns the computed average of all
	 * RGBs of all neighbors of pixel at (x, y).
	 */
	public static int getModeRGB(int x, int y) {
		// 8 or less surrounding neighbors
		List<Color> neighbors = new ArrayList<Color>();
		
		// these values will compute the average RGB values
		// from all neighbors surrounding pixel at (x, y).
		int avgR = 0;
		int avgG = 0; 
		int avgB = 0;
		int mode = 0;

		if (x > 0) {
			// Left
			neighbors.add(new Color(input.getRGB(x - 1, y)));
			
			if (y > 0) {
				// Left-Up
				neighbors.add(new Color(input.getRGB(x - 1, y - 1)));
			}
			
			if (y < (height - 1)) {
				// Left-Down
				neighbors.add(new Color(input.getRGB(x - 1, y + 1)));
			}
		}
		
		if (x < (width - 1)) {
			// Right
			neighbors.add(new Color(input.getRGB(x + 1, y)));
			
			if (y > 0) {
				// Right-Up
				neighbors.add(new Color(input.getRGB(x + 1, y - 1)));
			}
			
			if (y < (height - 1)) {
				// Right-Down
				neighbors.add(new Color(input.getRGB(x + 1, y + 1)));
			}
		}
		

		// Middle Up
		if (y > 0) {
			neighbors.add(new Color(input.getRGB(x, y - 1)));
		}
		
		// Middle Down
		if (y < (height - 1)) {
			neighbors.add(new Color(input.getRGB(x, y + 1)));
		}

		for (int i = 0; i < neighbors.size(); i++) {
			avgR += neighbors.get(i).getRed();
			avgG += neighbors.get(i).getGreen();
			avgB += neighbors.get(i).getBlue();
		}
		avgR /= neighbors.size();
		avgG /= neighbors.size();
		avgB /= neighbors.size();

		mode = new Color(avgR, avgG, avgB).getRGB();

		return mode;
	}
}
