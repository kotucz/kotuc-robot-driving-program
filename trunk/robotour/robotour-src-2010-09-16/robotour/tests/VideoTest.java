package robotour.tests;

/*
 * VideoFrame.java
 *
 * Created on 3. srpen 2006, 21:09
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.media.protocol.*;

import java.util.*;

import java.io.*;

/**
 *
 * @author  PC
 */
class VideoTest {
    
    Player p;
    Processor proc;
    VideoFormat format;
    
    Component controlPanel, visualComponent; 
    
    public void startVideo() {
//        CaptureDeviceInfo deviceInfo = CaptureDeviceManager.getDevice("deviceName");
        
//        format = new javax.media.format.();
//         format = new javax.media.format.YUVFormat(YUVFormat.YUV_YUYV);
//        format = new javax.media.format.RGBFormat();
         
         format = null;
         
         // Get the CaptureDeviceInfo for the live audio capture device 
         Vector<CaptureDeviceInfo> deviceList = CaptureDeviceManager.getDeviceList(format); 
         println("devices:");
         int i= 0;
         for (CaptureDeviceInfo di: deviceList) {        
            println("device "+i+" "+di);
//            println("formats: ");
//            for (Format f:di.getFormats()) {
//                 println("  "+f);
//            }
            i++;
         }
         
         CaptureDeviceInfo di = deviceList.get(2);         
         

         
         // Create a Player for the capture device: 
         try{
             format = (VideoFormat)di.getFormats()[7];
             
             p = Manager.createRealizedPlayer(di.getLocator()); 
             p.prefetch();
             p.start();
             
             println("player created");
             
             fgc = (FrameGrabbingControl)p.getControl("javax.media.control.FrameGrabbingControl");

//           proc = Manager.createRealizedProcessor(new ProcessorModel(di.getLocator(), new Format[] {format}, new FileTypeDescriptor(FileTypeDescriptor.MSVIDEO)));
//             if (fgc==null) fgc = (FrameGrabbingControl)proc.getControl("javax.media.control.FrameGrabbingControl");;
             
             visualComponent = p.getVisualComponent();
             controlPanel = p.getControlPanelComponent();
            
                  
             System.out.println(fgc);             
                 
             
         } catch (Exception ex) { 
            ex.printStackTrace();
         }  finally {
             
         }

    }
    
    
    private FrameGrabbingControl fgc;
        
    private BufferToImage bti;
          
    
    public BufferedImage snap() {
        Buffer buf = fgc.grabFrame();

        bti = new BufferToImage((VideoFormat)buf.getFormat());
        
        return (BufferedImage)bti.createImage(buf);
       
    }

    
    
    public void close() {
        if (p!=null) p.close();
        if (proc!=null) proc.close();
        println("closed");
    }
 
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                               
                VideoTest vi = new VideoTest();
                vi.startVideo();
                JFrame vf = new JFrame();
//                vf.setLayuo
                
                vf.add(vi.visualComponent);
//                vf.add(vi.controlPanel);
                vf.pack();
                
                vf.setVisible(true);
            }
        });
    }

    
    protected void println(String s) {
        System.out.println(s);
    }
    
}
