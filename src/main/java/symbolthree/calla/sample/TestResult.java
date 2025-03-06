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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import symbolthree.calla.Answer;
import symbolthree.calla.Choice;
import symbolthree.calla.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TestResult extends Question {
    private String explain    = null;
    private String result     = null;
    private int    totalScore = 0;
    static final Logger logger = LoggerFactory.getLogger(TestResult.class.getName());
    
    @Override
    public boolean enterQuestion() {
        totalScore = Integer.valueOf(Answer.getInstance().getB("TOTAL_SCORE"));
        XPathFactory xpfac = XPathFactory.instance();
        
        try {
            InputStream is       = this.getClass().getResourceAsStream("/symbolthree/calla/sample/Test.xml");
            SAXBuilder  builder  = new SAXBuilder();
            Document    doc      = builder.build(is);
            String      xpathStr = "//Result[number(@scoreFrom) <= " + totalScore + " and number(@scoreTo) > "
                                   + totalScore + "]";
            
            XPathExpression<Element> xp = xpfac.compile(xpathStr, Filters.element());

            result  = xp.evaluateFirst(doc).getValue();
            
            xp      = xpfac.compile("//Results/Suggestion", Filters.element());
            explain = xp.evaluateFirst(doc).getValue();
            
        } catch (JDOMException je) {
        	logger.error("Malformat XML file");
            System.exit(1);
        } catch (IOException ioe) {
            logger.error("Unable to read question XML file");
            System.exit(1);
        }

        return true;
    }

    
    public ArrayList<Choice> choices() {
    	ArrayList<Choice> choices = new ArrayList<Choice>();
    	choices.add(new Choice("RESTART", "Start again"));
        return choices;
    }
    
    @Override
    public boolean fixMultipleChoiceHeight() {
    	return false;
    }
    
    @Override
    public String getQuestion() {
        return "Your score is " + totalScore + "\n" + result;
    }

    @Override
    public String getExplanation() {
        return explain;
    }
    
    @Override
    public String nextAction() {
    	// reset test data
        Answer.getInstance().putB(TestQuestion.QUESTION_NO, "1");
        Answer.getInstance().putB(TestQuestion.TOTAL_SCORE, "0");
    	return "SurveyStart";
    }
    
}
