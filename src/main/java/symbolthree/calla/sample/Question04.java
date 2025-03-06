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
import symbolthree.calla.Helper;
import symbolthree.calla.Message;
import symbolthree.calla.Question;

public class Question04 extends Question {
    private String CURRENT_FIELD  = "question04.currentField";
    private String LOGIN_FIELD    = "question04.loginName";
    private String PASSWORD_FIELD = "question04.password";

    @Override
    public String getQuestion() {
        String str = "A Question with a text field and a password field. \n";

        if ((getCurrentField() != null) && getCurrentField().equals(LOGIN_FIELD)) {
            str = str + "Please enter the login name (admin)";
        }

        if ((getCurrentField() != null) && getCurrentField().equals(PASSWORD_FIELD)) {
            str = str + "Please enter the password (password)";
        }

        return str;
    }

    @Override
    public String getExplanation() {
        String str      = "";
        String password = Answer.getInstance().getB(PASSWORD_FIELD);
        String masked   = "";

        if (password != null) {
            for (int i = 0; i < password.length(); i++) {
                masked = masked + "*";
            }
        }

        str = "Login Name : " + Helper.nullStr(Answer.getInstance().getB(LOGIN_FIELD)) + "\n" + "Password   : "
              + masked;

        return str;
    }

    @Override
    public boolean isMultipleChoices() {
        return (getCurrentField() == null);
    }

    @Override
    public boolean isPasswordInput() {
        return (getCurrentField() != null) && getCurrentField().equals(PASSWORD_FIELD);
    }

    @Override
    public ArrayList<Choice> choices() {
        ArrayList<Choice> al = new ArrayList<Choice>();

        al.add(new Choice(LOGIN_FIELD, "Enter your login name"));
        al.add(new Choice(PASSWORD_FIELD, "Enter your password"));
        al.add(new Choice("RESET", "Reset fields"));
        al.add(new Choice("SUBMIT", "Submit"));

        return al;
    }

    @Override
    public Message leaveQuestionMsg(boolean flag) {
        if (!flag) {
            return new Message(Message.ERROR, "Invalid login name / password combination.");
        } else {
            return null;
        }
    }

    @Override
    public String lastAction() {
        return "Question03";
    }

    @Override
    public boolean leaveQuestion() {
        String ans = Answer.getInstance().getA(this);

        if (getCurrentField() == null) {
            if (ans.equals("RESET")) {
                setCurrentField(null);
                Answer.getInstance().putB(PASSWORD_FIELD, null);
                Answer.getInstance().putB(LOGIN_FIELD, null);
            } else if (ans.equals("SUBMIT")) {
                String loginName = Answer.getInstance().getB(LOGIN_FIELD);
                String password  = Answer.getInstance().getB(PASSWORD_FIELD);

                if ((password == null) || (loginName == null) ||!password.equals("password")
                        ||!loginName.equals("admin")) {
                    return false;
                }
            } else {
                setCurrentField(ans);
            }
        } else if (getCurrentField().equals(LOGIN_FIELD)) {
            Answer.getInstance().putB(LOGIN_FIELD, ans);
            setCurrentField(null);
        } else if (getCurrentField().equals(PASSWORD_FIELD)) {
            Answer.getInstance().putB(PASSWORD_FIELD, ans);
            setCurrentField(null);
        }

        return true;
    }

    @Override
    public String nextAction() {
        String ans = Answer.getInstance().getA(this);

        if (ans.equals("SUBMIT")) {
            return "Question05";
        } else {
            return this.getClass().getCanonicalName();
        }
    }

    @Override
    public String getTitle() {
        return "Demo | " + this.getClass().getSimpleName();
    }

    private void setCurrentField(String str) {
        Answer.getInstance().putB(CURRENT_FIELD, str);
    }

    private String getCurrentField() {
        return Answer.getInstance().getB(CURRENT_FIELD);
    }
}
