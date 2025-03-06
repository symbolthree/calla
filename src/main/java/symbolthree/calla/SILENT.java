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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SILENT mode (playback mode) of CALLA.
 * In the flower.properties, flower.responses value must be specified.
 *
 * @author  $Author: Christopher Ho $
 * @version $Revision: 5 $
 */
public class SILENT implements Constants {
  static final Logger logger = LoggerFactory.getLogger(SILENT.class.getName());

  private String     answerStr = "";
  private Question   currentQuestion;
  private Response   responses       = new Response();
  private int        questionCounter = 0;

  /**
   * Constructor
   */
  public SILENT() {
  	/*  replaced by log4j
        System.setProperty(Helper.FLOWER_LOG_LEVEL, FlowerProp.instance().get(Helper.FLOWER_LOG_LEVEL));
        System.setProperty(Helper.FLOWER_LOG_OUTPUT, FlowerProp.instance().get(Helper.FLOWER_LOG_OUTPUT));
    */
  }

  /**
   * Main class of SILENT - start this method directly if you want
   */

  public static void main(String[] args) {
    main();
  }

  protected static void main() {
    SILENT silent = new SILENT();
    silent.start();
  }

  private void start() {
      logger.debug("Start in Silent mode");

      responses.readResponseFile();

      printTitle();

      String firstQuestion = Helper.getString(pStr("start"), CLASS_DEFAULT_START);
      Object clazz = Helper.getRenderObject(firstQuestion);
      if (clazz == null) {
        firstQuestion = CLASS_DEFAULT_START;
      }

      try {
          render(firstQuestion);
      } catch (Exception e) {
          showErrDialog(e.getClass().getName() + "\n" + e.getMessage());
      }
  }

  private void response() {
      String className = responses.getClassName(questionCounter);

      logger.debug("question counter : " + questionCounter);
      logger.debug("class name: " + className);

      if (className.equals(currentQuestion.getClass().getCanonicalName())) {
        answerStr = responses.getAnswer(questionCounter);
        questionCounter++;
      } else {
        System.out.println("Question and Response are mismatch.");
        exitProgram(1);
      }

      System.out.println("[Answer] " + answerStr);

      if (currentQuestion.needAnswer()) {
          if (answerStr == null || answerStr.trim().equals("")) {
              showErrDialog("Please enter a value");
              exitProgram(1);
          }
      }

      try {
          if (currentQuestion.isMultipleChoices()) {

              Answer.getInstance().putA(currentQuestion, answerStr);
              logger.debug("Answer=" + answerStr);

              if (answerStr.equalsIgnoreCase("X") && currentQuestion.showExit()) {
                exitProgram(0);
              }

              if (answerStr.equalsIgnoreCase("B") && currentQuestion.lastAction() != null) {
                String clazz = currentQuestion.lastAction();
                render(clazz);
              }

          } else {

              // not a multiple choices...
              Answer.getInstance().putA(currentQuestion, answerStr);

              if (currentQuestion.isPasswordInput()) {
            	  logger.debug("Answer=******");
              } else {
            	  logger.debug("Answer=" + answerStr);
              }
          }

          boolean valid = currentQuestion.leaveQuestion();

          if (valid) {
              showMsgDialog(currentQuestion.leaveQuestionMsg(true));
              String clazz = currentQuestion.nextAction();

              render(clazz);
          } else {
              showMsgDialog(currentQuestion.leaveQuestionMsg(false));
              response();
          }
      } catch (Exception e) {
          showErrDialog(e.getClass().getName() + "\n" + e.getMessage());
          exitProgram(1);
      }
  }

  private void render(String classAlias) {

      Object clazz = Helper.getRenderObject(classAlias);

      if (clazz == null) {
        exitProgram(1);
      }

      if (clazz instanceof Question) {
    	  logger.debug("render class is a Question");
          currentQuestion = (Question) clazz;
          logger.debug("Class instantiation done.");

          Renderer renderer = new Renderer(currentQuestion);

          if (renderer.needToRender()) {
              StringBuffer buffer = renderer.getStringBuffer();

              System.out.println("\n" + buffer.toString());
              logger.debug("Class rendered.");
              response();
          } else {

              // no need to render
        	  logger.debug("Class skipped.");

              String str = currentQuestion.nextAction();

              render(str);
          }
      }

      if (clazz instanceof Action) {
    	  logger.debug("render class is an Action");

          Action action = (Action) clazz;

          // showSystemOut(action.showSystemOutput());
          Executor.execute(action);

          if (action.actionMessage() != null) {
              showMsgDialog(action.actionMessage());
          }

          String str = action.nextAction();

          render(str);
      }
  }

  private String pStr(String key) {
      return CallaProp.instance().get("calla." + key);
  }

  private void showMsgDialog(Message msg) {
      if (msg != null) {
        System.out.println("[" + msg.getMessageTitle() + "] " + msg.getMessageContent());
      }
  }

  private void showErrDialog(String str) {
      showMsgDialog(new Message(Message.ERROR, str));
  }

  private void printTitle() {
      String       title = pStr("title");
      StringBuffer buf   = new StringBuffer();

      for (int i = 0; i < title.length() + 4; i++) {
          buf.append("-");
      }

      buf.append("\n  " + title + "  \n");

      for (int i = 0; i < title.length() + 4; i++) {
          buf.append("-");
      }

      System.out.println(buf.toString());
  }

  private void exitProgram(int exitCode) {
    System.exit(exitCode);
 }
}
