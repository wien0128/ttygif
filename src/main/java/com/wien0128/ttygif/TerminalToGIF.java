package com.wien0128.ttygif;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TerminalToGIF {

    private BufferedImage renderFrame(TerminalRecParser.Frame frame) {
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawString(frame.content, 10, 20);
        g.dispose();

        return image;
    }

    public void convertRecToGIF(String ttyrecPath, String gifPath) throws IOException {
        TerminalRecParser parser = new TerminalRecParser();
        List<TerminalRecParser.Frame> frames = parser.parse(ttyrecPath);

        try (ImageOutputStream output = new FileImageOutputStream(new File(gifPath))) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
            writer.setOutput(output);
            writer.prepareWriteSequence(null);

            for (TerminalRecParser.Frame frame : frames) {
                BufferedImage image = renderFrame(frame);
                IIOMetadataNode root = new IIOMetadataNode("javax_imageio_gif_image_1.0");
                IIOMetadataNode graphicsControlExtensionNode = new IIOMetadataNode("GraphicControlExtension");

                graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
                graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
                graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
                graphicsControlExtensionNode.setAttribute("transparentColorIndex", Integer.toString((int) (frame.timestamp / 10)));
                graphicsControlExtensionNode.setAttribute("transparentColorMode", "0");

                root.appendChild(graphicsControlExtensionNode);
                IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);
                metadata.mergeTree("javax_imageio_gif_image_1.0", root);

                writer.writeToSequence(new IIOImage(image, null, metadata), null);
            }

            writer.endWriteSequence();
        }
    }

    public static void main(String[] args) {
        try {
            TerminalToRec ttyrec = new TerminalToRec("output.ttyrec");
            ttyrec.write("Hello, ttyrec!\n");
            ttyrec.write("This is a second line.\n");
            ttyrec.close();

            TerminalToGIF ttygif = new TerminalToGIF();
            ttygif.convertRecToGIF("output.gif", "output.gif");

            System.out.println("Conversion to GIF completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}