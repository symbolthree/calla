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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Executor.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 4 $
******************************************************************************/

package symbolthree.calla;

/**
 * Executor to run Action class 
 * 
 * @author  $Author: Christopher Ho $
 * @version $Revision: 4 $
 */
public class Executor {
    public static final String RCS_ID =
        "$Header: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Executor.java 4     1/24/17 9:55a Christopher Ho $";
    
    /**
     * @param Action subclass of Action
     */
    public static void execute(Action action) {
        boolean valid = action.enterAction();

        if (valid) {
            action.execute();
        }
    }
}
