///////////////////////////////////////////////////////////////////////////////
// An on-the fly processor micro-manager plugin that hacks the demo camera
// with images from webcams, thanks to the java webcam capture library.
// derived from ImageFlipper by Chris Weisiger
// at: https://github.com/micro-manager/micro-manager/tree/mm2/plugins/ImageFlipper/src/main/java/org/micromanager/imageflipper
//

package fr.cnrs.ibmp.mmwc;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.data.Metadata;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorContext;

import com.github.sarxos.webcam.Webcam;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class WebcamProcessor extends Processor {

   public static Webcam webcam;


   private Studio studio_;
   String camera_;
   int webcam_;
   boolean arg1_;
   Dimension resolution_;

   public WebcamProcessor(Studio studio, String camera,int webcamIndex, Dimension d) {
      studio_ = studio;
      camera_ = camera;
      webcam_ = webcamIndex;
      
      webcam = Webcam.getWebcams().get(webcamIndex);
      if (!webcam.isOpen())      
    	  {	webcam.setViewSize(d);
    	  	webcam.open();
    	  }
      while (!webcam.isImageNew()) IJ.wait(1);

      resolution_ = d;
   }

   /**
    * Process one image.
    */
   @Override
   public void processImage(Image image, ProcessorContext context) {
      String imageCam = image.getMetadata().getCamera();
      if (imageCam == null || !imageCam.equals(camera_) || ! webcam.isOpen()) {
         // Image is for the wrong camera; just pass it along unmodified.
         context.outputImage(image);
         return;
      }
      context.outputImage(
            transformImage(studio_, image, arg1_, resolution_));
   }

   /**
    * Executes image transformation
    */
   
   public static Image transformImage(Studio studio, Image image,
         boolean arg1, Dimension resolution_2) {
	  
	   
	  ImageProcessor proc = studio.data().ij().createProcessor(image);
      PropertyMap.PropertyMapBuilder builder;
      PropertyMap userData = image.getMetadata().getUserData();
      if (userData != null) {
         builder = userData.copy();
      }
      else {
         builder = studio.data().getPropertyMapBuilder();
      }
      builder.putString("Image source:", "webcam capture");
      Metadata newMetadata = image.getMetadata().copy().userData(builder.build()).build();
      
      BufferedImage i = webcam.getImage();
      ImagePlus imp = new ImagePlus("tmp", i);
      
      // convert to byte processor, because creating rgb image does not work for now.
      // otherwise, we would:
      // Object pixels = imp.getProcessor().convertToFloat().getPixels();      
      // Image result = studio.data().createImage(pixels, resolution_2.width, resolution_2.height, 4, 3, image.getCoords(), newMetadata);

      proc = imp.getProcessor().convertToByte(true);
      Image result = studio.data().ij().createImage(proc, image.getCoords(), newMetadata);
      

      return result;
   }
}
