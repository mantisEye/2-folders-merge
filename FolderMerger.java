import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.file.FileVisitOption;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FolderMerger {

    public static int items = 1;

    public static void main(String[] args) throws Exception {

        // create a new output folder
        createFolder("merged");

        FileWriter writer = new FileWriter("name_change_list.txt");

        // open folder (source1), collect all files
        File source1 = new File("./source1");
        File[] files = source1.listFiles();

        meatAndPotatos(writer, files, "./source1");

        // repeat for source2
        File source2 = new File("./source2");
        files = source2.listFiles();

        meatAndPotatos(writer, files, "./source2");

        System.out.println(items + " items been moved.");

        writer.close();
    }



    public static void createFolder(String folderPath) throws IOException {

        Path folder = Paths.get(folderPath);

        if (Files.exists(folder)) {
            deleteFolder(folderPath); // delete the existing folder and its content
        }

        Files.createDirectory(folder); // create the new folder
    }


    public static void deleteFolder(String folderPath) throws IOException {

        Path folder = Paths.get(folderPath);

        if (Files.exists(folder)) {
            Files.walk(folder, FileVisitOption.FOLLOW_LINKS)
                .sorted((a, b) - > b.compareTo(a)) // sort in reverse order for proper deletion
                .forEach(path - > {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.out.println("Failed to delete: " + path + " - " + e.getMessage());
                    }
                });
        }
    }

    public static void meatAndPotatos(FileWriter writer, File[] files, String sPath) throws IOException {
        for (int i = 0; i < files.length; i++) {

            File currFile = files[i];

            // get the extention
            String filename = currFile.getName();
            String extension = filename.substring(filename.lastIndexOf('.') + 1);

            String newName = "item" + (items++) + "." + extension;

            // add to the name_change_list
            writer.append(currFile.getName() + "  ->  " + newName + "\n");
            writer.flush();

            // move and rename
            Path sourcePath = Path.of(sPath, currFile.getName());
            Path destinationPath = Path.of("./merged", newName);
            try {
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Failed to move the file: " + e.getMessage());
            }
        }
    }
}