import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ls {
    private final File mainFile;
    private final File outputFile;
    private final boolean longer, reverse, human;

    public Ls(boolean longer, boolean human, boolean reverse, File output, String directoryName) {
        this.longer = longer;
        this.reverse = reverse;
        this.human = human;
        this.outputFile = output;
        this.mainFile = new File(directoryName);
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
        long size = file.isDirectory() ? 0 : file.length();
        long coefficient;
        String dataSize;

        if (size < 1024 * 8) {
            coefficient = 8;
            dataSize = "Byte";
        } else if (size < 1024 * 1024 * 8) {
            coefficient = 1024 * 8;
            dataSize = "Kb";
        } else if (size < 1024L * 1024 * 1024 * 8) {
            coefficient = 1024 * 1024 * 8;
            dataSize = "Mb";
        } else {
            coefficient = 1024L * 1024 * 1024 * 8;
            dataSize = "Gb";
        }

        if (human) {
            return size / coefficient + dataSize;
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
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        return (file.getName() + " " + this.getRules(file) +
                " " + this.getSize(file) + " " + this.getTime(file)).strip();
    }

    public List<String> getLs() throws IOException {
        @NotNull
        File[] files = mainFile.isDirectory() ? mainFile.listFiles() : new File[]{mainFile};
        List<String> lsAnswer = new ArrayList<>();

        if (reverse) {
            Arrays.sort(files, Collections.reverseOrder());
        } else {
            Arrays.sort(files);
        }

        for (File file : files) {
            lsAnswer.add(getOut(file));
        }

        if (outputFile != null) {
            FileWriter writer = new FileWriter(outputFile);

            for (String str: lsAnswer) {
                writer.write(str);
                writer.write("\n");
            }

            writer.close();
            return Collections.singletonList("The information is written to a file");
        } else {
            return lsAnswer;
        }
    }
}
