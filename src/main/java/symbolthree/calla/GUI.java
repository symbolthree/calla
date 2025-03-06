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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/GUI.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 10:40a $
 * $Revision: 16 $
******************************************************************************/


package symbolthree.calla;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Main class to start the GUI version of CALLA
 */
public class GUI extends JFrame implements ActionListener, KeyListener, Constants, Runnable {
    private static final long serialVersionUID = 1764451497516196253L;
    
    private static final int  ICONSIZE           = 16;
    private CallaJTextPane      textPane         = new CallaJTextPane();
    private StyledDocument      doc              = textPane.getStyledDocument();    

    private JButton             submitBTN        = new JButton();
    private JButton             selectBTN        = new JButton();    
    private JButton             exitBTN          = new JButton();
    private JButton             helpBTN          = new JButton();    
    
    private JTextField          responseTF       = new JTextField();
    private JPasswordField      passwordTF       = new JPasswordField();
    
    private JLabel              responseLBL      = new JLabel();
    
    private JFileChooser        fc;
    private JFileChooser        dc;
    
    private JScrollPane         scrollPane;    
    private Question            currentQuestion;
    private Style               questionStyle;
    private Style               explanationStyle;
    private Style               choiceStyle;
    private static final String QUESTION_STYLE    = "question";
    private static final String EXPLANATION_STYLE = "explanation";
    private static final String CHOICE_STYLE      = "choice";
    
    private Response             responses        = new Response();  
    
    JPanel       statusPanel = new JPanel();
    JProgressBar progressBar = new JProgressBar(0,100);
    
    static final Logger logger = LoggerFactory.getLogger(GUI.class.getName());
    
    /**
     * Constructor
     */
    public GUI() {
    	
    	showSplashScreen();
    	
        setProgramLookAndFeel();
        setStyles();
        setButtonIcon();
        
        GridBagConstraints c   = new GridBagConstraints();
        
        // JScrollPane
        createTextPane();
        textPane.addKeyListener(this);
        scrollPane = new JScrollPane(textPane);

        scrollPane.setPreferredSize(new Dimension(Helper.getInt(pStr("panel.width"), 500),
                Helper.getInt(pStr("panel.height"), 400)));
        
        scrollPane.setMaximumSize(new Dimension(Helper.getInt(pStr("panel.width"), 500),
                Helper.getInt(pStr("panel.height"), 400)));
        
        scrollPane.getViewport().setBackground(Helper.getColor(pStr("panel.background.color"), Color.WHITE));
        
        // responsePanel
        JPanel responsePanel = new JPanel(new GridBagLayout());

        createInputFields();
        
        c.gridx  = 0;
        c.gridy  = 0;
        c.anchor = GridBagConstraints.WEST;
        responsePanel.add(responseLBL, c);
        c.gridx  = 1;
        c.anchor = GridBagConstraints.WEST;
        responsePanel.add(responseTF, c);
        responsePanel.add(passwordTF, c);

        // buttonPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        c.gridy  = 0;
        c.gridx  = 0;
        c.anchor = GridBagConstraints.EAST;
        selectBTN.addActionListener(this);
        selectBTN.setEnabled(false);
        buttonPanel.add(selectBTN, c);
        c.gridx = 1;
        submitBTN.setMnemonic(KeyEvent.VK_ENTER);
        submitBTN.addActionListener(this);
        buttonPanel.add(submitBTN, c);
        c.gridx = 2;
        helpBTN.setMnemonic(KeyEvent.VK_H);
        helpBTN.addActionListener(this);
        buttonPanel.add(helpBTN, c);
        c.gridx = 3;
        exitBTN.setMnemonic(KeyEvent.VK_X);
        exitBTN.addActionListener(this);
        buttonPanel.add(exitBTN, c);

        // overall layout
        GridBagLayout      gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gbl);
        gbc.gridy     = 0;
        gbc.gridx     = 0;
        gbc.gridwidth = 2;
        this.add(scrollPane, gbc);
        gbc.gridy     = 1;
        gbc.gridx     = 0;
        gbc.gridwidth = 1;
        this.add(responsePanel, gbc);
        gbc.gridy     = 1;
        gbc.gridx     = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(buttonPanel, gbc);
        String title = Helper.getString(CallaProp.instance().get("calla.title"), "CALLA");
        if (isRecordingMode()) {
          title = title + " (recording)";
        }
        this.setTitle(title);

        BufferedImage icon = null;

        try {
            icon = ImageIO.read(this.getClass().getResource(pStr("icon")));
            this.setIconImage(icon);            
        } catch (Exception e) {}
        
        gbc.gridy=2;
        gbc.gridx=0;
        gbc.gridwidth=2;
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(Helper.getInt(pStr("panel.width"), 500), 5));
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        this.add(progressBar, gbc);
        
        gbl.layoutContainer(this);
        this.pack();

        Dimension screenSize   = Toolkit.getDefaultToolkit().getScreenSize();
        int       windowWidth  = this.getWidth();
        int       windowHeight = this.getHeight();

        this.setLocation((int) (screenSize.getWidth() - windowWidth) / 2,
                         (int) (screenSize.getHeight() - windowHeight) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        progressBar.setVisible(false);
    }

    /**
     * Main class of GUI - start this method directly if you want
     */
    public static void main(String[] args) {
      main();
    }
    
    protected static void main() {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(new Thread(gui));
    }
    
    public void run() {
        String firstQuestion = Helper.getString(CallaProp.instance().get("calla.start"), CLASS_DEFAULT_START);
        logger.debug("Start Question = " + firstQuestion);
        
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

    public void actionPerformed(ActionEvent e) {
    	logger.debug("action performed = " + ((JButton)e.getSource()).getName());
        if (e.getSource() == exitBTN) {
        	askForExitProgram();
        }

        if (e.getSource() == submitBTN) {
            inProgress(true);
            response();
            inProgress(false);
        }

        if (e.getSource() == selectBTN) {
            if (currentQuestion.isDirectoryInput()) {
                showDirectoryDialog();
            }

            if (currentQuestion.isFileInput()) {
                showFileDialog();
            }
        }
        
        if (e.getSource() == helpBTN) {
        	launchHelp();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            e.consume();
            inProgress(true);
            response();
            inProgress(false);
        }
    }

    private void response() {
        String ans = null;
        
        inProgress(true);
        
        if (currentQuestion.isPasswordInput()) {
            ans = new String(passwordTF.getPassword());
            passwordTF.setText("");
        } else {
            ans = responseTF.getText().trim();
            responseTF.setText("");
        }

        if (currentQuestion.needAnswer()) {
            if (ans.equals("")) {
                showErrDialog("Please enter a value.");
                return;
            }
        }

        try {
            if (currentQuestion.isMultipleChoices()) {
                // handle Exit            	
                if (ans.equalsIgnoreCase("X") && currentQuestion.showExit()) {
                	askForExitProgram();
                }

                // handle Back key
                if (ans.equalsIgnoreCase("B") && currentQuestion.lastAction() != null) {

                  System.setProperty("calla.choice.page","0");
                    
                  if (isRecordingMode()) {
                    responses.addResponse(currentQuestion,"B");
                  }
                  
                  String clazz = currentQuestion.lastAction();
                  render(clazz);
                  return;
                }
                
                // handle next/previous keys for paginated choices
                if (ans.equalsIgnoreCase("N") || ans.equalsIgnoreCase("P")) {

                	boolean goNextPage = false;
                	
                	if (currentQuestion.choices().size() > Helper.getChoiceSize()) {

                        int y = (Helper.getChoicePage()+1)*Helper.getChoiceSize();
                        int z = currentQuestion.choices().size();
                        if (y >= z) y = z;
                        
	                    if (Helper.getChoicePage()==0) {
	                	  if (ans.equalsIgnoreCase("N")) {
	                		  Helper.addChoicePage(1);
		                	  goNextPage = true;
	                	  }
	                    }
	                    
	                    if (!goNextPage && Helper.getChoicePage()>0 && y < z) {
	                	  if (ans.equalsIgnoreCase("N")) {
	                		  Helper.addChoicePage(1);
	                		  goNextPage = true;
	                	  }
	                      if (ans.equalsIgnoreCase("P")) {
	                    	  Helper.addChoicePage(-1);
	                    	  goNextPage = true;
	                      }
	                    }
	                      
	                    if (!goNextPage && Helper.getChoicePage()>0 && y == z){
	                      if (ans.equalsIgnoreCase("P")) {
	                    	  Helper.addChoicePage(-1);
		                	  goNextPage = true;	                    	  
	                      }
                	    }
                	  
	                    logger.debug("goNextPage=" + goNextPage);
	                    
                	    if (goNextPage) {
                          String clazz = currentQuestion.getClass().getName();
                          render(clazz);
                          return;
                	    }
                	}
                }
                
                if (!Helper.isInteger(ans)) {
                    showErrDialog("Please enter a valid number.");
                    return;
                }

                // check whether the number is a valid choice for a paginated choice page
                
                if (Helper.isInteger(ans) && Helper.getChoiceSize() != 0) { 
                	logger.debug("check multiple choice ans range: Choice Page " + Helper.getChoicePage() + ", choice size " + Helper.getChoiceSize());                	
                	if (Helper.getInt(ans,-1) > (Helper.getChoicePage()+1)*Helper.getChoiceSize() ||
                		Helper.getInt(ans,-1) < Helper.getChoicePage()*Helper.getChoiceSize()) {
                        showErrDialog("Please enter a valid number.");
                        return;
                	} else {
                     	// reset pagination when the the number is valid
                     	Helper.resetChoicePage();
                	}
                }
                
                int idx = Helper.getInt(ans, -1);

                if (idx > currentQuestion.choices().size() || idx <= 0) {
                    showErrDialog("Please enter a valid number.");
                    return;
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
            	
            	if (minLen > 0 && ans.trim().length() < minLen) {
                    showErrDialog("Please enter " + minLen + " or more characters");
                    return;
            	}
            	if (maxLen > 0 && ans.trim().length() > maxLen) {
                    showErrDialog("Please enter " + maxLen + " or less characters");
                    return;
            	}
            	
                Answer.getInstance().putA(currentQuestion, ans.trim());

                if (currentQuestion.isPasswordInput()) {
                    logger.debug("Answer=******");
                } else {
                    logger.debug("Answer=" + ans);
                }
            }
            
            boolean valid = currentQuestion.leaveQuestion();

            if (valid) {
              
              showMsgDialog(currentQuestion.leaveQuestionMsg(true));
              
              // save responses
                if (isRecordingMode()) {
                    responses.addResponse(currentQuestion,
                                          Answer.getInstance().getA(currentQuestion));
                }              
              
                String clazz = currentQuestion.nextAction();

                render(clazz);

            } else {

                showMsgDialog(currentQuestion.leaveQuestionMsg(false));
                
            }
        } catch (Exception e) {
            showErrDialog(e.getClass().getName() + "\n" + e.getMessage());
            logger.error("response error", e);
        }
    }

    private void render(String classAlias) {
        
        Object clazz = Helper.getRenderObject(classAlias);
        if (clazz == null) {
          logger.error("Unable to find object alias of " + classAlias);
          exitProgram(1);
        }

        if (clazz instanceof Question) {
            logger.debug("render class is a Question");
            currentQuestion = (Question) clazz;
            logger.debug("Class instantiation done.");

            Renderer renderer = new Renderer(currentQuestion);

            if (renderer.needToRender()) {
                showMsgDialog(currentQuestion.enterQuestionMsg(true));  
                
                textPane.setLineWrap(currentQuestion.lineWrap());
                selectBTN.setEnabled(false);
                responseTF.setEditable(true);
                exitBTN.setEnabled(currentQuestion.showExit());   
                
                String title = currentQuestion.getTitle();
                if (title != null) {
                  if (Helper.getBoolean(CallaProp.instance().get("calla.record"), false)) {
                    title = title + " (Recording Mode)";
                  }                  
                  this.setTitle(title);
                }
                
                StringBuffer questionBuf     = renderer.getQuestion();
                StringBuffer explanationBuf  = renderer.getExplanation();
                StringBuffer choiceBuf       = renderer.getChoices();

                if (currentQuestion.isNewScreen()) textPane.setText(null);

                addText(questionBuf.toString(), QUESTION_STYLE);
                addText(explanationBuf.toString(), EXPLANATION_STYLE);
                addText("\n", EXPLANATION_STYLE);
                addText(choiceBuf.toString(), CHOICE_STYLE);

                if (currentQuestion.isPasswordInput()) {
                    passwordTF.setVisible(true);
                    responseTF.setVisible(false);
                    passwordTF.requestFocus();
                } else {
                    passwordTF.setVisible(false);
                    responseTF.setVisible(true);
                    responseTF.requestFocus();
                }
                

                if (currentQuestion.isDirectoryInput()) {
                    showDirectoryDialog();
                }

                if (currentQuestion.isFileInput()) {
                    showFileDialog();
                }
                
                logger.debug("Class rendered.");
                
            } else {
                
                showMsgDialog(currentQuestion.enterQuestionMsg(false));  
                // no need to render
                logger.debug("Class skipped.");
                
                String str = currentQuestion.nextAction();

                render(str);
            }
        }

        if (clazz instanceof Action) {
            logger.debug("render class is an Action");

            Action action = (Action) clazz;
            showSystemOut(action.showSystemOutput());
            
            Executor.execute(action);

            if (action.actionMessage() != null) {
                showMsgDialog(action.actionMessage());
            }

            String str = action.nextAction();

            render(str);
        }
        
        // put the cursor on the first line
        textPane.setCaretPosition(0);

    }

    public void keyReleased(KeyEvent e) {

        // Auto-generated method stub
    }

    public void keyTyped(KeyEvent e) {
      if (e.getSource()==textPane) {
        if (currentQuestion.isPasswordInput()) this.passwordTF.requestFocus();
        if (currentQuestion.isMultipleChoices() ||
            currentQuestion.isFileInput() ||
            currentQuestion.isDirectoryInput()) this.responseTF.requestFocus();
            char c = e.getKeyChar();
            if (c >=32  && c <= 126) {
              this.responseTF.setText(this.responseTF.getText() + c);
            }
      }
    }

    private void setProgramLookAndFeel() {
        String lnfClass = Helper.getString(pStr("lnf"), "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        try {
            UIManager.setLookAndFeel(lnfClass);
            UIManager.put("ProgressBar.repaintInterval", new Integer(100));
            UIManager.put("ProgressBar.cycleTime", new Integer(5000));
            
        } catch (Exception e) {
        	logger.warn("Unable to set Look And Feel : " + lnfClass);
        }
    }

    private String pStr(String key) {
    	String val = CallaProp.instance().get("calla.gui." + key);
    	if (val != null) {
    		return val.trim();
    	} else {
    		return "";
    	}
    }

    private void showMsgDialog(Message msg) {
      if (msg != null) {
        JOptionPane.showMessageDialog(this, msg.getMessageContent(), msg.getMessageTitle(), msg.getMessageType());
      }
    }

    private int showConfirmDialog(String str) {
    	return JOptionPane.showConfirmDialog(this, str, "Confirm", JOptionPane.YES_NO_OPTION);
    }
    
    private void showErrDialog(String str) {
        showMsgDialog(new Message(Message.ERROR, str));
    }

    private void showSystemOut(boolean flag) {
        if (flag) {
            OutputStream out = new OutputStream() {
                public void write(int b) throws IOException {
                    addText(String.valueOf((char) b), CHOICE_STYLE);
                }
                public void write(byte[] b, int off, int len) throws IOException {
                    addText(new String(b, off, len), CHOICE_STYLE);
                }
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                }
            };

            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(out, true));
        } else {
            System.setOut(System.out);
            System.setErr(System.err);
        }
    }

    private void addText(final String str, final String style) {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              try {
                doc.insertString(doc.getLength(), str, doc.getStyle(style));
                
              } catch (BadLocationException ble) {
                logger.error("add text error", ble);
              }
          }
      });        
    }

    private void inProgress(boolean flag) {
      submitBTN.setEnabled(!flag);
      responseTF.setEnabled(!flag);
      passwordTF.setEnabled(!flag);
      textPane.setEnabled(!flag);
      progressBar.setVisible(flag && currentQuestion.showProgress());
      //progressBar.setVisible(flag);
      
      if (flag) {
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      } else {
          setStyles();
          setCursor(Cursor.getDefaultCursor());
          responseTF.requestFocus();
      }
    }

    private void createTextPane() {
        textPane.setEditable(false);
        textPane.setFocusable(true);
        textPane.setBackground(Helper.getColor(pStr("panel.background.color"), Color.WHITE));
        textPane.setDisabledTextColor(Helper.getColor(pStr("disabled.color"), Color.GRAY));
    }

    private void createInputFields() {
        Font rfont = new Font(Helper.getString(pStr("response.font.name"), "Serif"),
                              Helper.getFontStyle(pStr("response.font.style"), Font.BOLD),
                              Helper.getInt(pStr("response.font.size"), 16));
        Color bg = Helper.getColor(pStr("response.background.color"), Color.WHITE);
        Color fg = Helper.getColor(pStr("response.font.color"), Color.BLACK);
        Color cc = Helper.getColor(pStr("response.caret.color"), Color.BLACK);

        try {
	        Image enterImg  = ImageIO.read(getClass().getResource("/symbolthree/calla/icon/enter.png"));
	        Image enterImg2 = enterImg.getScaledInstance(ICONSIZE, ICONSIZE, java.awt.Image.SCALE_SMOOTH);        
	        responseLBL.setIcon(new ImageIcon(enterImg2));
        } catch (Exception e) {
        	responseLBL.setText(" => ");
        }
        responseLBL.setToolTipText("Enter");
 
        responseTF.setColumns(Helper.getInt(pStr("response.size"), 30));
        responseTF.setFont(rfont);
        responseTF.setBorder(BorderFactory.createEmptyBorder());
        responseTF.setBackground(bg);
        responseTF.setForeground(fg);
        responseTF.setCaretColor(cc);
        responseTF.addKeyListener(this);

        // overlay both textfields; set the passwordTF hidden first
        passwordTF.setColumns(Helper.getInt(pStr("response.size"), 30));
        passwordTF.setFont(rfont);
        passwordTF.setBorder(BorderFactory.createEmptyBorder());
        passwordTF.setBackground(bg);
        passwordTF.setForeground(fg);
        passwordTF.setCaretColor(cc);
        passwordTF.addKeyListener(this);
    }

    private void showDirectoryDialog() {
        selectBTN.setEnabled(true);
        responseTF.setEditable(false);

        String currentDir = Helper.getString(
            Answer.getInstance().getB(FILE_BROWSER_CURR_DIR), 
            System.getProperty("user.dir"));
        
        dc = new JFileChooser(currentDir);
        
        dc.setCurrentDirectory(new File(currentDir));
        dc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int returnVal = dc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = dc.getSelectedFile();

            responseTF.setText(file.getAbsolutePath());
            Answer.getInstance().putB(FILE_BROWSER_CURR_DIR, file.getAbsolutePath());
        }
    }

    private void showFileDialog() {
        selectBTN.setEnabled(true);
        responseTF.setEditable(false);
        
        String currentDir = Helper.getString(
            Answer.getInstance().getB(FILE_BROWSER_FILE), 
            System.getProperty("user.dir"));
        
        fc = new JFileChooser(currentDir);
        
        if (currentQuestion.fileExtension()!=null) {
          fc.addChoosableFileFilter(new FileFilter() {
              String filter = currentQuestion.fileExtension();
              public boolean accept(File f) {
                  if (f.isDirectory()) {
                      return true;
                  }
  
                  String extension = Helper.getExtension(f);
  
                  if ((extension != null) &&!extension.equals("")) {
                      if (extension.equalsIgnoreCase(filter)) {
                          return true;
                      } else {
                          return false;
                      }
                  } else {
                      return true;
                  }
              }
              public String getDescription() {
                  return filter.toUpperCase() + " file only";
              }
          });
        }

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            responseTF.setText(file.getAbsolutePath());
            Answer.getInstance().putB(FILE_BROWSER_FILE, file.getAbsolutePath());
        }
    }
    
    private void setStyles() {
      Style defStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

      questionStyle = doc.addStyle(QUESTION_STYLE, defStyle);
      StyleConstants.setBackground(questionStyle, Helper.getColor(pStr("question.background.color"), Color.WHITE));      
      StyleConstants.setForeground(questionStyle, Helper.getColor(pStr("question.font.color"), Color.BLACK));
      StyleConstants.setFontFamily(questionStyle, pStr("question.font.name"));
      StyleConstants.setFontSize(questionStyle, Helper.getInt(pStr("question.font.size"), 14));
      if (pStr("question.font.style").toUpperCase().equals("ITALIC")) {
        StyleConstants.setItalic(questionStyle, true);
      }
      if (pStr("question.font.style").toUpperCase().equals("BOLD")) {
        StyleConstants.setBold(questionStyle, true);
      }
      
      choiceStyle = doc.addStyle(CHOICE_STYLE, defStyle);
      StyleConstants.setBackground(choiceStyle, Helper.getColor(pStr("choice.background.color"), Color.WHITE));      
      StyleConstants.setForeground(choiceStyle, Helper.getColor(pStr("choice.font.color"), Color.BLACK));
      StyleConstants.setFontFamily(choiceStyle, pStr("choice.font.name"));
      StyleConstants.setFontSize(choiceStyle, Helper.getInt(pStr("choice.font.size"), 14));
      if (pStr("choice.font.style").toUpperCase().equals("ITALIC")) {
        StyleConstants.setItalic(choiceStyle, true);
      }
      if (pStr("choice.font.style").toUpperCase().equals("BOLD")) {
        StyleConstants.setBold(choiceStyle, true);
      }

      explanationStyle = doc.addStyle(EXPLANATION_STYLE, defStyle);
      StyleConstants.setBackground(explanationStyle, Helper.getColor(pStr("explanation.background.color"), Color.WHITE));      
      StyleConstants.setForeground(explanationStyle, Helper.getColor(pStr("explanation.font.color"), Color.BLACK));
      StyleConstants.setFontFamily(explanationStyle, pStr("explanation.font.name"));
      StyleConstants.setFontSize(explanationStyle, Helper.getInt(pStr("explanation.font.size"), 14));
      if (pStr("explanation.font.style").toUpperCase().equals("ITALIC")) {
        StyleConstants.setItalic(explanationStyle, true);
      }
      if (pStr("explanation.font.style").toUpperCase().equals("BOLD")) {
        StyleConstants.setBold(explanationStyle, true);
      }      
      
    }
    
    private boolean isRecordingMode() {
       return (Helper.getBoolean(CallaProp.instance().get("calla.record"), false));
    }
    
    private void askForExitProgram() {
    	
    	if (Helper.getBoolean(CallaProp.instance().get(CALLA_EXIT_CONFIRM), true)) {
    	  int rtn = showConfirmDialog("Do you want to quit?");
    	
      	  if (rtn==JOptionPane.OK_OPTION) {
            if (isRecordingMode()) {
            }
            exitProgram(0);
    	  } else {
    		return;
    	  }
    	} else {
    		exitProgram(0);
    	}

    }
    
    private void exitProgram(int exitCode) {
      if (isRecordingMode()) responses.writeResponseFile();
      
      System.exit(exitCode);
   }
    
   private void showSplashScreen() {
       SplashScreen splash = SplashScreen.getSplashScreen();
       if (splash == null) return;
       Graphics2D g = splash.createGraphics();
       if (g == null) return;
       try {
           Thread.sleep(500);
       }
       catch(InterruptedException e) {}       
   }
   
   private void setButtonIcon() {
       try {
       Image submitImg  = ImageIO.read(getClass().getResource("/symbolthree/calla/icon/confirm.png"));
       Image submitImg2 = submitImg.getScaledInstance(ICONSIZE, ICONSIZE, java.awt.Image.SCALE_SMOOTH);
       submitBTN.setIcon(new ImageIcon(submitImg2));
       } catch (Exception e) {
       	submitBTN.setText("Submit " + '\u00BB');
       }
       submitBTN.setToolTipText("Submit");
       submitBTN.setName("SUBMIT");
       
       try {
       Image exitImg  = ImageIO.read(getClass().getResource("/symbolthree/calla/icon/exit.png"));
       Image exitImg2 = exitImg.getScaledInstance(ICONSIZE, ICONSIZE, java.awt.Image.SCALE_SMOOTH);
       exitBTN.setIcon(new ImageIcon(exitImg2));
       } catch (Exception e) {
       	exitBTN.setText("Exit");
       }
       exitBTN.setToolTipText("Exit");
       exitBTN.setName("EXIT");

       try {
       Image selectImg  = ImageIO.read(getClass().getResource("/symbolthree/calla/icon/file.png"));
       Image selectImg2 = selectImg.getScaledInstance(ICONSIZE, ICONSIZE, java.awt.Image.SCALE_SMOOTH);
       selectBTN.setIcon(new ImageIcon(selectImg2));
       } catch (Exception e) {
       	selectBTN.setText("Select...");
       }
       selectBTN.setToolTipText("Select a file or folder");
       selectBTN.setName("SELECT");       

       try {
       Image helpImg  = ImageIO.read(getClass().getResource("/symbolthree/calla/icon/help.png"));
       Image helpImg2 = helpImg.getScaledInstance(ICONSIZE, ICONSIZE, java.awt.Image.SCALE_SMOOTH);
       helpBTN.setIcon(new ImageIcon(helpImg2));
       } catch (Exception e) {
       	helpBTN.setText("Help");
       }
       helpBTN.setToolTipText("Help");
       helpBTN.setName("HELP");       
   }
   
   private void launchHelp() {
   	 String fileName = CallaProp.instance().get(CALLA_HELP_FILE);
   	 
   	 if (fileName==null || fileName.equals("")) {
   		showErrDialog("No help file assigned");
   		return;
   	 }
   	 
   	 String className = currentQuestion.getClass().getName();
   	 String alias = CallaProp.instance().getAlias(className);
     logger.debug("current question = " + className + " (" + alias + ")");
     
     String url = null;
     

     if (fileName.indexOf("$P{alias}") > -1) {
    	 int pos1 = fileName.indexOf("$P{alias}");
    	 int pos2 = pos1 + 9;
    	 fileName = fileName.substring(0,pos1) + alias + fileName.substring(pos2);
     }

     if (fileName.toLowerCase().startsWith("http")) {
    	 url = fileName; 
     } else {
    	 File file = new File(fileName);
    	 url = file.getAbsolutePath();
     }
     
     logger.debug("Help URL = " + url);
     
     try {

    	 if (Desktop.isDesktopSupported()) {
    		 if (fileName.toLowerCase().startsWith("http")) {
    	       Desktop.getDesktop().browse(new URI(url));
    		 } else {
    	       Desktop.getDesktop().open(new File(fileName));
    		 }
    	 }
       
     } catch (Exception e) {
    	 showErrDialog("Unable to open help file");
    	 logger.error("Unable to open help file", e);
     }
     
   }
}
