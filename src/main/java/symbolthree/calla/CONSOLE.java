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

import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CONSOLE mode (text-based interactive) of CALLA.
 *
 */
public class CONSOLE implements Constants {

	static final Logger logger = LoggerFactory.getLogger(CONSOLE.class.getName());
    private String     answerStr = "";
    private Question   currentQuestion;
    private Response   responses = new Response();

    /**
     * Constructor
     */
    public CONSOLE() {
    	logger.debug("In Console constructor start");
    	/*  replaced by log4j
        System.setProperty(Helper.FLOWER_LOG_LEVEL, FlowerProp.instance().get(Helper.FLOWER_LOG_LEVEL));
        System.setProperty(Helper.FLOWER_LOG_OUTPUT, FlowerProp.instance().get(Helper.FLOWER_LOG_OUTPUT));
       */
    }

    /**
     * Main class of Console - start this method directly if you want
     */
    public static void main(String[] args) {
      main();
    }

    protected static void main() {
   	 logger.debug("In Console.main()...");
      CONSOLE console = new CONSOLE();
      console.start();
    }

    private void start() {
    	logger.debug("In Console.start()...");
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
            logger.error("Render Error", e);
        }
    }

    private void response() {

        answerStr = "";

        try {
            if (currentQuestion.isPasswordInput()) {
                char password[] = PasswordField.getPassword(System.in, "Enter password ==");

                answerStr = String.valueOf(password);

            } else {

                System.out.print(">> ");

                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader    br  = new BufferedReader(isr);

                answerStr = br.readLine();
            }
        } catch (IOException e) {
            showErrDialog(e.getMessage());
            logger.error("Response Error", e);
        }

        if (currentQuestion.needAnswer()) {

            if (answerStr.trim().equals("")) {
                showErrDialog("Please enter a value.");
                response();
            }
        }

        try {
            if (currentQuestion.isMultipleChoices()) {

                if (answerStr.equalsIgnoreCase("X") && currentQuestion.showExit()) {

                  if (Helper.getBoolean(CALLA_EXIT_CONFIRM, true)) {
                    int val = showConfirmDialog("Do you want to quit ?");

                    if (val==JOptionPane.OK_OPTION) {
                        if (isRecordingMode()) {
                          responses.addResponse(currentQuestion,"X");
                        }
                        exitProgram(0);
                    } else {
                	  response();
                    }

                  } else {
                	exitProgram(0);
                  }
                }

                if (answerStr.equalsIgnoreCase("B") && currentQuestion.lastAction() != null) {

                  if (isRecordingMode()) {
                    responses.addResponse(currentQuestion,"B");
                  }
                  String clazz = currentQuestion.lastAction();
                  render(clazz);
                  return;
                }

                // handle next/previous choice page
                if (answerStr.equalsIgnoreCase("N") || answerStr.equalsIgnoreCase("P")) {

                	boolean goNextPage = false;

                    int y = (Helper.getChoicePage()+1)*Helper.getChoiceSize();
                    int z = currentQuestion.choices().size();
                    if (y >= z) y = z;

                    if (Helper.getChoicePage()==0) {
                	  if (answerStr.equalsIgnoreCase("N")) {
                		  Helper.addChoicePage(1);
	                	  goNextPage = true;
                	  }
                    }

                    if (!goNextPage && Helper.getChoicePage()>0 && y < z) {
                	  if (answerStr.equalsIgnoreCase("N")) {
                		  Helper.addChoicePage(1);
                		  goNextPage = true;
                	  }
                      if (answerStr.equalsIgnoreCase("P")) {
                    	  Helper.addChoicePage(-1);
                    	  goNextPage = true;
                      }
                    }

                    if (!goNextPage && Helper.getChoicePage()>0 && y == z){
                      if (answerStr.equalsIgnoreCase("P")) {
                    	  Helper.addChoicePage(-1);
	                	  goNextPage = true;
                      }
            	    }

            	    if (goNextPage) {
                      String clazz = currentQuestion.getClass().getName();
                      render(clazz);
                      return;
            	    }
                }

                if (!Helper.isInteger(answerStr)) {
                    showErrDialog("Please enter a valid number.");

                    response();
                }

                // check whether the number is a valid choice for a paginated choice page

                if (Helper.isInteger(answerStr) && Helper.getChoiceSize() != 0) {
                	logger.debug("check multiple choice ans range: Choice Page " + Helper.getChoicePage() + ", choice size " + Helper.getChoiceSize());

                	if (Helper.getInt(answerStr,-1) > (Helper.getChoicePage()+1)*Helper.getChoiceSize() ||
                		Helper.getInt(answerStr,-1) < Helper.getChoicePage()*Helper.getChoiceSize()) {
                        showErrDialog("Please enter a valid number.");
                        response();;
                	}
                }

                int idx = Helper.getInt(answerStr,-1);

                if ((idx > currentQuestion.choices().size()) || (idx <= 0)) {
                    showErrDialog("Please enter a valid number.");
                    response();
                }

                // actual index is one less the entered number
                idx--;

                ArrayList<Choice> sortedChoices = Helper.sortChoices(
                                                    currentQuestion.choices(),
                                                    currentQuestion.sortChoicesBy());
                String answer = sortedChoices.get(idx).getChoiceKey();

                Answer.getInstance().putA(currentQuestion, answer);
                logger.debug("Answer=" + answer);

            } else {

                // not a multiple choices...
            	// check answer length
            	int minLen = currentQuestion.minTextInputLength();
            	int maxLen = currentQuestion.maxTextInputLength();

            	if (minLen > 0 && answerStr.trim().length() < minLen) {
                    showErrDialog("Please enter " + minLen + " or more characters");
                    response();
            	}
            	if (maxLen > 0 && answerStr.trim().length() > maxLen) {
                    showErrDialog("Please enter " + maxLen + " or less characters");
                    response();
            	}

                Answer.getInstance().putA(currentQuestion, answerStr);

                if (currentQuestion.isPasswordInput()) {
                	logger.debug("Answer=******");
                } else {
                	logger.debug("Answer=" + answerStr);
                }
            }

         	// reset pagination when the the number is valid
         	Helper.resetChoicePage();

            boolean valid = currentQuestion.leaveQuestion();

            if (valid) {

              showMsgDialog(currentQuestion.leaveQuestionMsg(true));

              // save responses
                if (isRecordingMode()) {
                  responses.addResponse(currentQuestion, Answer.getInstance().getA(currentQuestion));
                }

                // progress bar
                if (currentQuestion.showProgress()) {
                    showMsgDialog(new Message(Message.INFO, "Please wait..."));
                }

                String clazz = currentQuestion.nextAction();

                logger.debug("Next Action = " + clazz);

                render(clazz);

            } else {

                showMsgDialog(currentQuestion.leaveQuestionMsg(false));

                response();
            }

        } catch (Exception e) {
            showErrDialog(e.getClass().getName() + "\n" + e.getMessage());
            logger.error("Response Error", e);
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

            if (currentQuestion.isFileInput()) {

              ConsoleFileBrowser cfg = new ConsoleFileBrowser();
              cfg.setExtension(currentQuestion.fileExtension());
              cfg.setShowExit(!currentQuestion.needAnswer());

              String file = cfg.selectFile();

              Answer.getInstance().putA(currentQuestion, file);

              boolean valid = currentQuestion.leaveQuestion();

              if (valid) {

                showMsgDialog(currentQuestion.leaveQuestionMsg(true));

                // save responses
                  if (isRecordingMode()) {
                    responses.addResponse(currentQuestion, Answer.getInstance().getA(currentQuestion));
                  }

                  String str = currentQuestion.nextAction();

                  logger.debug("Next Action = " + clazz);

                  render(str);
              }


            } else if (currentQuestion.isDirectoryInput()) {

              ConsoleDirectoryBrowser cfg = new ConsoleDirectoryBrowser();
              cfg.setShowExit(!currentQuestion.needAnswer());

              String selectedDir = cfg.selectDirectory();

              Answer.getInstance().putA(currentQuestion, selectedDir);

              boolean valid = currentQuestion.leaveQuestion();

              if (valid) {

                showMsgDialog(currentQuestion.leaveQuestionMsg(true));

                // save responses
                  if (isRecordingMode()) {
                    responses.addResponse(currentQuestion, Answer.getInstance().getA(currentQuestion));
                  }

                  String str = currentQuestion.nextAction();

                  logger.debug("Next Action = " + clazz);

                  render(str);
              }

            } else {
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


    private int showConfirmDialog(String str) {
        System.out.print("[Confirm] " + str + "(Y or N) ");

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader    br  = new BufferedReader(isr);

        try {
          answerStr = br.readLine();
          if (answerStr.equalsIgnoreCase("Y")) {
        	  return JOptionPane.OK_OPTION;
          } else {
        	  return JOptionPane.CANCEL_OPTION;
          }
        } catch (IOException ioe) {
      	  return JOptionPane.CANCEL_OPTION;
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

        if (isRecordingMode()) {
          buf.append("\n" + "(Recording Mode)");
        }

        System.out.println(buf.toString());
    }

    private boolean isRecordingMode() {
      return (Helper.getBoolean(CallaProp.instance().get("calla.record"), false));
    }

    private void exitProgram(int exitCode) {
       if (isRecordingMode()) responses.writeResponseFile();

       System.exit(exitCode);
    }
}
