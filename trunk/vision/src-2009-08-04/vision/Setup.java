/*
 * Setup.java
 * 
 */
package vision;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Setup {

    public static final String MAP_SCALE = "map.scale";
    public static final String MAP_LAT = "map.eye.latitude";
    public static final String MAP_LONG = "map.eye.longitude";
    public static final String SSC32_PORT = "ssc.port";
    public static final String GPS_PORT = "gps.port";
    public static final String CAM_ANGLE = "camera.angle";
    public static final String CAM_HEIGHT = "camera.height";
    public static final String RNDF_FILE = "map.rndf.file";
    public static final String MDF_FILE = "map.mdf.file";
    public static final String REMOTE_PORT = "remote.port";
    private static Properties properties;
    static String root = System.getProperty("user.home") + "/robotour/";
    static File propertiesFile = new File(System.getProperty("user.home") + "/robotour.properties");

    /** uninstantiable */
    private Setup() {
    }


    static {
        loadProperties();
    }

    /**
     *  loads properties from robotour.properties file
     */
    private static void loadProperties() {
        System.out.println("loading properties " + propertiesFile);

        try {

            properties = new Properties();
            Reader reader = new FileReader(propertiesFile);
            properties.load(reader);
            reader.close();

            System.out.println("User properties success");

        } catch (Exception ex) {
            ex.printStackTrace();

            checkDirectories();
            System.out.println("Loading default properties");

            try {
                properties = new Properties();
                Reader reader = new InputStreamReader(Class.class.getResourceAsStream("/default.properties"));
                properties.load(reader);
                reader.close();

                System.out.println("Default properties success");

            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }


    }

    public static String get(String propertyName) {
        if (properties == null) {
            loadProperties();
        }
        String value = properties.getProperty(propertyName);
        System.out.println("Setup.get(" + propertyName + ") = " + value);
        return value;
    }

    public static void set(String propertyName, String value) {
        if (properties == null) {
            loadProperties();
        }
        properties.setProperty(propertyName, value);
//        System.out.println("Setup.set("+propertyName+", "+value+")");
    }

    /**
     *  
     * @param propertyName
     * @return int value of the given property. 0 if not found or error occures
     * throws no exception
     */
//    public static int getInt(String propertyName) {
//        try {
//            return Integer.parseInt(get(propertyName));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return 0;
//        }
//    }
    /**
     *  
     * @param propertyName
     * @param def default value
     * @return int value of the given property. 0 if not found or error occures
     * throws no exception
     */
    public static int getInt(String propertyName, int def) {
        try {
            String val = get(propertyName);
            if (val == null) {
                return def;
            }
            return Integer.parseInt(val);
        } catch (NumberFormatException ex) {
            System.err.println("Setup.getInt: " + ex.getMessage());
//            ex.printStackTrace();
            return def;
        }
    }

    public static String getRoot() {
        return root;
    }

    public static void reload() {
        loadProperties();
    }

    public static void store() {
        // TODO check modify in future - exit

        if (properties == null) {
            System.out.println("saving properties error! properties are null! may not have been used");
            return;
        }
        System.out.println("saving properties " + propertiesFile);
        properties.list(System.out);
        try {
            Writer writer = new FileWriter(propertiesFile);
            properties.store(writer, " Robotour properties file\n Tomas Kotula");
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Setup.properties saved");
    }

    static private void checkDirectories() {

        System.out.println("Creating root folder");
        try {
            File rootdir = new File(root);
            // see docs creates only if does not exists
            rootdir.createNewFile();
            new File(root + "vision.properties").createNewFile();
            new File(root + "tracks/").createNewFile();
            new File(root + "logs/").createNewFile();
            System.out.println("success");
        } catch (IOException ex) {
            System.out.println("failure!:");
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
