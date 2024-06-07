package Mavericks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleDataValidation {

    // t this can read c content from file which is given in while calling...
    public static String readContent(String ff_file_fileName, String afolderName) throws IOException {
        File f__file = new File("../Mavericks/" + afolderName + "/" + ff_file_fileName);

        StringBuilder contentBuilder = new StringBuilder();
        try (Scanner asa = new Scanner(f__file)) {
            while (asa.hasNextLine()) {
                contentBuilder.append(asa.nextLine());
            }
        }
        return contentBuilder.toString();
    }

    public static ArrayList<String> listFileNames(String ff_folderName) {
        ArrayList<String> fileNameList = new ArrayList<>();
        File a_folder = new File("../Mavericks/" + ff_folderName);

        File[] fileList = a_folder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                fileNameList.add(file.getName());
            }
        }
        return fileNameList;
    }

    // t this perform m validateDataa .
    public static void validateData() throws IOException {
        // c check title and l langague using regexx....
        String titlePattern = "<title>([\\s\\S]*?)</title>";
        String langPattern = "lang=\"([\\s\\S]*?)\"";

        String[] qw_folders = { "Yupik_files", "SaveonfoodsFiles", "Kroger_Files" };

        for (String as_folderName : qw_folders) {
            // System.out.println("Validating titles and languages in " + as_folderName +
            // "folder.");

            ArrayList<String> fileNameList = listFileNames(as_folderName);

            for (String fileName : fileNameList) {
                String fileContent = readContent(fileName, as_folderName);

                Pattern tt_titleRegex = Pattern.compile(titlePattern);
                Pattern ll_langRegex = Pattern.compile(langPattern);

                Matcher titleMatcher = tt_titleRegex.matcher(fileContent);
                Matcher langMatcher = ll_langRegex.matcher(fileContent);

                if (titleMatcher.find() && langMatcher.find()) {
                    // System.out.println("File: " + fileName +
                    // " Title: \"" + titleMatcher.group(1) + "\"; Lang: \"" + langMatcher.group(1)
                    // + "\"");
                }
            }

        }
    }

}