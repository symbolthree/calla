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
 * ================================================
 *
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/ConsoleFileBrowser.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 3 $
******************************************************************************/

package symbolthree.calla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File Browser running in Console mode
 * 
 * This class can be used in any program which requires text-based operation of
 * selecting a file, with or without specified file extension.
 * 
 * <pre>
 * ConsoleFileBrowser cfb = new ConsoleFileBrowser();
 * cfb.setFilePerPage(10);     // how many file plus directory will be shown in each page
 * cfb.setShowExit(false);     // add an option [X] Exit
 * cfb.setExtension("txt")     // set what file extension will be shown
 * String file = cdb.selectFile();
 * </pre>
 *
 * The default directory is "user.dir" for the first time it is invoked.
 * The last directory selected will be saved as system property  
 * <code>symplik.flower.ConsoleFileBrowser.currentDirectory</code>. 
 * Any new instance will use this value again when it starts.
 * 
 * So, in order to set the default directory, you need to
 * <pre>
 * System.setProperty(ConsoleFileBrowser.CURR_DIR, [you dir]);
 * </pre>
 * 
 * @author  $Author: Christopher Ho $
 * @version $Revision: 3 $
 */
public class ConsoleFileBrowser {

  public static final String CURR_DIR            = "symplik.flower.ConsoleFileBrowser.currentDirectory";
  static final Logger logger = LoggerFactory.getLogger(ConsoleFileBrowser.class.getName());  
  
  private static final String DIR_ROOT           = "ROOT_LIST";
  private static final String ACTION_LAST_PAGE   = "ACTION_LAST_PAGE";
  private static final String ACTION_NEXT_PAGE   = "ACTION_NEXT_PAGE";
  private static final String ACTION_UP_DIR      = "ACTION_UP_DIR";
  private static final String ACTION_DOWN_DIR    = "ACTION_DOWN_DIR";
  private static final String ACTION_SELECT_FILE = "ACTION_SELECT_DIR";
  private static final String ACTION_EXIT        = "ACTION_EXIT";
  
  private ArrayList<String> answerKey    =  new ArrayList<String>();
  private String            action       = "";
  private int               pageNo       = 0;
  private int               filePerPage  = 15;  
  private boolean           showExit     = false;
  private String            extension    = null;  
  
  
  public static void main(String[] args) {
    ConsoleFileBrowser c = new ConsoleFileBrowser();
    c.setExtension("TXT");
    c.setShowExit(true);
    System.out.println("Selected file: " + c.selectFile());
  }
  
  public String selectFile() {

    String resp = null;
    
    while (! action.equals(ACTION_SELECT_FILE) && ! action.equals(ACTION_EXIT)) {
      
      if (action.equals(ACTION_UP_DIR)) {
        upDir();
      } else if (action.equals(ACTION_NEXT_PAGE)) {
        nextPage();
      } else if (action.equals(ACTION_LAST_PAGE)) {
        lastPage();
      } else if (action.equals(ACTION_DOWN_DIR)) {
        downDir(resp);
      } 
      resp = browser();
    }

    return resp;
  }
  
  private String browser() {
    showQuestion();
    showChoices();
    
    String key = null;
    
    boolean validAns = false;
    while (!validAns) {
      // ans = 1,2,3...n or X
      String ans = response();
      try {
        if (ans.equalsIgnoreCase("X") && isShowExit()) {
          action = ACTION_EXIT;     
        
        } else {
          
          key = answerKey.get(Integer.parseInt(ans)-1);
          
          File file;
          if (key.startsWith("[") && key.endsWith("]")) {
            key = key.substring(1, key.length()-1);
            file = new File(getCurrDir(), key);
            if (file.isDirectory()) {
              action = ACTION_DOWN_DIR;
              return file.getAbsolutePath();
            }
          }
          
          if (getCurrDir().equals(DIR_ROOT)) {
            action = ACTION_DOWN_DIR;
            return key;
          }
          
          if (key.equals(ACTION_LAST_PAGE)) {
            action = ACTION_LAST_PAGE;
          } else if (key.equals(ACTION_NEXT_PAGE)) {
            action = ACTION_NEXT_PAGE;
          } else if (key.equals(ACTION_UP_DIR)) {
            action = ACTION_UP_DIR;
          } else { 
            action = ACTION_SELECT_FILE;
            file = new File(getCurrDir(), key);
            key = file.getAbsolutePath();
          }
        }      
        validAns = true;
      
      } catch (Exception e) {
        //e.printStackTrace();
        System.out.println("Invalid value. Please enter again.");
      }
    }
    
    return key;
  }
  
  private void showQuestion() {
    String content = null;
    if (getCurrDir().equals(DIR_ROOT)) {
      content = "Current directory : Computer";      
    } else {
      content = "Enter number to go up or down of a directory, or select a file.\n\n" +
                "Current directory : " + getCurrDir();
    }
    System.out.println(content);
  }
  
  private void showChoices() {
    answerKey.clear();
    
    StringBuffer sb = new StringBuffer();
    int counter = 1;
    
    if (getCurrDir().equals(DIR_ROOT)) {
      File[] allDrives = File.listRoots();
      for (int i=0;i<allDrives.length;i++) {
        sb.append("[" + (i+1) + "] " + allDrives[i].getAbsolutePath() + "\n");
        answerKey.add(allDrives[i].getAbsolutePath());
      }
    
    } else {
      File currDirF = new File(getCurrDir());
    
      String[] files = currDirF.list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          if (extension != null) { 
            return name.toUpperCase().endsWith(extension) || 
                   (new File(dir,name)).isDirectory();
          } else {
            return true;
          }
        }
      });
     
      int i = 0;
       
      if (getPageNo() == 0 ) {
        sb.append("[" + counter + "]  [..]\n");
        answerKey.add(ACTION_UP_DIR);
      } else {
        sb.append("[" + counter + "]  <- Last " + getFilePerPage()  + "\n");
        answerKey.add(ACTION_LAST_PAGE);
      }
      counter++;
       
      i = getPageNo() * getFilePerPage();

      // construct a complete list, dir first then file
      ArrayList<String> allAl = new ArrayList<String>();
      ArrayList<String> dirAl = new ArrayList<String>();
      ArrayList<String> fileAl = new ArrayList<String>();
      
      for(int j=0;j<files.length;j++) {
        File f = new File(getCurrDir(), files[j]);
        if (f.isDirectory()) {
          dirAl.add(files[j]);
        } else if (f.isFile()) {
          fileAl.add(files[j]);
        }
      }

      for (int j=0;j<dirAl.size();j++) {
        allAl.add("[" + dirAl.get(j) + "]"); 
      }
      
      for (int j=0;j<fileAl.size();j++) {
        allAl.add(fileAl.get(j)); 
      }      

      while (i < (getPageNo()+1) * getFilePerPage() && i < files.length) {

         if (counter > 9) {
           sb.append("[" + counter + "] " + allAl.get(i) + "\n");
         } else {
           sb.append("[" + counter + "]  " + allAl.get(i) + "\n");
         }
         answerKey.add(allAl.get(i));
         i++;
         counter++;
      }
       
      int remain = allAl.size() - (getPageNo() + 1) * getFilePerPage();
       
      remain = (remain > getFilePerPage())
               ? getFilePerPage()
               : remain;
      if (remain > 0) {
         sb.append("[" + counter + "] Next " + remain + " ->\n");
         answerKey.add(ACTION_NEXT_PAGE);
         counter++;
      }
    }

    if (isShowExit()) {
      sb.append("\n[x] Exit Browser\n");
      answerKey.add(ACTION_EXIT);
    }
    
    System.out.println(sb.toString());
  }
  
  private String response() {
    try {
      System.out.print(">> ");

      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader    br  = new BufferedReader(isr);

      String answerStr = br.readLine().trim();
      
      return answerStr;

    } catch (IOException e) {
        //Helper.logError(e);
    	logger.error("response error", e);
        return null;
    }
  }
  
  private void downDir(String dir) {
    System.setProperty(CURR_DIR, dir);
    pageNo = 0;
  }
  
  private void upDir() {
    
    File up = new File(getCurrDir() + File.separator + "..");

    try {
      if (up.getCanonicalFile().equals(new File(getCurrDir()))) {
        System.setProperty(CURR_DIR, DIR_ROOT);
      } else {
        System.setProperty(CURR_DIR, up.getCanonicalPath());
      }
      
      pageNo = 0;
      
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  
  private String getCurrDir() {
    if (System.getProperty(CURR_DIR)==null) {
      return System.getProperty("user.dir");
    } else {
      return System.getProperty(CURR_DIR);
    }
  }
  
  private int getPageNo() {
    return pageNo;
  }
  
  private void nextPage() {
    pageNo++; 
  }
  
  private void lastPage() {
    pageNo--;
  }

  public void setShowExit(boolean showExit) {
    this.showExit = showExit;
  }

  public boolean isShowExit() {
    return showExit;
  }
  
  public String getExtension() {
    return extension;
  }

  public void setExtension(String ext) {
    if (ext != null && !ext.startsWith(".")) {
      this.extension = "." + ext.toUpperCase();
    } else {
      this.extension = null;
    }
  }

  public void setFilePerPage(int filePerPage) {
    this.filePerPage = filePerPage;
  }

  public int getFilePerPage() {
    return filePerPage;
  }
  
}
