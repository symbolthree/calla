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

/**
 * A thread which displays input characters as masked characters
 */
public class MaskingThread extends Thread {
    private char             echochar = '>';
    private volatile boolean stop;

    /**
     * @param prompt The prompt displayed to the user
     */
    public MaskingThread(String prompt) {
        System.out.print(prompt);
    }

    /**
     * Begin masking until asked to stop.
     */
    public void run() {
        int priority = Thread.currentThread().getPriority();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        try {
            stop = true;

            while (stop) {
                System.out.print("\010" + echochar);

                try {

                    Thread.currentThread();
					// attempt masking at this rate
                    Thread.sleep(1);
                } catch (InterruptedException iex) {
                    Thread.currentThread().interrupt();

                    return;
                }
            }
        } finally {    // restore the original priority
            Thread.currentThread().setPriority(priority);
        }
    }

    /**
     * Instruct the thread to stop masking.
     */
    public void stopMasking() {
        this.stop = false;
    }
}
