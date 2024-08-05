macro "Process Image Sequence" {
File.openSequence("C:/Users/Asus/OneDrive/Desktop/Spring202324/Image Processing/HW/HWs/HW1/dataset/", " filter=-11");
run("Gaussian Blur...", "sigma=2 stack");
run("Median...", "radius=2 stack");
run("Compile and Run...", "compile=C:/Users/Asus/Downloads/ij154-win-java8/ImageJ/plugins/Tools/HSV_Threshold.java");
run("Make Binary", "calculate black");
run("Open", "stack");
run("Close-", "stack");
//setTool("oval");
makeOval(193, 80, 280, 363);
run("Fit Ellipse");
run("Make Inverse");
setBackgroundColor(0, 0, 0);
run("Clear", "stack");
run("Image Sequence... ", "dir=[C:/Users/Asus/OneDrive/Desktop/Spring202324/Image Processing/HW/HWs/HW3/HW2_mask_algorithm/] format=PNG use");
}
