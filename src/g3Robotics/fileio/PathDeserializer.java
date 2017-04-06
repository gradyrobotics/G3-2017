package g3Robotics.fileio;

import g3Robotics.trajectorylib.Path;

/**
 * Interface for methods that deserializes a Path or Trajectory.
 * 
 * @author Jared341
 */
public interface PathDeserializer {
  
  public Path deserialize(String serialized);
}
