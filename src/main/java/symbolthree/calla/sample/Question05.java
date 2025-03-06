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

import symbolthree.calla.Answer;
import symbolthree.calla.Choice;
import symbolthree.calla.Helper;
import symbolthree.calla.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------


import java.util.ArrayList;

public class Question05 extends Question {
    private String CURRENT_FIELD  = "question05.currentField";
    private String DIR_FIELD      = "question05.dir";
    private String FILE_FIELD     = "question05.text";
    private String TXT_FILE_FIELD = "question05.txt_file";
    private static final Logger logger = LoggerFactory.getLogger(Question05.class.getName());

    @Override
    public String getQuestion() {
        String str          = "Question which prompt file and directory chooser.\n";
        String currentField = getCurrentField();

        if (currentField != null) {
            if (currentField.equals(FILE_FIELD)) {
                str = str + "Please select a file\n";
            } else if (currentField.equals(TXT_FILE_FIELD)) {
                str = str + "Please select a text file\n";
            } else if (currentField.equals(FILE_FIELD)) {
                str = str + "Please select a directory\n";
            }
        }

        return str;
    }

    @Override
    public String getExplanation() {
        String str = "";

        str = "Selected File     : " + Helper.nullStr(Answer.getInstance().getB(FILE_FIELD)) + "\n"
              + "Selected Text File: " + Helper.nullStr(Answer.getInstance().getB(TXT_FILE_FIELD)) + "\n"
              + "Selected Directory: " + Helper.nullStr(Answer.getInstance().getB(DIR_FIELD)) + "\n";

        return str;
    }

    @Override
    public String fileExtension() {
        if ((getCurrentField() != null) && getCurrentField().equals(TXT_FILE_FIELD)) {
            return "txt";
        } else {
            return null;
        }
    }

    @Override
    public boolean isFileInput() {
        return (getCurrentField() != null)
               && (getCurrentField().equals(FILE_FIELD) || getCurrentField().equals(TXT_FILE_FIELD));
    }

    @Override
    public boolean isDirectoryInput() {
        return (getCurrentField() != null) && getCurrentField().equals(DIR_FIELD);
    }

    @Override
    public boolean isMultipleChoices() {
        return getCurrentField() == null;
    }

    @Override
    public ArrayList<Choice> choices() {
        ArrayList<Choice> al = new ArrayList<Choice>();

        al.add(new Choice(FILE_FIELD, "Select a file"));
        al.add(new Choice(TXT_FILE_FIELD, "Select a text file (.txt)"));
        al.add(new Choice(DIR_FIELD, "Select a directory"));
        al.add(new Choice("RESET", "Reset values"));
        al.add(new Choice("RESTART", "Restart the application"));
        al.add(new Choice("NEXT", "Finish"));

        return al;
    }

    @Override
    public boolean needAnswer() {
        return false;
    }

    @Override
    public boolean leaveQuestion() {
        String ans = Answer.getInstance().getA(this);

        if (getCurrentField() == null) {
            if (ans.equals("RESET")) {
                setCurrentField(null);
                Answer.getInstance().putB(FILE_FIELD, null);
                Answer.getInstance().putB(TXT_FILE_FIELD, null);
                Answer.getInstance().putB(DIR_FIELD, null);
            } else {
                setCurrentField(ans);
            }
        } else if (getCurrentField().equals(FILE_FIELD)) {
            Answer.getInstance().putB(FILE_FIELD, ans);
            setCurrentField(null);
        } else if (getCurrentField().equals(TXT_FILE_FIELD)) {
            Answer.getInstance().putB(TXT_FILE_FIELD, ans);
            setCurrentField(null);
        } else if (getCurrentField().equals(DIR_FIELD)) {
            Answer.getInstance().putB(DIR_FIELD, ans);
            setCurrentField(null);
        }

        return true;
    }

    private String getCurrentField() {
        return Answer.getInstance().getB(CURRENT_FIELD);
    }

    @Override
    public String nextAction() {
        String ans = Answer.getInstance().getA(this);

        if (ans.equals("RESTART")) {
            Answer.getInstance().clearAll();

            return "Question01";
        }

        if (ans.equals(FILE_FIELD)) {
            return this.getClass().getCanonicalName();
        }

        if (ans.equals(TXT_FILE_FIELD)) {
            return this.getClass().getCanonicalName();
        }

        if (ans.equals("NEXT")) {
            return null;
        } else {
            return this.getClass().getCanonicalName();
        }
    }

    private void setCurrentField(String str) {
        Answer.getInstance().putB(CURRENT_FIELD, str);
        logger.debug("set current field: " + str);
    }

    @Override
    public boolean lineWrap() {
      return false;
    }
    
    @Override
    public String getTitle() {
        return "Demo | " + this.getClass().getSimpleName();
    }
}
