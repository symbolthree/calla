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
 * Choice consists of choiceKey and choiceDesc(ription).
 * ChoiceKey is the value used internally to represent a multiple choice;
 * while choiceDesc is the wording showing to the end users. 
 * Multiple choice is always shown in a numeric list in the screen.
 * 
 * For each Question, the corresponding answer is the choiceKey, which is stored 
 * in the Response.xml file for playback use (run in silent operation).
 */
public class Choice {
    private String choiceDesc;
    private String choiceKey;

    /**
     * Constructor.
     * @param Choice Key
     * @param Choice Description
     */
    public Choice(String key, String desc) {
        this.choiceKey  = key;
        this.choiceDesc = desc;
    }

    /**
     * ChoiceKey Setter method
     * @param choiceKey
     */
    public void setChoiceKey(String choiceKey) {
        this.choiceKey = choiceKey;
    }

    /**
     * ChoiceKey Getter method
     * @return
     */
    public String getChoiceKey() {
        return choiceKey;
    }

    /**
     * ChoiceDesc(ription) Setting method
     * @param choiceDesc
     */
    public void setChoiceDesc(String choiceDesc) {
        this.choiceDesc = choiceDesc;
    }

    /**
     * ChoiceDesc(ription) Getting method
     * @return
     */
    public String getChoiceDesc() {
        return choiceDesc;
    }
}
