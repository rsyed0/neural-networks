import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Segmenter {

	private BufferedImage img;
	private int width,height;
	
	public Segmenter(String imgName){
		
		try{
			img = ImageIO.read(new File(imgName));
			width = img.getWidth();
			height = img.getHeight();
		}catch (IOException e){
			System.err.println("Couldn't load image from "+imgName+".");
			System.exit(1);
		}
		
	}
	
	public Segmenter(BufferedImage img){
		this.img = img;
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public List<BufferedImage> horizontalSplit(Network net){
		
		List<BufferedImage> imgs = new ArrayList<>();
		double[][] brightness = new double[height][width];
		
		for(int c=0;c<width;c++){
			for(int r=0;r<height;r++){
				int color = img.getRGB(c,r);

				// extract each color component
				int red = (color >>> 16) & 0xFF;
				int green = (color >>>  8) & 0xFF;
				int blue = (color >>>  0) & 0xFF;

				// calculate brightness in range 0.0 to 1.0
				brightness[r][c] = 1.0-((red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255);
            }
        }
		
		int endX=1,startX=0;
		double lastMax = 0.0;
		
		while (endX<width){
			
			// set up brightness array
			double[] imgBrightness = new double[net.getNetwork().get(0).length];
			int i=0;
			for (int c=startX;c<endX;c++){
				for (int r=0;r<height;r++){
					imgBrightness[i] = brightness[r][c];
					i++;
				}
			}
			
			// run, get maximum
			List<double[]> imgOutput = net.run(imgBrightness);
			double max = 0.0;
			for (double out:imgOutput.get(imgOutput.size()-1))
				max = ((out>max) ? out : max);
			
			if (max < lastMax){
				imgs.add(img.getSubimage(startX,0,endX-startX,height));
				startX = endX;
			}else lastMax = max;
			
			endX++;
		}
		
		return imgs;
		
	}
	
}
