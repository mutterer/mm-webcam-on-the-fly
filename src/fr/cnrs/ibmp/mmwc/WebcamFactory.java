///////////////////////////////////////////////////////////////////////////////
// An on-the fly processor micro-manager plugin that hacks the demo camera
// with images from webcams, thanks to the java webcam capture library.
// derived from ImageFlipper by Chris Weisiger
// at: https://github.com/micro-manager/micro-manager/tree/mm2/plugins/ImageFlipper/src/main/java/org/micromanager/imageflipper
//

package fr.cnrs.ibmp.mmwc;

import java.awt.Dimension;

import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.data.Processor;
import org.micromanager.data.ProcessorFactory;

public class WebcamFactory implements ProcessorFactory {
   private PropertyMap settings_;
   private Studio studio_;

   public WebcamFactory(PropertyMap settings, Studio studio) {
      settings_ = settings;
      studio_ = studio;
   }

   @Override
   public Processor createProcessor() {
      return new WebcamProcessor(studio_, 
    		settings_.getString("camera", ""),
    		settings_.getString("webcam", ""),
    		new Dimension(settings_.getInt("width",640), settings_.getInt("width",480))
      );
   }
}
