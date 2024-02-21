package Objects;

import java.io.*;

public class IOController {

    public enum ObjectType {ATTRACTION, LOCATION, SCHEDULE_ITEM};
    private static final String attractionFolderName = "attractions/";
    private static final String locationFolderName = "locations/";
    private static final String scheduleItemFolderName = "scheduleItems/";
    private static final String baseFolderName = "data/";

    /**
     * createFilePath
     * returns a String of the full relative filepath using the default baseFolder, the given id and getSubFolder() with objectType as parameter
     * @param id the id of the object that will be referred to in the filepath
     * @param objectType ObjectType enum symbolizing the object type that defines what folder path to use
     * @return a String of the full relative filepath
     * @author Joshua Roovers
     */
    private static String createFilePath(int id, ObjectType objectType){
        String idString = Integer.toString(id);
        return baseFolderName+getSubFolder(objectType)+idString;
    }

    /**
     * getSubFolder
     * returns the corresponding FolderName according to the given objectType enum
     * @param objectType ObjectType enum symbolizing the object type that defines what folder path to use
     * @return a string with the FolderName associated with the objectType enum
     * @author Joshua Roovers
     */
    private static String getSubFolder(ObjectType objectType){
        String subFolder = null;
        switch (objectType){
            case ATTRACTION:
                subFolder = attractionFolderName;
                break;
            case LOCATION:
                subFolder = locationFolderName;
                break;
            case SCHEDULE_ITEM:
                subFolder = scheduleItemFolderName;
                break;
        }
        return subFolder;
    }

    /**
     * update
     * calls saveObjectToFile() method with the newVersion and filePath as parameter created from createFilePath() using the id and objectType
     * @param id the id of the object that needs to be updated
     * @param newVersion the latest version of the Object that will be updated
     * @param objectType ObjectType enum symbolizing the object type that defines what folder path to use
     * @author Joshua Roovers
     */
    public static void update(int id, Object newVersion, ObjectType objectType){
        saveObjectToFile(createFilePath(id, objectType), newVersion);
    }

    /**
     * delete
     * calls deleteFile() method with the filePath as parameter created from createFilePath() using the id and objectType
     * @param id the id of the object that needs to be deleted
     * @param objectType ObjectType enum symbolizing the object type that defines what folder path to use
     * @author Joshua Roovers
     */
    public static void delete(int id, ObjectType objectType){
        deleteFile(createFilePath(id, objectType));
    }

    /**
     * saveObjectToFile
     * creates or updates a file from the given object
     * @param filePath path name of the file that will store the object
     * @param data the object to be stored
     * @author Joshua Roovers
     */
    private static void saveObjectToFile(String filePath, Object data) {

//        String filename = Integer.toString(fileId);//pref to hash
//        String filePath = "data/"+dataFolderName+"/"+filename; //todo could probably use a file type but works as is

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // Serialize and write the object to the file
            outputStream.writeObject(data);
            System.out.println("Object stored in the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * getObjectFromFile
     * returns an object that was the file of the given filepath
     * @param filePath the pathname of the file
     * @return returns the retrieved object
     * @author Joshua Roovers
     */
    public static Object getObjectFromFile(String filePath) {
//        String filename = Integer.toString(fileId);//pref to hash
//        String filePath = "data/"+dataFolderName+"/"+filename; //todo could probably use a file type but works as is

        // Read the object back from the file
        Object recoveredObject = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            // Read the object from the file
            recoveredObject = (Object) inputStream.readObject();
            //System.out.println("Object read from file: " + recoveredObject);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recoveredObject;
    }

    /**
     * deleteFile //todo might need some security to keep it within the data folder scope
     * deletes the file at the given filepath
     * @param filePath the pathname of the file
     * @author JoshuaRoovers
     */
    public static void deleteFile(String filePath){
        // Create a File object representing the file
        File file = new File(filePath);

        // Check if the file exists before deleting
        if (file.exists()) {
            // Delete the file
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
