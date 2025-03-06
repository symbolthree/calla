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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Response.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 4 $
******************************************************************************/

package symbolthree.calla;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response class is responsible for reading and creating response.xml for silent operation.
 * The structure of response.xml is:
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <RESPONSES CREATION_DATE="[timestamp]">
 *   <RESPONSE ID="[sequence no]" CLASS="[Question full-qualified class name]">[ChoiceKey | free-text answer]</RESPONSE>
 *   <RESPONSE ID="0" CLASS="symplik.oracle.fndload.question.OperationModeQuestion">DOWNLOAD</RESPONSE>
 *   <RESPONSE ID="1" CLASS="symplik.oracle.fndload.question.DownloadObjectQuestion">FND_NEW_MESSAGES</RESPONSE>
 * </RESPONSES>
 * }
 * </pre>  
 *   
 * @author  $Author: Christopher Ho $
 * @version $Revision: 4 $
 */
public class Response implements Constants {
    public static final String RCS_ID =
      "$Header: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Response.java 4     1/24/17 9:55a Christopher Ho $";
    static final Logger logger = LoggerFactory.getLogger(Response.class.getName());
    
    private Vector<String> questionV = new Vector<String>();
    private Vector<String> answerV   = new Vector<String>();
    
    public Response() {
    }
    
    /**
     * add a response node to the response file
     * @param clazz
     * @param value
     */
    public void addResponse(Question clazz, String value) {
      questionV.add(clazz.getClass().getCanonicalName());
      answerV.add(value);
    }
    
    /**
     * write the response xml file
     */
    public void writeResponseFile() {
      String str = Helper.getString(CallaProp.instance().get(CALLA_RESPONSES), CALLA_RESPONSES_FILE);
      File file = new File(str);
      logger.debug("write response file " + file.getAbsolutePath());
      
      Element          root       = new Element("RESPONSES");
      SimpleDateFormat sdf        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String           timestamp  = sdf.format(Calendar.getInstance().getTime());
      
      root.setAttribute("CREATION_DATE", timestamp);
      
      for (int i=0;i<questionV.size();i++) {
        Element ele = new Element("RESPONSE");
        ele.setAttribute("ID", String.valueOf(i));
        ele.setAttribute("CLASS", questionV.get(i));
        ele.setText(answerV.get(i));
        root.addContent(ele);
      }

      Document document = new Document(root);
     
      try {
        
        XMLOutputter outputter = new XMLOutputter();

        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(document, new FileOutputStream(file));
        
      } catch (IOException ioe) {
    	logger.error("Unable to wrie response file", ioe);
      }
    }
    
    public String getClassName(int i) {
      return questionV.get(i);
    }
    
    public String getAnswer(int i) {
      return answerV.get(i);
    }
    
    /**
     * 
     */
    public void readResponseFile() {
      String f = Helper.getString(CallaProp.instance().get(CALLA_RESPONSES), CALLA_RESPONSES_FILE);
      File file = new File(f);
      if (!file.exists()) {
    	logger.debug("Unable to find responses file " + file.getAbsolutePath());
        System.exit(1);
      } else {
    	  logger.debug("Using response file " + file.getAbsolutePath());
        
        try {
          SAXBuilder builder    = new SAXBuilder();          
          Document   responses  = builder.build(file);
          
          XPathFactory xpfac = XPathFactory.instance();
          XPathExpression<Element> xp = xpfac.compile("/RESPONSES/RESPONSE", Filters.element());
          
          List<Element> list = (List<Element>)xp.evaluate(responses);
          
          String[] questionArry = new String[list.size()];
          String[] answerArry   = new String[list.size()];

          Element   ele;
          Attribute attr;
          
          for (int i=0;i<list.size();i++) {
            XPathExpression<Attribute> xp1 = xpfac.compile("/RESPONSES/RESPONSE[@ID=" + i + "]/@CLASS", Filters.attribute());        	  
            attr = xp1.evaluateFirst(responses);
            questionArry[i] = attr.getValue();
            
            XPathExpression<Element> xp2 = xpfac.compile("/RESPONSES/RESPONSE[@ID=" + i + "]", Filters.element());
            ele = xp2.evaluateFirst(responses);
            answerArry[i] = ele.getText();
          }
          
          questionV.addAll(Arrays.asList(questionArry));
          answerV.addAll(Arrays.asList(answerArry));
          
        } catch (JDOMException je) {
        	logger.error("Malformat responses XML file");
          System.exit(1);
        } catch (IOException ioe) {
        	logger.error("Unable to read responses XML file");          
          System.exit(1);          
        }
      }
    }
}


