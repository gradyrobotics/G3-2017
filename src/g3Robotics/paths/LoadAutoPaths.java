package g3Robotics.paths;

import g3Robotics.trajectorylib.Path;
import g3Robotics.fileio.TextFileDeserializer;
import g3Robotics.fileio.TextFileReader;
import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;

/**
 * Load all autonomous mode paths.
 * 
 * Stolen from Team 254.
 */
public class LoadAutoPaths {
  // Make sure these match up!
  public final static String[] kPathNames = { "TestPath",
                                              "CenterLanePathFar",
                                              "WallLanePath",
                                              "InsideLanePathClose", 
                                              "StraightAheadPath",
                                              };
  public final static String[] kPathDescriptions = { "Straight Line, Far", 
                                                     "Middle Lane",
                                                     "Wall Lane",
                                                     "Inside, Close",
                                                     "Straight ahead",
                                                     };
  static Hashtable<String, Path> paths_ = new Hashtable<String, Path>();
  
  public static void loadPaths() {
    Timer t = new Timer();
    t.start();
    TextFileDeserializer deserializer = new TextFileDeserializer();
    for (int i = 0; i < kPathNames.length; ++i) {
      
      TextFileReader reader = new TextFileReader("file://" + kPathNames[i] + 
              ".txt");
      
      Path path = deserializer.deserialize(reader.readWholeFile());
      paths_.put(kPathNames[i], path);
    }
    System.out.println("Parsing paths took: " + t.get());
  }
  
  public static Path get(String name) {
    return (Path)paths_.get(name);
  }
  
  public static Path getByIndex(int index) {
    return (Path)paths_.get(kPathNames[index]);
  }
}
