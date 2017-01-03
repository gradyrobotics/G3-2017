package g3Robotics.fileio;

//import com.sun.squawk.io.BufferedReader;
//import com.sun.squawk.microedition.io.*;
//import com.sun.squawk.util.*;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

//import javax.microedition.io.*;

/**
 * This class parses text files for properties.
 *
 * @author PratikKunapuli
 */
public class PropertyReader
{
    //private FileConnection mFileConnection = null;
    private File mFile;
    private BufferedReader mReader = null;

    private final PropertySet mPropertySet;

    public PropertyReader()
    {
        mPropertySet = PropertySet.getInstance();
    }

    private void closeFile()
    {
    }

    private void parseLine(String aLine) throws IOException
    {
        StringTokenizer lStringTok = new StringTokenizer(aLine);

        int lNumTokens = lStringTok.countTokens();

        // We best have at least two tokens ...
        if( lNumTokens < 2 )
        {
            throw new IOException("Malformed file");
        }

        String lKey = lStringTok.nextToken();
        String lValue = lStringTok.nextToken();
        
        mPropertySet.addProperty(lKey, lValue);
    }
    
    private void parseAutonomousLine(String aLine, int aStateIndex) throws IOException
    {
        StringTokenizer lStringTok = new StringTokenizer(aLine);

        int lNumTokens = lStringTok.countTokens();

        // We best have at least two tokens ...
        if( lNumTokens < 1 )
        {
            // Error!
            throw new IOException("Malformed file");
        }

        // The first token is the autonomous state itself
        String lKey = "AutonomousState" + aStateIndex;
        String lValue = lStringTok.nextToken();

        mPropertySet.addProperty(lKey, lValue);

        // Any addition tokens are args
        if( lNumTokens > 1 )
        {
            lKey = "AutonomousFirstParam" + aStateIndex;
            lValue = lStringTok.nextToken();
            mPropertySet.addProperty(lKey, lValue);
        }
        if( lNumTokens > 2 )
        {
            lKey = "AutonomousSecondParam" + aStateIndex;
            lValue = lStringTok.nextToken();
            mPropertySet.addProperty(lKey, lValue);
        }
        if( lNumTokens > 3 )
        {
            lKey = "AutonomousThirdParam" + aStateIndex;
            lValue = lStringTok.nextToken();
            mPropertySet.addProperty(lKey, lValue);
        }
        if( lNumTokens > 4 )
        {
            lKey = "AutonomousFourthParam" + aStateIndex;
            lValue = lStringTok.nextToken();
            mPropertySet.addProperty(lKey, lValue);
        }
        if( lNumTokens > 5 )
        {
            lKey = "AutonomousFifthParam" + aStateIndex;
            lValue = lStringTok.nextToken();
            mPropertySet.addProperty(lKey, lValue);
        }
    }

    public void parseAutonomousFile(String aURI)
    {
        try
        {
            // Close any lingering files first
            closeFile();

            // Open the new file
            //mFileConnection = (FileConnection)Connector.open(aURI);
            mFile = new File (aURI);
            if( !mFile.exists() )
            {
                // fileConnection.create();
                System.err.println("Could not find specified file!");
                return;
            }

            // Make an I/O adapter sandwich to actually get some text out
            mReader = new BufferedReader( new FileReader(mFile));

            // Now parse the thing
            String lLine;

            // Loop through each line to read in the actions
            int lAutonomousState = 1;
            //lLine = mReader.readLine();
            //mPropertySet.addProperty("AutonomousName", lLine);
            while( (lLine = mReader.readLine()) != null)
            {
                parseAutonomousLine(lLine, lAutonomousState);
                lAutonomousState++;
            }

            mPropertySet.addProperty("AutonomousNumStates", Integer.toString(lAutonomousState-1));
            System.out.println("Finished parsing properties file.");
        }
        catch( IOException e )
        {
            System.err.println("Could not open file connection!");
        }
        finally
        {
            closeFile();
        }
    }

    public void parseFile(String aURI)
    {
        try
        {
            // Close any lingering files first
            closeFile();

            // Open the new file
            mFile = new File (aURI);
            if( !mFile.exists() )
            {
                // fileConnection.create();
                System.err.println("Could not find specified file!");
                return;
            }

            // Make an I/O adapter sandwich to actually get some text out
            mReader = new BufferedReader( new FileReader(mFile));

            // Now parse the thing
            String lLine;

            // Loop through each line to read in the actions
            while( (lLine = mReader.readLine()) != null)
            {
                parseLine(lLine);
            }
            System.out.println("Finished parsing properties file.");
        }
        catch( IOException e )
        {
            System.err.println("Could not open file connection!");
        }
        finally
        {
            closeFile();
        }
    }

}
