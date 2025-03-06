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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Question.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 12 $
******************************************************************************/

package symbolthree.calla;

import java.util.ArrayList;

/**
 * This is the abstract class for any screen which requires user interaction or 
 * displays textual information.
 * 
 * The sequence of method to be executed when a Question is instantiated:
 * <ul>
 * <li>enterQuestion</li>
 * <li>getQuestion</li>
 * <li>getExplanation</li>
 * <li>leaveQuestion</li>
 * <li>nextAction</li> 
 * </ul>
 * To create a Question, you must extend this class and override any method if needed.
 * You can create alias for your subclass in the flower.properties file by:
 * <pre>classAliasXXX = com.blah.questionXXX</pre>
 * You can use this alias to get the answer:
 * <pre>Answer.getInstance().getA("classAlias")</pre>
 * 
 * <code>
 * public String nextAction() {
 *     return "classAliasXXX";
 * }
 * </code>
 *
 */ 
public abstract class Question implements Constants {

    /**
     * override this method to tell whether this question will be shown
     * @return  True if you want to carry on; False to give up this question
     */
    public boolean enterQuestion() {

        return true;
    }

    /**
     * override this method to return a message after enterQuestion is processed.
     *
     * @param  flag return value of enterQuestion.
     * @return message object to be displayed    
     */
    public Message enterQuestionMsg(boolean flag) {

        return null;
    }

    /**
     * override this method to construct an sorted Arraylist of Choice object
     * Arraylist can be sorted by the sortChoicesBy method.
     * e.g.
     * <pre>
       ArrayList<Choice> al = new ArrayList<Choice>();
       al.add(new Choice("A", "Option A");
       al.add(new Choice("B", "Option B");
       al.add(new Choice("C", "Option C");
     * </pre>
     * @return the ArrayList of Choice object
     */
    public ArrayList<Choice> choices() {

        return new ArrayList<Choice>();
    }

    /**
     * override this method to return the question content.
     * @return The content of this question
     */
    public String getQuestion() {

        return null;
    }

    /**
     * This method is for multiple choice question only.
     * @return False if you don't want to see the "[x] Exit" option. Default is True.  
     */
    public boolean showExit() {

      return true;
    }

    /**
     * Override this method if you want to see progress bar/message in-between Question.  
     * @return True if you want to see the progress bar (GUI) or "Please wait" (CONSOLE). Default is False.  
     */
    public boolean showProgress() {
    	return false;
    }
    
    /**
     * override this method if you want to show explanation of this question. This content 
     * is shown below the Question content.
     * @return explanation content
     */
    public String getExplanation() {

      return null;
  }

    /**
     * override this method if the input field is a password. The response will be 
     * masked when entered. Work for free-text input (isMultipleChoices is False). 
     * @return  True if masking is needed. Default is False.
     */
    public boolean isFreeTextInput() {

      return false;
    }
    
    
    /**
     * override this method if you want to add extra empty lines if the no. of multiple choices is 
     * less than the flower.choice.size.
     * @return  Default is true
     */    
    public boolean fixMultipleChoiceHeight() {
    	return true;
    }
    
    /**
     * override this method if the free-text answer requires at least certain characters. 
     * @return  Default is -1 (unlimited)
     */    
    public int minTextInputLength() {
    	return -1;
    }

    
    /**
     * override this method if the free-text answer requires at most certain characters. 
     * @return  Default is -1 (unlimited)
     */    
    public int maxTextInputLength() {
    	return -1;
    }    
    
    /**
     * override this method if the input field is a free text.
     * @return  True if masking is needed. Default is False.
     */
    public boolean isPasswordInput() {

      return false;
    }

    /**
     * override this method if you want end-user to select a directory. A directory browser 
     * will be shown in GUI mode. Work for free-text input (isMultipleChoices is False). 
     * @return  True if input is a directory. Default is False.
     */
    public boolean isDirectoryInput() {

        return false;
    }

    /**
     * override this method if you want end-user to select a file. A file browser 
     * will be shown in GUI mode.  You can further restrict the file extension to be selected
     * by setting the fileExtension method. Work for free-text input (isMultipleChoices is False). 
     * @return  True if input is a file. Default is False.
     */
    public boolean isFileInput() {
        return false;
    }

    /**
     * override this method to restrict the file with extension specified.
     * This method is used if isFileInput is True.
     * @return The file extension to be selected by the file browser
     */
    public String fileExtension() {
        return "";
    }

    /**
     *  override this method to tell whether the response is a free-text or a multiple choice.
     * @return False if the question requires a free-text answer.  Default is True.
     */
    public boolean isMultipleChoices() {

        return true;
    }
    
    /**
     * override this method to specify how the multiple choices are sorted.
     * @return SORT_AS_IS, SORT_BY_KEY, SORT_BY_DESC
     */
    public String sortChoicesBy() {
      return SORT_AS_IS;
    }
    
    /**
     * sometimes the question is optional; no answer is needed. By default
     * the question requires an answer.
     * @return  False if this question does not require an answer. Default is True. 
     */
    public boolean needAnswer() { 

        return true;
    }

    /**
     * implement this method if you need some kind of validation on the answer
     * or run some code after the answer is present.
     * @return True if process can carry on, False if you want it from proceeding
     */
    public boolean leaveQuestion() {

        return true;
    }

    /**
     implement this method if you need to return a message after leaveMessage method is done.
     * @param   value from leaveQuestion
     * @return  Message shown after leaveQuestion
     */
    public Message leaveQuestionMsg(boolean flag) {

        return null;
    }

    /**
     * Override to tell which Question/Action to go back if it is allowed for a Question.
     * This method is for multiple choice question only. 
     * @return If defined you will want to see the "[b] Back" option. Default is null.
     */
    public String lastAction() {

      return null;
    }
    

    /**
     * override this method to tell which Question or Action class to be the next one.
     * If none specified, the program will exit.
     * @return Class alias or full qualified class name
     */
    public String nextAction() {
      
        return "";
    }

    /**
     * return true if you want to display the question as a new screen
     * (i.e. content of previous question/action will be flushed)
     * @return True to refresh the screen; False to append from the last screen
     */
    public boolean isNewScreen() {

        return true;
    }
    
    /**
     * override this method to set the GUI title. It does nothing in Console mode.
     * @return GUI window title
     */
    public String getTitle() {
      
        return null;
    }
    
    /**
     * Override this method to set linewarp on or off for this question. Work for GUI mode only.
     * @return False if you don't want lines are wrapped. Default is True.
     */
    public boolean lineWrap() {
      
      return true;
    }
}
