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

public class Question02 extends Question {
    @Override
    public String getQuestion() {
        return "Hello " + Answer.getInstance().getA("Question01") + "!\n\n"
               + "This is a multiple choice question. The no. of choices shown in each page is controlled by flower.choice.size properties";
    }

    @Override
    public String getExplanation() {
        return "How old are you?";
    }

    @Override
    public ArrayList<Choice> choices() {
        ArrayList<Choice> al = new ArrayList<Choice>();

        al.add(new Choice("20-", "Younger than 20"));
        al.add(new Choice("30", "21-30"));
        al.add(new Choice("40", "31-40"));
        al.add(new Choice("50", "41-50"));
        al.add(new Choice("60", "51-60"));
        al.add(new Choice("70", "61-70"));
        al.add(new Choice("70+", "70 or higher"));

        return al;
    }

    @Override
    public boolean leaveQuestion() {
        String ans = Answer.getInstance().getA(this);

        if (ans.equals("20-")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Message leaveQuestionMsg(boolean flag) {
        if (flag) {
            return new Message(Message.INFO, "You are old enough to go to next question");
        } else {
            return new Message(Message.ERROR, "You are too young.");
        }
    }

    @Override
    public String lastAction() {
        return "Question01";
    }

    @Override
    public String nextAction() {
        return "Question03";
    }
    
    @Override
    public boolean showProgress() {
    	return true;
    }
}
