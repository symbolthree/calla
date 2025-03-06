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
******************************************************************************/

package symbolthree.calla;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 * Extended JTextPane to set the linewrap on and off 
 */
public class CallaJTextPane extends JTextPane {

  private boolean lineWrap = false;
  
  private static final long serialVersionUID = -6571474101706571931L;

  public CallaJTextPane() {
    super();
  }

  public CallaJTextPane(StyledDocument arg0) {
    super(arg0);
  }

  public boolean getScrollableTracksViewportWidth()  {
    return isLineWrap();
  }

  public void setLineWrap(boolean lineWrap) {
    this.lineWrap = lineWrap;
  }

  public boolean isLineWrap() {
    return lineWrap;
  }  
}
