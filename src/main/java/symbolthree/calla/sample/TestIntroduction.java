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

import symbolthree.calla.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestIntroduction extends Question {
    private String about       = null;
    private String instruction = null;
    private static final Logger logger = LoggerFactory.getLogger(TestIntroduction.class.getName());
    
    @Override
    public boolean enterQuestion() {
    	XPathFactory xpfac = XPathFactory.instance();

        try {
            InputStream is      = this.getClass().getResourceAsStream("/symbolthree/calla/sample/Test.xml");
            SAXBuilder  builder = new SAXBuilder();
            Document    doc     = builder.build(is);
            
            XPathExpression<Element> xp  = xpfac.compile("//About", Filters.element());
            about       = xp.evaluateFirst(doc).getValue();
            
            xp          = xpfac.compile("//Instruction", Filters.element());
            instruction = xp.evaluateFirst(doc).getValue();
            
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
        return about;
    }

    @Override
    public String getExplanation() {
        return instruction;
    }

    @Override
    public String getTitle() {
        return "Inernet Addiction Test";
    }

    @Override
    public boolean isMultipleChoices() {
        return false;
    }

    @Override
    public boolean needAnswer() {
        return false;
    }

    @Override
    public String nextAction() {
        return "SurveyQuestion";
    }
}
