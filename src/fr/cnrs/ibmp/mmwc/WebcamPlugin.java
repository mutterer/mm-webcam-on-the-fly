///////////////////////////////////////////////////////////////////////////////
// An on-the fly processor micro-manager plugin that hacks the demo camera
// with images from webcams, thanks to the java webcam capture library.
// derived from ImageFlipper by Chris Weisiger
// at: https://github.com/micro-manager/micro-manager/tree/mm2/plugins/ImageFlipper/src/main/java/org/micromanager/imageflipper
//

package fr.cnrs.ibmp.mmwc;

import org.micromanager.data.ProcessorConfigurator;
import org.micromanager.data.ProcessorPlugin;
import org.micromanager.data.ProcessorFactory;

import org.micromanager.PropertyMap;
import org.micromanager.Studio;

import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;

@Plugin(type = ProcessorPlugin.class)
public class WebcamPlugin implements ProcessorPlugin, SciJavaPlugin {
   private Studio studio_;

   @Override
   public void setContext(Studio studio) {
      studio_ = studio;
   }

   @Override
   public ProcessorConfigurator createConfigurator(PropertyMap settings) {
      return new WebcamConfigurator(studio_, settings);
   }

   @Override
   public ProcessorFactory createFactory(PropertyMap settings) {
      return new WebcamFactory(settings, studio_);
   }

   @Override
   public String getName() {
      return "Webcam Capture";
   }

   @Override
   public String getHelpText() {
      return "Hacks the current camera with images from a webcam, using the webcam capture library";
   }

   @Override
   public String getVersion() {
      return "Version 1.0";
   }

   @Override
   public String getCopyright() {
      return "?";
   }

}
