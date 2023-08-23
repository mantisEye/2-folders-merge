import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FolderMerger {

    public static int sourceN = 0;

    public static void main(String[] args) throws Exception {

        // create a new output folder
        createFolder("merged");

        // open folder (source1), collect all files and put them in merged
        File source1 = new File("./source1");
        File[] files = source1.listFiles();
        sourceN++;
        meatAndPotatos(files, "./source1");

        // repeat for source2
        File source2 = new File("./source2");
        files = source2.listFiles();
        sourceN++;
        meatAndPotatos(files, "./source2");

       System.out.println("Files have been successfully merged and moved. Check the 'merged' folder.");
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
                .sorted((a, b) -> b.compareTo(a)) // sort in reverse order for proper deletion
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.out.println("Failed to delete: " + path + " - " + e.getMessage());
                    }
                });
        }
    }

    public static void meatAndPotatos(/*FileWriter writer,*/ File[] files, String sPath) throws IOException {
        
        int items = 1;
        for (int i = 0; i < files.length; i++) {

            File currFile = files[i];

            // get the extention
            String newName = "s"+ sourceN + "-" + (items++) + "_" + currFile.getName();

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
