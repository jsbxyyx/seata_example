package org.xxz.test;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author
 */
public class CleanProject {

    public static void main(String[] args) {
        deleteFile("D:\\github\\seata", new Pattern[]{
                Pattern.compile("\\.idea"),
                Pattern.compile(".*\\.iml"),
                Pattern.compile("target"),
                Pattern.compile("\\.flattened-pom\\.xml")
        });
    }

    public static void deleteFile(String path, Pattern[] patterns) {
        File file = new File(path);
        deleteFile(file, false, patterns);
    }

    public static void deleteFile(File file, boolean force, Pattern[] patterns) {
        if (!file.exists())
            return;
        if (force) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFile(f, true, patterns);
                }
            }
            file.delete();
            System.out.println("Deleted file/folder: " + file.getAbsolutePath());
        } else {
            boolean match = match(patterns, file.getName());
            if (match) {
                deleteFile(file, true, patterns);
            } else {
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        deleteFile(f, false, patterns);
                    }
                }
            }
        }
    }

    public static boolean match(Pattern[] patterns, String name) {
        for (Pattern s : patterns) {
            if (s.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }

}
