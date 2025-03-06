/******************************************************************************
 *
 * ≡ CALLA ≡
 * Copyright (C) 2009-2020 Christopher Ho 
 * All Rights Reserved, http://www.symbolthree.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * E-mail: christopher.ho@symbolthree.com
 *
******************************************************************************/

package symbolthree.calla;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point of CALLA framework.
 * <pre>
 * Usage: 
 * CALLA.bat | sh 
 * -display [CONSOLE | GUI | SILENT] 
 * -config [path to flower.properties file]
 * </pre>
 * If display is not specified, it will check whether a display is present, and use GUI if possible.
 * If config is not specified, flower.properties in current directory is used.  
 */
public class MAIN implements Constants {
    static final Logger logger = LoggerFactory.getLogger(MAIN.class.getName());

    /**
     * Main class
     * @param args
     */
    public static void main(String[] args) {
        String displayMode = "";
        String configFile  = "";

        int i = 0;
        
        if (args.length % 2 == 0) {
          try {
            while (i < args.length) {
              
              if (args[i].equals("-display")) {
                displayMode = args[i+1];
              }
              
              if (args[i].equals("-config")) {
                configFile = args[i+1];
              }
              i++;
            }
          } catch (Exception e) {
            showUsage();          
          }
        } else {
          showUsage();          
        }
        
        if (! configFile.equals("")) {
          File file = new File(configFile);
          
          if (!file.exists()) {
            System.out.println("Incorrect config file " + file.getAbsolutePath());
            showUsage();
          } else {
            System.setProperty(CALLA_CONFIG, file.getAbsolutePath());
          }
        }

        logger.info("CALLA Engine Start....");
        logger.debug("java home: " + System.getProperty("java.home"));
        logger.debug("java runtime: " + System.getProperty("java.runtime.version") + " by " + System.getProperty("java.specification.vendor"));
        logger.debug("java runtime arch: " +  System.getProperty("os.arch"));
        String osName = System.getProperty("os.name");
        logger.debug("os info: " + osName);
        if (osName.toUpperCase().startsWith("WIN")) {
        	String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        	String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
        	String realArch = arch.endsWith("64")
        	                  || wow64Arch != null && wow64Arch.endsWith("64")
        	                      ? "64" : "32";     
        	logger.debug("os arch " + realArch);
        }
        
        logger.debug("classpath: " + System.getProperty("java.class.path"));
                
        logger.info("displayMode = " + displayMode);
        
        if (displayMode.equals(DISPLAY_MODE_SLIENT)) {
            System.setProperty(DISPLAY_MODE, DISPLAY_MODE_SLIENT);
            SILENT.main();
        } else if (displayMode.equals(DISPLAY_MODE_GUI) && ! isGraphicDisplay()) {
            System.out.println("Unable to start in GUI mode.");
            System.exit(0);
        } else if (displayMode.equals(DISPLAY_MODE_GUI)) {
            System.setProperty(DISPLAY_MODE, DISPLAY_MODE_GUI);
            GUI.main();
        } else if (displayMode.equals(DISPLAY_MODE_CONSOLE)) {
            System.setProperty(DISPLAY_MODE, DISPLAY_MODE_CONSOLE);          
            CONSOLE.main();
        } else if (isGraphicDisplay()) {
            System.setProperty(DISPLAY_MODE, DISPLAY_MODE_GUI);          
            GUI.main();          
        } else if (!isGraphicDisplay()) {
            System.setProperty(DISPLAY_MODE, DISPLAY_MODE_CONSOLE);          
            CONSOLE.main();          
        }
    }

    private static void showUsage() {
      System.out.println("Optional argments:");      
      System.out.println("-display  [GUI|CONSOLE|SILENT]");
      System.out.println("-config   [location of config properties]");   
      System.exit(1);
    }
    
    private static boolean isGraphicDisplay() {
        try {
            Toolkit.getDefaultToolkit().getScreenResolution();
        } catch (HeadlessException he) {
            return false;
        }

        return true;
    }
}
