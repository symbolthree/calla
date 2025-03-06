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

import java.util.ArrayList;

import symbolthree.calla.Answer;
import symbolthree.calla.Choice;
import symbolthree.calla.Message;
import symbolthree.calla.Question;

public class Question03 extends Question {
    private static String SORTING = "question03.sorting";

    @Override
    public Message enterQuestionMsg(boolean flag) {
        String ans = Answer.getInstance().getA(this);

        if (ans == null) {
            return new Message(Message.INFO, "Choice Sorting");
        } else {
            return null;
        }
    }

    @Override
    public String getQuestion() {
        return "The choices can be sorted by key, description or the same order when you created the list. "
               + "In this question you can decide which sorting method to display the choices. Also the Exit option "
               + "is disabled; Back is allowed and it will go back to the first screen.\n";
    }

    @Override
    public String getExplanation() {
        return "Please select a fruit you like the most, or how the choices are sorted. Choice key is enclosed in the bracket. "
               + "Select Next to go to the next question.";
    }

    @Override
    public ArrayList<Choice> choices() {
        ArrayList<Choice> al = new ArrayList<Choice>();

        al.add(new Choice("R", "Orange        (R)"));
        al.add(new Choice("P", "Pinapple      (P)"));
        al.add(new Choice("N", "Banana        (N)"));
        al.add(new Choice("A", "Apple         (A)"));
        al.add(new Choice("T", "Watermelon    (T)"));
        al.add(new Choice("W", "Strawberry    (W)"));
        al.add(new Choice("V", "Cherry        (V)"));
        al.add(new Choice("Z1", "No sorting    (Z1)"));
        al.add(new Choice("Z2", "Sort by Key   (Z2)"));
        al.add(new Choice("Z3", "Sort by Desc. (Z3)"));
        al.add(new Choice("Z4", "Next          (Z4)"));

        return al;
    }

    @Override
    public String sortChoicesBy() {
        return (Answer.getInstance().getB(SORTING) == null)
               ? super.sortChoicesBy()
               : Answer.getInstance().getB(SORTING);
    }

    @Override
    public String lastAction() {
        return "symplik.flower.sample.Question01";
    }

    @Override
    public boolean leaveQuestion() {
        return (Answer.getInstance().getA(this).startsWith("Z"));
    }

    @Override
    public Message leaveQuestionMsg(boolean flag) {
        if (!flag) {
            return new Message(Message.INFO, "Choice key = " + Answer.getInstance().getA(this));
        }

        return null;
    }

    @Override
    public boolean showExit() {
        return false;
    }

    @Override
    public boolean isMultipleChoices() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Demo | " + this.getClass().getSimpleName();
    }

    @Override
    public String nextAction() {
        String ans = Answer.getInstance().getA(this);

        if (ans == null) {
            return "Question03";
        } else if (ans.equals("Z1")) {
            Answer.getInstance().putB(SORTING, SORT_AS_IS);

            return "Question03";
        } else if (ans.equals("Z2")) {
            Answer.getInstance().putB(SORTING, SORT_BY_KEY);

            return "Question03";
        } else if (ans.equals("Z3")) {
            Answer.getInstance().putB(SORTING, SORT_BY_DESC);

            return "Question03";
        } else if (ans.equals("Z4")) {
            return "Question04";
        }

        return this.getClass().getCanonicalName();
    }
}
