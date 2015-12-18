package org.kevoree.webcam;



import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Input;
import org.kevoree.annotation.Output;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;

import com.github.sarxos.webcam.Webcam;


@ComponentType(description="Kevoree Component to get a picture to the webcam and send it in Base 64 String. Array of byte can be obtained using "
		+ "String s1 = java.net.URLDecoder.decode(STINGFROMPORT, \"ISO-8859-1\");\n"+
    	"Decoder base1 = Base64.getDecoder();\n"+
    	"byte[] b = base1.decode(s1);  \n")
public class WebCamCapture {


    @Output
    org.kevoree.api.Port out;

	private Webcam webcam;

    public static void main(String[] args) throws Exception {

    	Webcam webcam = Webcam.getDefault();
    	webcam.open();
    	BufferedImage image = webcam.getImage();
    	webcam.close();
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "jpg", baos);
    	baos.flush();
    	Encoder base = Base64.getEncoder();
    	String encodedImage = base.encodeToString(baos.toByteArray());
    	baos.close();
    	encodedImage = java.net.URLEncoder.encode(encodedImage, "ISO-8859-1");


    	
    	String s1 = java.net.URLDecoder.decode(encodedImage, "ISO-8859-1");
    	Decoder base1 = Base64.getDecoder();
    	byte[] b = base1.decode(s1);    	
    	FileOutputStream fos = new FileOutputStream(new File("/tmp/test2.jpg"));
    	fos.write(b);
    	fos.close();
	}
    
    
    @Input
    public void in(Object i) {
    	webcam.open();
    	
    	BufferedImage image = webcam.getImage();
    	
    	webcam.close();

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
			ImageIO.write(image, "jpg", baos);
		
    	baos.flush();
    	Encoder base = Base64.getEncoder();
    	String encodedImage = base.encodeToString(baos.toByteArray());
    	baos.close();
    	encodedImage = java.net.URLEncoder.encode(encodedImage, "ISO-8859-1");
    	out.send(encodedImage);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Start
    public void start() {
    	webcam = Webcam.getDefault();

    }

    @Stop
    public void stop() {
    }


}

