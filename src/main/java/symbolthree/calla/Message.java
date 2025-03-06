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
 * $Archive: /TOOL/FLOWER/CALLA/src/symbolthree/flower/Message.java $
 * $Author: Christopher Ho $
 * $Date: 1/24/17 9:55a $
 * $Revision: 4 $
******************************************************************************/



package symbolthree.calla;

import javax.swing.JOptionPane;

/**
 * Message object which is displayed as dialog box in GUI, a line of text in CONSOLE mode
 */
public class Message {
    public static final int INFO     = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARN     = JOptionPane.WARNING_MESSAGE;
    public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN    = JOptionPane.PLAIN_MESSAGE;
    public static final int ERROR    = JOptionPane.ERROR_MESSAGE;
    private String          messageContent;
    private int             messageType;

    
    public Message() {
    }
    
    /**
     * @param messageType    INFO, WARN, QUESTION, PLAIN, ERROR
     * @param messageContent message content
     */
    public Message(int messageType, String messageContent) {
        this.messageType    = messageType;
        this.messageContent = messageContent;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageTitle() {
        if (messageType == INFO) {
            return "Info";
        }

        if (messageType == WARN) {
            return "Warning";
        }

        if (messageType == ERROR) {
            return "Error";
        }

        if (messageType == PLAIN) {
            return "";
        }

        if (messageType == QUESTION) {
            return "Question";
        }

        return "";
    }
}
