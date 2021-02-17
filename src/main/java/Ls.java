import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Ls {
    private final File directory;
    private final String output;
    private final boolean longer, reverse, human;

    public Ls(boolean longer, boolean human, boolean reverse, String output, String directoryName) {
        this.longer = longer;
        this.reverse = reverse;
        this.human = human;
        this.output = output;
        this.directory = new File(directoryName);
    }

    @NotNull
    private String getRules(File file) {
        boolean x = file.canExecute();
        boolean w = file.canWrite();
        boolean r = file.canRead();
        if (human) {
            return (r ? "r" : "") + (w ? "w" : "") + (x ? "x" : "");
        } else if (longer) {
            return (r ? "1" : "0") + (w ? "1" : "0") + (x ? "1" : "0");
        }
        return "";
    }

    @NotNull
    private String getSize(File file) {
        long size = file.length();

        if (human) {
            if (file.isDirectory()) {
                return size + "Kb";
            } else {
                return size / 1024 + "Kb";
            }
        } else if (longer) {
            return String.valueOf(size);
        }

        return "";
    }

    @NotNull
    private String getTime(File file) {

        if (longer) {
            return String.valueOf(file.lastModified());
        }

        return "";
    }

    private String getOut(File file) {
        if (!file.exists()) { throw new IllegalArgumentException(); }
        return (file.getName() + " " + this.getRules(file) +
                " " + this.getSize(file) + " " + this.getTime(file)).strip();
    }

    public void getLs() throws IOException {

        if (directory.isDirectory()) {
            @NotNull
            File[] files = directory.listFiles();
            if (reverse) {
                Arrays.sort(files, Collections.reverseOrder());
            } else {
                Arrays.sort(files);
            }

            if (output == null) {
                for (File file : files) {

                    System.out.println(getOut(file));
                }

            } else {
                if (!new File(output).exists()) { throw new IllegalArgumentException(); }
                FileWriter writer = new FileWriter(output);


                for (File file : files) {
                    writer.write(getOut(file));
                    writer.write("\n");
                }

                writer.close();
            }
        } else {
            if (output == null) {
                System.out.println(getOut(directory));
            } else {
                if (!new File(output).exists()) { throw new IllegalArgumentException(); }
                FileWriter writer = new FileWriter(output);
                writer.write(getOut(directory));
                writer.write("\n");
                writer.close();
            }
        }
    }
}
