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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/ConsoleDirectoryBrowser.java $
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
 * Directory Browser running in Console mode
 * 
 * This class can be used in any program which requires text-based operation of
 * selecting a directory.
 * 
 * <pre>
 * ConsoleDirectoryBrowser cdb = new ConsoleDirectoryBrowser();
 * cdb.setDirPerPage(10);     // how many directories will be shown in each page
 * cdb.setShowExit(false);    // add an option [X] Exit 
 * String dir = cdb.selectDirectory();
 * </pre>
 *
 * The default directory is "user.dir" for the first time it is invoked.
 * The last directory selected will be saved as system property  
 * <code>symbolthree.calla.ConsoleDirectoryBrowser.currentDirectory</code>. 
 * Any new instance will use this value again when it starts.
 * 
 * So, in order to set the default directory, you need to
 * <pre>
 * System.setProperty(ConsoleDirectoryBrowser.CURR_DIR, [you dir]);
 * </pre>
 * 
 * @author  $Author: Christopher Ho $
 * @version $Revision: 3 $
 */
public class ConsoleDirectoryBrowser {

  public static final String  CURR_DIR           = "symbolthree.calla.ConsoleDirectoryBrowser.currentDirectory";
  static final Logger logger = LoggerFactory.getLogger(ConsoleDirectoryBrowser.class.getName());
  
  private static final String DIR_ROOT           = "ROOT_LIST";
  private static final String ACTION_LAST_PAGE   = "ACTION_LAST_PAGE";
  private static final String ACTION_NEXT_PAGE   = "ACTION_NEXT_PAGE";
  private static final String ACTION_UP_DIR      = "ACTION_UP_DIR";
  private static final String ACTION_DOWN_DIR    = "ACTION_DOWN_DIR";
  private static final String ACTION_SELECT_DIR  = "ACTION_SELECT_DIR";
  private static final String ACTION_EXIT        = "ACTION_EXIT";
  
  private ArrayList<String> answerKey = new ArrayList<String>();
  private String            action = "";
  private int               pageNo = 0;
  private int               dirPerPage  = 10;
  private boolean           showExit;
  
  public static void main(String[] args) {
    ConsoleDirectoryBrowser c = new ConsoleDirectoryBrowser();
    c.setShowExit(true);
    System.out.println("Selected Directory: " + c.selectDirectory());
  }
  
  public String selectDirectory() {

    String resp = null;
    
    while (! action.equals(ACTION_SELECT_DIR) && ! action.equals(ACTION_EXIT)) {
      
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
      String ans = response();
      try {
        if (ans.equalsIgnoreCase("X") && isShowExit()) {
          action = ACTION_EXIT;          
        } else if (ans.endsWith("d") || ans.endsWith("D")) {
          key = answerKey.get(Integer.parseInt(ans.substring(0, ans.length()-1))-1);
          if (! key.equals(ACTION_LAST_PAGE) &&
              ! key.equals(ACTION_NEXT_PAGE) &&
              ! key.equals(ACTION_UP_DIR) &&
              ! key.equals(ACTION_DOWN_DIR)) {
             action = ACTION_SELECT_DIR;
          } else {
             throw new Exception(); 
          }
        } else {
          key = answerKey.get(Integer.parseInt(ans)-1);

          if (key.equals(ACTION_LAST_PAGE)) {
            action = ACTION_LAST_PAGE;
          } else if (key.equals(ACTION_NEXT_PAGE)) {
            action = ACTION_NEXT_PAGE;
          } else if (key.equals(ACTION_UP_DIR)) {
            action = ACTION_UP_DIR;
          } else { 
            action = ACTION_DOWN_DIR;
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
      content = "Enter number to go up or down of a directory.\n" +
                "Enter number and letter D (e.g. 2d) to select a directory.\n\n" +
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
          File f = new File(dir, name);
            return f.isDirectory();
        }
      });
     
      int i = 0;
       
      if (getPageNo() == 0 ) {
        sb.append("[" + counter + "]  [..]\n");
        answerKey.add(ACTION_UP_DIR);
      } else {
        sb.append("[" + counter + "]  <- Last " + getDirPerPage()  + "\n");
        answerKey.add(ACTION_LAST_PAGE);
      }
      counter++;
       
      i = getPageNo() * getDirPerPage();
      
      while (i < (getPageNo()+1) * getDirPerPage() && i < files.length) {
         if (counter > 9) {
           sb.append("[" + counter + "] " + files[i] + "\n");
         } else {
           sb.append("[" + counter + "]  " + files[i] + "\n");
         }
         answerKey.add(getCurrDir() + File.separator + files[i]);
         i++;
         counter++;
      }
       
      int remain = files.length - (getPageNo() + 1) * getDirPerPage();
       
      remain = (remain > getDirPerPage())
               ? getDirPerPage()
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

  public void setDirPerPage(int dirPerPage) {
    this.dirPerPage = dirPerPage;
  }

  public int getDirPerPage() {
    return dirPerPage;
  }
}
