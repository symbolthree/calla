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

public class DefaultQuestion extends Question {

  @Override  
  public String getQuestion() {
      return "Invalid start question. Press Enter to quit the program.\n";
  }

  @Override  
  public String getExplanation() {
    return "Please define a valid calla.start value in calla.properties file.";
  }
  
  @Override  
  public boolean isMultipleChoices() {
      return false;
  }

  @Override  
  public boolean needAnswer() {
      return false;
  }
  
  @Override  
  public String nextAction() {
      return "";
  }
}
