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

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import symbolthree.calla.Answer;
import symbolthree.calla.Choice;
import symbolthree.calla.Helper;
import symbolthree.calla.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TestQuestion extends Question {
    public  static String QUESTION_NO       = "QUESTION_NO";
    public  static String TOTAL_SCORE       = "TOTAL_SCORE";
    private String        qContent          = null;
    private int           qNo               = 0;
    private int           totalNoOfQuestion = 0;
    private int           totalScore        = 0;
    static final Logger logger = LoggerFactory.getLogger(TestQuestion.class.getName());
    
    
    @Override
    public boolean enterQuestion() {
        qNo        = Helper.getInt(Answer.getInstance().getB(QUESTION_NO), 1);
        totalScore = Helper.getInt(Answer.getInstance().getB(TOTAL_SCORE), 0);
    	
        XPathFactory xpfac = XPathFactory.instance();
        
        try {
            InputStream is      = this.getClass().getResourceAsStream("/symbolthree/calla/sample/Test.xml");
            SAXBuilder  builder = new SAXBuilder();
            Document    doc     = builder.build(is);
            XPathExpression<Element> xp  = xpfac.compile("//Question", Filters.element());
            totalNoOfQuestion =  xp.evaluate(doc).size();
            
            xp                = xpfac.compile("//Question[@no='" + qNo + "']", Filters.element());
            qContent          = xp.evaluateFirst(doc).getValue();
            
        } catch (JDOMException je) {
        	logger.error("Malformat XML file", je);
            System.exit(1);
        } catch (IOException ioe) {
        	logger.error("Unable to read question XML file", ioe);
            System.exit(1);
        }

        return true;
    }

    @Override
    public String getQuestion() {
        return "Question " + qNo + " of " + totalNoOfQuestion + "\n";
    }

    @Override
    public String getExplanation() {
        return qContent;
    }

    @Override
    public ArrayList<Choice> choices() {
        ArrayList<Choice> al = new ArrayList<Choice>();

        al.add(new Choice("1", "Rarely"));
        al.add(new Choice("2", "Occasionally"));
        al.add(new Choice("3", "Frequently"));
        al.add(new Choice("4", "Often"));
        al.add(new Choice("5", "Always"));

        return al;
    }

    @Override
    public boolean leaveQuestion() {
        totalScore = Integer.valueOf(Answer.getInstance().getA(this)) + totalScore;
        logger.info("totalScore = " + totalScore);
        Answer.getInstance().putB(TOTAL_SCORE, String.valueOf(totalScore));
        Answer.getInstance().putB(QUESTION_NO, String.valueOf(qNo));

        return true;
    }

    @Override
    public String nextAction() {
        if (qNo >= totalNoOfQuestion) {
            return "SurveyResult";
        } else {
            Answer.getInstance().putB(QUESTION_NO, String.valueOf(qNo + 1));

            return this.getClass().getCanonicalName();
        }
    }

    @Override
    public String getTitle() {
        return "Internet Addiction Test | Question " + qNo + " of " + totalNoOfQuestion;
    }
    
}
