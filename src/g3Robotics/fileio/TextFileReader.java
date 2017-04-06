package g3Robotics.fileio;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
//import javax.microedition.io.file;

/**
 * Read a text file into a string.
 *
 * @author AlexL
 */
public class TextFileReader {

  //private FileConnection file_connection_ = null;
  private File mFile;
  private BufferedReader mReader = null;

  public TextFileReader(String uri) {
    try {
      // Open the new file
    	mFile = new File(uri);
    	 if( !mFile.exists() )
         {
             System.err.println("Could not find specified file!");
             return;
         }
         mReader = new BufferedReader( new FileReader(mFile));
   
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Could not open file connection!");
      closeFile();
    }
  }

  private void closeFile() {

  }

  // Returns null at end of file
  public String readLine() {
    String line = null;
    try {
      line = mReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      closeFile();
    }
    return line;
  }
  
  public String readWholeFile() {
    StringBuffer buffer = new StringBuffer();
    String line;
    while ((line = readLine()) != null) {
      buffer.append(line);
      buffer.append("\n");
    }
    return buffer.toString();
  }
}
