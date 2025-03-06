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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class to read flower.properties file
 */
public class CallaProp implements Constants {
    private static CallaProp flowerProp = null;
    private Properties        prop       = new Properties();
    
    static final Logger logger = LoggerFactory.getLogger(CallaProp.class.getName());
    
    /**
     * Constructor to load flower.properties file
     */
    protected CallaProp() {
      
        String configFile = System.getProperty(CALLA_CONFIG);

        File file;

        String configFileSource = "";
        
        try {
          
          InputStream propInputStream = null;
          
          if (configFile==null || configFile.equals("")) {
              file = new File(System.getProperty("user.dir"), CALLA_CONFIG_FILE);
        	  configFileSource = file.getAbsolutePath();
              
              if (! file.exists()) {
                // try root of classpath
                propInputStream = this.getClass().getResourceAsStream("/" + CALLA_CONFIG_FILE);
                configFileSource = CALLA_CONFIG_FILE + " from classpath";                          
              
              } else {
                propInputStream = new FileInputStream(file);
              }
              
          } else {
            file = new File(configFile);
            configFileSource = file.getAbsolutePath();
            propInputStream = new FileInputStream(file);
          }
          
          prop.load(propInputStream);
          logger.debug("config properties = " + configFileSource);
          
        } catch (IOException e) {
            System.out.println("error in reading calla properties from " + configFileSource);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @return FlowerProp instance
     */
    public static CallaProp instance() {
        if (flowerProp == null) {
            flowerProp = new CallaProp();
        }

        return flowerProp;
    }

    /**
     * get properties value by key in flower.properties
     * @param key
     * @return properties value
     */
    public String get(String key) {
    	String rtnVal = prop.getProperty(key);
    	if (rtnVal != null && !rtnVal.equals("")) {
    		rtnVal = resolveVariables(rtnVal);
    	}
        return rtnVal;
    }
    
    public String getAlias(String fullClassName) {
    	String alias = null;
    	Set<Object> s = prop.keySet();
    	Iterator<Object> itr = s.iterator();
    	while (itr.hasNext()) {
    		alias = (String)itr.next();
    		if (prop.getProperty(alias).equals(fullClassName)) {
    			break;
    		}
    	}
    	return alias;
    }
    
    /**
     * resolve variables $J{java system variable} and $S{system environment variable} 
     * @param str
     * @return resolved string
     */
    private String resolveVariables(String str) {
    	String input = str;
    	Pattern vars;
    	Matcher m;
		
    	vars = Pattern.compile("[$J]\\{(.+?)\\}");
		m = vars.matcher(input);
		StringBuffer sb = new StringBuffer();
		int lastMatchEnd = 0;
        while (m.find()) {
            sb.append(input.substring(lastMatchEnd, m.start()-1));
            final String envVar = m.group(1);
            final String envVal = System.getProperty(envVar);
            if (envVal == null)
                sb.append(input.substring(m.start(), m.end()));
            else
                sb.append(envVal);
            lastMatchEnd = m.end();
        }
        sb.append(input.substring(lastMatchEnd));
        input = sb.toString();
        
		vars = Pattern.compile("[$S]\\{(.+?)\\}");
		m = vars.matcher(input);
		sb = new StringBuffer();
		lastMatchEnd = 0;
        while (m.find()) {
            sb.append(input.substring(lastMatchEnd, m.start()-1));
            final String envVar = m.group(1);
            final String envVal = System.getenv(envVar);
            if (envVal == null)
                sb.append(input.substring(m.start(), m.end()));
            else
                sb.append(envVal);
            lastMatchEnd = m.end();
        }
        sb.append(input.substring(lastMatchEnd));
        
        return sb.toString();
    }
}
