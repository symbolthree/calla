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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant Task to run CALLA in Silent mode (playback operation)
 * Usage example:
 * <pre>
 * {@code
 *   <property name="calla.dir" location="C:\Program Files\SYMPLiK\CALLA" />
 *   <taskdef name="calla"
 *            classname="symplik.flower.CallAntTask"
 *            classpath="${calla.dir}/classes:
 *                       ${calla.dir}/lib/CALLA.jar:
 *                       ${calla.dir}/lib/jdom-jaxen-1.1.1.zip:
 *                       ${calla.dir}/lib/[other jar files] 
 *   />
 * <target name="runCalla">
 *   <calla configFile="${calla.dir}\flower.properties"/>
 * </target>
 * }  
 * </pre>
 */
public class CallaAntTask extends Task {

  private String configFile;
 
  public CallaAntTask() {
  }

  public void execute() throws BuildException {
    SILENT.main();
  }
  /**
   * Config file Setter method
   * @param configFile (flower.properties)
   */
  public void setConfigFile(String configFile) {
    this.configFile = configFile;
  }

  /**
   * Config file Getter method
   * @return config file (flower.properties) location
   */
  public String getConfigFile() {
    return configFile;
  }
}
