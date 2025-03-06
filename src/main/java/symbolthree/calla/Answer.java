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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This singleton class is responsible for storing answer for each question.
 * 
 * There're two types of answer:<br/> 
 * (1) responses from user input (e.g. multiple choice answer, free-text input)<br/>
 * (2) derived values from (1)<br/>
 * 
 * Answer(1) can be retrieved anywhere by using getA([class alias | name]).
 * Answer(2) can be retrieved anywhere by using getB([class alias | name]).
 * 
 * In this class, getA and putA methods are used to store and retrieve direct answer values for 
 * a Question class; while getB and putB are used for derived values. 
 * putA([key], null) and putB([key], null) will remove [key] from lookup.  
 * 
 * In recording mode, only the values stored in putA() method will be saved in the response.xml.
 */
public class Answer implements Constants {
    static final Logger logger = LoggerFactory.getLogger(Answer.class.getName());

    private static Answer allAnswers  = null;
    Properties            answerValue = null;

    /**
     * Constructor
     */
    protected Answer() {
        answerValue = new Properties();
    }

    /**
     * get an instance of this class by using Answer.getInstance().get....
     * @return this class
     */
    public static Answer getInstance() {
        if (allAnswers == null) {
            allAnswers = new Answer();
        }

        return allAnswers;
    }

    /**
     * Retrieve answer from questionAlias or full-qualified class name
     * @param questionAlias
     * @return answer value
     */
    public String getA(String questionAlias) {
        String fullClassName = CallaProp.instance().get(questionAlias);
        if (fullClassName==null || fullClassName.equals("")) {
          fullClassName = questionAlias;
        }

        return answerValue.getProperty(fullClassName);
    }

    /**
     * Retrieve answer from a Question object
     * @param question
     * @return answer value
     */
    public String getA(Question question) {
        return answerValue.getProperty(question.getClass().getName());
    }

    /**
     * Store Question response using question object
     * @param question Question object
     * @param aValue   choice key or free-text. Null to remove this entry.
     */
    public void putA(Question question, String aValue) {
        if (aValue==null) {
          answerValue.remove(question.getClass().getName());
        } else {
          answerValue.setProperty(question.getClass().getName(), aValue);
        }
    }

    /**
     * Store Question response using class alias or full qualified class name
     * @param questionAlias Question alis or full qualified class name
     * @param aValue   choice key or free-text. Null to remove this entry.
     */
    public void putA(String questionAlias, String aValue) {
        String fullClassName = CallaProp.instance().get(questionAlias);
        if (fullClassName==null) {
          fullClassName = questionAlias;
        }
        if (aValue==null) {
          answerValue.remove(fullClassName);
        } else {
          answerValue.setProperty(fullClassName, aValue);
        }
    }

    /**
     * retrieve derived values
     * @param key 
     * @return null if this key is not defined
     */
    public String getB(String key) {
      return answerValue.getProperty(key);
    }

    /**
     * retrieve derived values as Int
     * @param key 
     * @return null if this key is not defined
     */
    public int getBasInt(String key) {
      String ans = answerValue.getProperty(key);
      int rtnVal = Integer.MAX_VALUE;
      try {
        rtnVal = Integer.parseInt(ans);
      } catch (Exception e) {
    	  logger.warn("unable to convert to integer - " + ans);
      }
      return rtnVal;
        
    }

    /**
     * 
     * @param key
     * @param aValue
     */
    public void putB(String key, String aValue) {
        if (aValue==null) {
          answerValue.remove(key);
        } else {
          answerValue.setProperty(key, aValue);
        }
    }

    /**
     * 
     * @param key
     * @param aValue
     */
    public void putB(String key, int aValue) {
      answerValue.setProperty(key, Integer.toString(aValue));
    }
    
    /**
     * remove all answer entries. Used for rerun the application from start 
     */
    public void clearAll() {
      answerValue.clear();
      logger.debug("All answer purged.");
    }
    
}
