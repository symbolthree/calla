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

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Helper class for CALLA
*/

public class Helper implements Constants {
  public static final String RCS_ID                =
    "$Header: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Helper.java 11    1/24/17 9:55a Christopher Ho $";

    //final static private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
    //private static FileWriter             logWriter;
    static final Logger logger = LoggerFactory.getLogger(Helper.class.getName());
    
    /**
     * @param text Input string
     * @param message Error message
     * @return integer value of this string
     * @throws Exception
     */
    public static int getInt(final String text, final String message) throws Exception {
        if (text == null) {
            throw new Exception(message);
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            throw new Exception("NumberFormatError: " + message);
        }
    }

    /**
     * @param text Input string
     * @param defaultVal return value if input string is empty or error
     * @return integer value of this string
     */
    public static int getInt(final String text, final int defaultVal) {
        if (text == null) {
            return defaultVal;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            return defaultVal;
        }
    }

    /**
     * @param Input string
     * @param defaultVal return value if input string is empty or error
     * @return boolean value of this string
     */
    public static boolean getBoolean(final String text, final boolean defaultVal) {
        if (text == null) {
            return defaultVal;
        }

        return text.equalsIgnoreCase("true");
    }

    /**
     * @param Input string
     * @param defaultVal return value if input string is empty
     * @return output string
     */
    public static String getString(final String text, final String defaultVal) {
        if (text == null || text.equals("")) {
            return defaultVal;
        }

        return text;
    }

    /**
     * @param color string representation of a color  (color name, #123456)
     * @param defaultValue return color value if input is empty or has error
     * @return color object of this string represented
     */
    public static Color getColor(final String color, final Color defaultValue) {
        if (color == null) {
            return defaultValue;
        }

        try {
            return Color.decode(color.toUpperCase());
        } catch (NumberFormatException nfe) {
            try {
                final Field f = Color.class.getField(color.toUpperCase());

                return (Color) f.get(null);
            } catch (Exception ce) {
                return defaultValue;
            }
        }
    }


    /**
     * @param str Font style in string (PLAIN, ITALIC, BOLD)
     * @param defaultStyle return style if error or empty
     * @return Font style
     */
    public static int getFontStyle(final String str, final int defaultStyle) {
        try {
            final Field f = Font.class.getField(str.toUpperCase());

            return f.getInt(null);
        } catch (Exception e) {
            return defaultStyle;
        }
    }

    /**
     * @param str Input String
     * @return True if the input string is a letter from A-Z, case insensitive
     */
    public static boolean isLetter(final String str) {
      String allLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      return allLetter.indexOf(str.toUpperCase())>=0?true:false;
    }
    
    
    /**
     * @param str Input string
     * @return True if input is a valid integer value
     */
    public static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Convert Log level integer value to String representation
     * @param logLevel
     * @return
     */
    
    /*
    private static String logLevelStr(int logLevel) {
        if (logLevel == LOG_DEBUG) {
            return "DEBUG";
        }

        if (logLevel == LOG_INFO) {
            return "INFO";
        }

        if (logLevel == LOG_WARN) {
            return "WARN";
        }

        if (logLevel == LOG_ERROR) {
            return "ERROR";
        }

        return String.valueOf("UNKNOWN");
    }
    */

    /**
     * Convert Log level string value to integer 
     * @param logLevel
     * @return
     */
    /*
    private static int logLevelInt(String logLevel) {
        if (logLevel.equals("DEBUG")) {
            return LOG_DEBUG;
        }

        if (logLevel.equals("INFO")) {
            return LOG_INFO;
        }

        if (logLevel.equals("WARN")) {
            return LOG_WARN;
        }

        if (logLevel.equals("ERROR")) {
            return LOG_ERROR;
        }

        return LOG_ERROR;
    }
    */

    /**
     * Usage: Helper.logError(throwable) = 
     * Helper.log(LOG_ERROR, error message) if log level > debug
     * Helper.log(LOG_ERROR, stacktrace) if log level = debug
     * @param t
     */
    
    /*
    public static void logError(Throwable t) {
        if (System.getProperty(FLOWER_LOG_LEVEL).equals(logLevelStr(LOG_DEBUG))) {
            log(LOG_ERROR, getStackTrace(t));
        } else {
            log(LOG_ERROR, t.getMessage());
        }
    }
    */

    /**
     * Usage: Helper.log(LOG_DEBUG | LOG_INFO | LOG_WARN | LOG_ERROR, log message)
     * @param logLevel
     * @param logMsg
     */
    
    /*
    public static void log(int logLevel, String logMsg) {
        int    logThreshold = logLevelInt(System.getProperty(FLOWER_LOG_LEVEL));
        String logOutput    = System.getProperty(FLOWER_LOG_OUTPUT);
        String logLine      = timeFormat.format(new java.util.Date()) + " - [" + logLevelStr(logLevel) + "] - "
                              + logMsg;

        if ((logLevel >= logThreshold) || (logLevel == LOG_ERROR)) {
            if (logOutput.equals(LOG_OUTPUT_SYSTEM_OUT)) {
                System.out.println(logLine);
            } else if (logOutput.equals(LOG_OUTPUT_FILE)) {
                try {
                    if (logWriter == null) {
                        logWriter = new FileWriter(new File(getLogFileDir(), FLOWER_LOG_FILE));
                    }

                    logWriter.write(logLine + System.getProperty("line.separator"));
                    logWriter.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    */
    
    /**
     * @param str input string
     * @return If input is null, an empty string ("") is returned.
     */
    public static String nullStr(String str) {
      if (str==null) {
        return "";
      } else {
        return str.trim();
      }
    }

    /*
    private static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw, true);

        t.printStackTrace(pw);
        pw.flush();
        sw.flush();

        return sw.toString();
    }
    */

    /**
     * @param f File object
     * @return extension of this file (return null if not found)
     */
    public static String getExtension(File f) {
        try {
            if (f != null) {
                String ext = null;
                String s   = f.getName();
                int    i   = s.lastIndexOf('.');

                if ((i >= 0) && (i < s.length() - 1)) {
                    ext = s.substring(i + 1).toLowerCase();
                }

                return ext;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * This method return object represents by input class alias
     * If class alias is not found, the input is treated as full-qualified class name
     * @param classAlias
     * @return new instance of such object
     */
    public static Object getRenderObject(String classAlias) {
      
      logger.debug("render alias=" + classAlias);
      
      if (classAlias == null || classAlias.equals("")) {
        logger.debug("render alias is empty");
        return null;
      }
  
      String className = CallaProp.instance().get(classAlias);
      
      if (className == null || className.equals("")) {
        className = classAlias;
      }
      
      try {
          Object clazz = Class.forName(className).newInstance();
          return clazz;

        } catch (InstantiationException e) {
          logger.error("Unable to instantiate class or alias " + className);
          return null;
        
        } catch (IllegalAccessException e) {
          logger.error("Unable to access class or alias " + className);
          return null;          
        
        } catch (ClassNotFoundException e) {
          logger.error("Unable to find class or alias " + className);
          return null;          
        }        

    }
    
    /**
     * Sorting Choices according to sort parameter
     * @param al ArrayList of Choice object
     * @param sort SORT_AS_IS (no sort), SORT_BY_KEY (internal choice key), SORT_BY_DESC (choice description)
     *             SORT_BY_KEY_CI (internal choice key, case sensitive), SORT_BY_DESC_CI (choice description, case insensitive)
     * @return sorted ArrayList of Choice object
     */
    public static ArrayList<Choice> sortChoices(ArrayList<Choice> al, String sort) {
      if (sort.equals(SORT_AS_IS)) return al;      
      
      Hashtable<String, Choice> ht = new Hashtable<String, Choice>();
      ArrayList<Choice> sortedAl   = new ArrayList<Choice>();        
      ArrayList<String> arry       = new ArrayList<String>();
          
      for (int i=0;i<al.size();i++) {
          
          String idx = null;
          if (sort.equals(SORT_BY_KEY)) {
            idx = al.get(i).getChoiceKey();
            
          } else if (sort.equals(SORT_BY_KEY_CI)){
            idx = al.get(i).getChoiceKey().toUpperCase();
            
          } else if (sort.equals(SORT_BY_DESC)) {
            idx = al.get(i).getChoiceDesc();
            
          } else if (sort.equals(SORT_BY_DESC_CI)) {
            idx = al.get(i).getChoiceDesc().toUpperCase();
          }
          arry.add(idx);
          ht.put(idx, al.get(i));
      }
      Collections.sort(arry);
    
      for (int i=0;i<al.size();i++) {
          sortedAl.add(ht.get(arry.get(i)));
      }
      
      return sortedAl;
    }
    
    /*
    private static String getLogFileDir() {
    	String defaultDir = System.getProperty("java.io.tmpdir");
    	String rtnVal = defaultDir;

    	String dir = FlowerProp.instance().get(FLOWER_LOG_DIR);
    	if (dir !=null && ! dir.equals("")) {
    		File testDir = new File(dir);
    		if (testDir.exists() && testDir.isDirectory()) {
    			rtnVal = testDir.getAbsolutePath();
    		} else {
    			boolean mkdirs = testDir.mkdirs();
    			if (mkdirs) rtnVal = testDir.getAbsolutePath();
    		}
    	}
    	return rtnVal;
    }
    */
    
    public static int getChoiceSize() {
        return Helper.getInt(CallaProp.instance().get("calla.choice.size"), 0);
    }
    
    public static int getChoiceMax() {
    	return Helper.getInt(CallaProp.instance().get("flcallaower.choice.max"), 99999);
    }
    
    public static int getChoicePage() {
    	logger.debug("calla.choice.page is " + System.getProperty("calla.choice.page"));
    	return getInt(System.getProperty("calla.choice.page"),0);
    }
    
    public static void addChoicePage(int no) {
    	System.setProperty("calla.choice.page", String.valueOf(getChoicePage()+no));
    	//Helper.log(Helper.LOG_DEBUG, "flower.choice.page is added as " + System.getProperty("flower.choice.page"));
    }
    
    public static void resetChoicePage() {
    	System.setProperty("calla.choice.page", String.valueOf(0));
    	//Helper.log(Helper.LOG_DEBUG, "flower.choice.page is reset as " + System.getProperty("flower.choice.page"));

    }
    
    public static String getChoiceDescByKey(ArrayList<Choice> al, String key) {
    	Iterator<Choice> itr = al.iterator();
    	String rtnVal = null;
    	while (itr.hasNext()) {
    		Choice c = (Choice)itr.next();
    		if (c.getChoiceKey().equals(key)) {
    			rtnVal = c.getChoiceDesc();
    			break;
    		}
    	}
    	return rtnVal;
    }
    
	public static String[] listToArray(ArrayList<String> _list) {
	    String[] strArray = new String[_list.size()];
	    strArray = _list.toArray(strArray);
	    return strArray;
	}
	
	public static String printArray(ArrayList<String> _list) {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<_list.size();i++) {
			sb.append(_list.get(i) + " ");
		}
		return sb.toString();
	}
	
	public static boolean isLOVAnswer(String str) {
		if (str==null || str.equals("")) return false;
		
		if (str.length() > 2 && str.startsWith("#") &&  str.endsWith("#")) {
		   return true;
		} else {
		  return false;
		}
	}
	
	public static String setLOVKey(String str) {
		return "#" + str + "#";
	}
	
	public static String getLOVKey(String str) {
		return str.substring(1, str.length()-1);
	}
	
	public static String getCALLAVersion() {
        InputStream is   = Helper.class.getResourceAsStream("/build.properties");
        Properties  prop = new Properties();

        try {
            prop.load(is);
        } catch (Exception e) {}

        return prop.getProperty("build.version") + " build " + prop.getProperty("build.number");
	}
	
}
