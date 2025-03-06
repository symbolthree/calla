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

/**
 * All constants used in CALLA framework
 */
public interface Constants {
 
  public static String       DISPLAY_MODE          = "symbolthree.calla.app.mode";
  public static String       DISPLAY_MODE_GUI      = "GUI";    
  public static String       DISPLAY_MODE_CONSOLE  = "CONSOLE";
  public static String       DISPLAY_MODE_SLIENT   = "SILENT";
  
  public static String       CALLA_CONFIG         = "calla.config";  
  public static String       CALLA_CONFIG_FILE    = "calla.properties";
  public static String       CALLA_RESPONSES      = "calla.responses";
  public static String       CALLA_EXIT_CONFIRM   = "calla.exit.confirm";
  public static String       CALLA_HELP_FILE      = "calla.help.file";
  public static String       CALLA_RESPONSES_FILE = "responses.xml";

  public static String       FLOWER_LAST_QUESTION  = "calla.lastQuestion";
  
  public static String       CLASS_DEFAULT_START   = "symbolthree.calla.DefaultQuestion";
  public static String       CLASS_FILE_BROWSER    = "symbolthree.calla.ConsoleFileBrowser";
  public static String       CLASS_DIR_BROWSER     = "symbolthree.calla.ConsoleDirBrowser";

  public static String       FILE_BROWSER_CURR_DIR = "calla.consleFileBroswer.currentDirectory";
  public static String       FILE_BROWSER_DIR      = "calla.consleFileBroswer.selectedDirectory";
  public static String       FILE_BROWSER_FILE     = "calla.consleFileBroswer.selectedtFile";

  // no sorting
  public static String       SORT_AS_IS            = "AS_IS";

  // case sensitve sorting
  public static String       SORT_BY_KEY           = "KEY";  
  public static String       SORT_BY_DESC          = "DESC";

  // case insensitve sorting
  public static String       SORT_BY_KEY_CI        = "KEY_CI";  
  public static String       SORT_BY_DESC_CI       = "DESC_CI";
  
}
