import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.text.TextWindow;

import java.util.Set;
import java.util.TreeSet;

public class ColorStatisticsAnalyzer implements PlugInFilter {
	private Set<Integer> colors = new TreeSet<>();
	private int[] colorCount = new int[256];
	private int[] minRbMean = new int[256];
	private int[] maxRbMean = new int[256];
	private long[] rbMeanSum = new long[256];
	private long[] rbMeanSquaredSum = new long[256];

	private int totalImages;
	private int currentImageIndex;

	@Override
	public int setup(String args, ImagePlus imp) {
		for (int i = 0; i < 256; i++) {
			minRbMean[i] = Integer.MAX_VALUE;
			maxRbMean[i] = Integer.MIN_VALUE;
		}
		totalImages = imp.getStackSize();
		currentImageIndex = 0;
		return DOES_RGB | DOES_STACKS;
	}

	private void addUniqueColor(int color) {
		if ((color & 0xFFFFFF) != 0) {  // Ignore black
			colors.add(color);
		}
	}

	@Override
	public void run(ImageProcessor processor) {
		int width = processor.getWidth();
		int height = processor.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int color = processor.getPixel(x, y);
				addUniqueColor(color);
			}
		}

		analyzeColors();

		colors.clear();
		currentImageIndex++;
		if (currentImageIndex == totalImages) {
			displayResults();
		}
	}

	private void analyzeColors() {
		for (int color : colors) {
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = color & 0xFF;

			int rbMean = (red + blue) / 2;
			int rbMeanSquared = rbMean * rbMean;

			colorCount[green]++;
			minRbMean[green] = Math.min(minRbMean[green], rbMean);
			maxRbMean[green] = Math.max(maxRbMean[green], rbMean);
			rbMeanSum[green] += rbMean;
			rbMeanSquaredSum[green] += rbMeanSquared;
		}
	}

	private void displayResults() {
		StringBuilder resultBuilder = new StringBuilder();
		resultBuilder.append("Green Component\tCount\tMin\tMax\tMean\tMean2\n");

		for (int i = 0; i < 256; i++) {
			int count = colorCount[i];
			int min = (minRbMean[i] == Integer.MAX_VALUE) ? 0 : minRbMean[i];
			int max = (maxRbMean[i] == Integer.MIN_VALUE) ? 0 : maxRbMean[i];
			int mean = (count > 0) ? (int) (rbMeanSum[i] / count) : 0;
			int meanSquared = (count > 0) ? (int) (rbMeanSquaredSum[i] / count) : 0;

			resultBuilder.append(String.format("%d\t%d\t%d\t%d\t%d\t%d\n",
					i, count, min, max, mean, meanSquared));
		}

		new TextWindow("Color Statistics Analysis", resultBuilder.toString(), 800, 600);
	}
}
