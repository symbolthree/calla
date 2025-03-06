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
import java.util.*;

/**
 * A console (text-based) password input field
 * 
 * @author  $Author: Christopher Ho $
 * @version $Revision: 4 $
 */
public class PasswordField {

    /**
     * @param input stream to be used (e.g. System.in)
     * @param prompt The prompt to display to the user.
     * @return The password as entered by the user.
     */
    public static final char[] getPassword(InputStream in, String prompt) throws IOException {
        MaskingThread maskingthread = new MaskingThread(prompt);
        Thread        thread        = new Thread(maskingthread);

        thread.start();

        char[] lineBuffer;
        char[] buf;

        buf = lineBuffer = new char[128];

        int room   = buf.length;
        int offset = 0;
        int c;

loop:
        while (true) {
            switch (c = in.read()) {
            case -1 :
            case '\n' :
                break loop;

            case '\r' :
                int c2 = in.read();

                if ((c2 != '\n') && (c2 != -1)) {
                    if (!(in instanceof PushbackInputStream)) {
                        in = new PushbackInputStream(in);
                    }

                    ((PushbackInputStream) in).unread(c2);
                } else {
                    break loop;
                }
            default :
                if (--room < 0) {
                    buf  = new char[offset + 128];
                    room = buf.length - offset - 1;
                    System.arraycopy(lineBuffer, 0, buf, 0, offset);
                    Arrays.fill(lineBuffer, ' ');
                    lineBuffer = buf;
                }

                buf[offset++] = (char) c;

                break;
            }
        }

        maskingthread.stopMasking();

        if (offset == 0) {
            return null;
        }

        char[] ret = new char[offset];

        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');

        return ret;
    }
}
