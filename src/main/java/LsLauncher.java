/*Вариант 1 -- ls
        Вывод содержимого указанной в качестве аргумента директории в виде
        отсортированного списка имен файлов.
        Флаг -l (long) переключает вывод в длинный формат, в котором, кроме имени
        файла, указываются права на выполнение/чтение/запись в виде битовой маски XXX, время
        последней модификации и размер в байтах.
        Флаг -h (human-readable) переключает вывод в человеко-читаемый формат (размер в
        кило-, мега- или гигабайтах, права на выполнение в виде rwx).
        Флаг -r (reverse) меняет порядок вывода на противоположный.
        Флаг -o (output) указывает имя файла, в который следует вывести результат; если
        этот флаг отсутствует, результат выводится в консоль.
        В случае, если в качестве аргумента указан файл, а не директория, следует вывести информацию об этом файле.
        Command Line: ls [-l] [-h] [-r] [-o output.file] directory_or_file
        Actually will be: java -jar ls.jar [-l] [-h] [-r] [-o output.file] directory_or_file
        Кроме самой программы, следует написать автоматические тесты к ней.*/


import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class LsLauncher {

    @Option(name = "-l", aliases = "--long", required = false)
    private boolean longer;

    @Option(name = "-h", aliases = "--human", required = false)
    private boolean humanReadable;

    @Option(name = "-r", aliases = "--reverse", required = false)
    private boolean reverse;

    @Option(name = "-o", aliases = "--output", required = false)
    private String output;

    @Argument(required = true, metaVar = "InputName", usage = "Input directory/file name")
    private String inputDirectoryName;


    public static void main(String[] args) {
        new LsLauncher().launch(args);
    }

    private void launch(String[] args) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar ls.jar [-l] [-h] [-r] [-o output.file] directory_or_file");
            parser.printUsage(System.err);
            return;
        }

        Ls ls = new Ls(longer, humanReadable, reverse, output, inputDirectoryName);
        try {
            ls.getLs();
        } catch (Exception e) {
            System.out.println("There is no such directory");
        }
    }
}
