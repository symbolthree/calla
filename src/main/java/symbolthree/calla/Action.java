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
 * Abstract class for non-textual action between Questions.
 * To create an Action, you need to subclass of this class and override necessary method for you needs.  
 * Put a classAlias in flower.properties for easy referencing in Answer.instance.getA/putA method or 
 * nextAction method.
 * Sequence of method invoking:
 * <ul>
 * <li>enterAction</li>
 * <li>execute</li>
 * <li>nextAction</li>
 * </ul>
 */
public abstract class Action {
    /**
     * @return True if you want to carry on this Action class; False to skip this Action
     */
    public boolean enterAction() {
        return true;
    }

    /**
     * Put your main logic in this method 
     */
    public void execute() {}

    /**
     * @return Message object which contain textual message after execute has done.
     */
    public Message actionMessage() {
        return null;
    }

    /**
     * @return Next Action or Question class, in ClassAlias or full-qualified class name, followed by this class. 
     */
    public String nextAction() {
        return null;
    }
    
    /**
     * Override this method to set whether you want to show System.out to screen. For GUI mode only.
     * (Console mode always show System.out).
     * @return True if you want to display System.out.print() to the screen. Default is False.
     */
    public boolean showSystemOutput() {
        return false;
    }
}
