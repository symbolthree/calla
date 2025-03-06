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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Renderer.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 9 $
******************************************************************************/



package symbolthree.calla;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renderer is responsible for creating textual elements (as StringBuffer) for Question class.
 * Elements are: question, explanation, and choices
 */
public class Renderer implements Constants {
    static final Logger logger = LoggerFactory.getLogger(Renderer.class.getName());
    
    private StringBuffer questionBuf    = new StringBuffer();
    private StringBuffer choiceBuf      = new StringBuffer();
    private StringBuffer explanationBuf = new StringBuffer();
    private static final String sep     = System.getProperty("line.separator");
    private boolean      needRender;

    /**
     * Constructor
     * @param class subclassed of Question
     */
    public Renderer(Question question) {
        logger.debug("Rendering start...");
        questionBuf.setLength(0);
        explanationBuf.setLength(0);        
        choiceBuf.setLength(0);

        boolean valid = question.enterQuestion();

        logger.debug("enterQuestion=" + valid);

        if (valid) {
            needRender = true;

            // display question
            addLine(question.getQuestion(), questionBuf);

            addLine(question.getExplanation(), explanationBuf);

            logger.debug("isMultipleChoices=" + question.isMultipleChoices());
            // display choices
            if (question.isMultipleChoices()) {
                ArrayList<Choice> choices = question.choices();
                
                if (choices != null && choices.size() > 0) {
                  choices = Helper.sortChoices(choices, question.sortChoicesBy());
                  
                  if (Helper.getChoiceSize() != 0) {
                      int x = Helper.getChoicePage()*Helper.getChoiceSize()+1;
                      int y = (Helper.getChoicePage()+1)*Helper.getChoiceSize();
                      int z = choices.size();
                      if (y >= z) y = z;
                      
                      logger.debug("Choice " + x + " to " + y  + " of " + z);
                      
                      displayChoices(choices, x, y, question.fixMultipleChoiceHeight());
                      
                      addLine(choiceBuf);
                      
                      // show pagination if no. of choices is more than ChoiceSize setting
                      if (z > Helper.getChoiceSize()) {
                        addLine("Choice " + x + " to " + y  + " of " + z, choiceBuf);

                        int remainingChoices = z-y;
                        if (remainingChoices > Helper.getChoiceSize()) {
                        	remainingChoices = Helper.getChoiceSize();
                        }
                        
                        if (Helper.getChoicePage()==0){
                          addLine("[N] Next " + remainingChoices + " choices", choiceBuf);
                        }
                        if (Helper.getChoicePage()>0 && y < z) {
                            addLine("[P] Previous " + Helper.getChoiceSize() + " / [N] Next " + remainingChoices + " choices", choiceBuf);
                          } 
                        if (Helper.getChoicePage()>0 && y == z){
                            addLine("[P] Previous "+ Helper.getChoiceSize() + " choices", choiceBuf);
                        }                        
                      }
                  }
                } // if choice != null
                
                addLine(choiceBuf);
                
                if (question.lastAction() != null) {
                  add("[B]  Back  ", choiceBuf);
                }
               
                if (question.showExit()) {
                  add("[X]  Exit", choiceBuf);
                }
            }
        } else {
            needRender = false;
        }
    }

    /**
     * Question.enterQuestion value
     * @return True or False
     */
    public boolean needToRender() {
        return needRender;
    }

    /**
     * @return String content of Question as StringBuffer
     */
    public StringBuffer getQuestion() {
      return questionBuf;
    }

    /**
     * @return String content of Explanation as StringBuffer
     */
    public StringBuffer getExplanation() {
      return explanationBuf;
    }

    /**
     * @return String content of multiple choices as StringBuffer
     */
    public StringBuffer getChoices() {
      return choiceBuf;
    }

    /**
     * @return combination of Question, Explanation and Choices
     */
    public StringBuffer getStringBuffer() {
        return questionBuf.append(explanationBuf).append(sep).append(choiceBuf);
    }

    private void addLine(StringBuffer buffer) {
        buffer.append(sep);
    }

    private void add(String str, StringBuffer buffer) {
      if ((str != null) &&!str.trim().equals("")) {
        buffer.append(str);
      }
    }
    
    private void addLine(String str, StringBuffer buffer) {
        if ((str != null) &&!str.trim().equals("")) {
            buffer.append(str + sep);
        }
    }
    
    private void displayChoices(ArrayList<Choice> choices, int fromVal, int toVal, boolean fixHeight) {
      // the first choice is 1 (i is starting from zero)
      
      for (int i = 0; i < choices.size(); i++) {
        Choice c = choices.get(i);
        if (i >= fromVal-1 && i <= toVal-1) {
	        if (i < 9) {
	          // add extra spaces at the end for text alignment
	          addLine("[" + (i + 1) + "]   " + c.getChoiceDesc(), choiceBuf);
	        } else if (i < 99) {
	          addLine("[" + (i + 1) + "]  " + c.getChoiceDesc(), choiceBuf);
	        } else {
		      addLine("[" + (i + 1) + "] " + c.getChoiceDesc(), choiceBuf);	        	
	        }
        }
      }

      if (fixHeight) {
	      int rowRemain = Helper.getChoiceSize() - (toVal-fromVal+1);
	      logger.debug("rowRemain=" + rowRemain);
	      while (rowRemain > 0) {
	        addLine(choiceBuf);
	        rowRemain--;
	      }
      }

      //addLine(choiceBuf);
   }
}
