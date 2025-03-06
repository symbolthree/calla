package symbolthree.calla;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {

	public StringTest() {
	}
	
	public static void main(String[] args) {
		StringTest t = new StringTest();
		t.run();
	}
	
	private void run() {
		String str = "Windows Type is $J{os.name}-($J{os.arch})\n" +
	                 "Java version is $J{java.runtime.version}($J{java.home})\n" +
				     "User home directory is $J{user.home}\n" +
	                 "OS user is $S{USERNAME} and domain is $S{USERDOMAIN}\n" + 
		             "System Path is $S{PATH}\n";
					
		//Pattern vars = Pattern.compile("[$J]\\{(\\S+)\\}");
		Pattern vars = Pattern.compile("[$J]\\{(.+?)\\}");
		Matcher m = vars.matcher(str);
		StringBuffer sb = new StringBuffer();
		int lastMatchEnd = 0;
        while (m.find()) {
        	System.out.println(m.start() + " " + m.end() + " " + m.group(1));
            sb.append(str.substring(lastMatchEnd, m.start()-1));
            final String envVar = m.group(1);
            final String envVal = System.getProperty(envVar);
            if (envVal == null)
                sb.append(str.substring(m.start(), m.end()));
            else
                sb.append(envVal);
            lastMatchEnd = m.end();
        }
        sb.append(str.substring(lastMatchEnd));
        str = sb.toString();
        
		vars = Pattern.compile("[$S]\\{(.+?)\\}");
		m = vars.matcher(str);
		sb = new StringBuffer();
		lastMatchEnd = 0;
        while (m.find()) {
        	System.out.println(m.start() + " " + m.end() + " " + m.group(1));
            sb.append(str.substring(lastMatchEnd, m.start()-1));
            final String envVar = m.group(1);
            final String envVal = System.getenv(envVar);
            if (envVal == null)
                sb.append(str.substring(m.start(), m.end()));
            else
                sb.append(envVal);
            lastMatchEnd = m.end();
        }
        sb.append(str.substring(lastMatchEnd));
        
        System.out.println(sb.toString());
        
        
	}

}
