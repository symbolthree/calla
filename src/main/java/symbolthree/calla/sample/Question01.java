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

package symbolthree.calla.sample;

import symbolthree.calla.Question;

public class Question01 extends Question {
    public String getQuestion() {
        return "Welcome. This demostration will show you the features that CALLA can do.\n\n" + 
               "This question requires a non-empty free-text answer.\n";
    }

    public String getExplanation() {
        return "Press Enter your nickname (must be 5 to 10 characters):";
    }

    @Override    
    public boolean isMultipleChoices() {
        return false;
    }

    @Override
    public int minTextInputLength() {
    	return 5;
    }
    
    @Override
    public int maxTextInputLength() {
    	return 10;
    }    
    
    @Override
    public String nextAction() {
        return "Question02";
    }
    
    @Override
    public boolean isFreeTextInput() {
    	return true;
    }
}
