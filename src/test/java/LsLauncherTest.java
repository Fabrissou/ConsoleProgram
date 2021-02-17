import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class LsLauncherTest {

    private void assertFileContent(String name, String expectedContent) throws IOException {
//        File file = new File(name);
        String content = new String(Files.readAllBytes(Paths.get(name)));
        assertEquals(expectedContent, content);
    }

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(output));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(null);
    }

    @Test
    public void main() throws IOException {
        LsLauncher.main(new String[]{"files"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"--long", "files"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"--human", "files"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"-l", "files/lol"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"-r", "-h", "files/newDir"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"-l", "-h", "--reverse", "files/testdir"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"abc"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"-r", "abc"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"-l", "files/lol/keker"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"--reverse", "files/SUPERMEGADIR"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"--long", "ULTRADIR99999"});
        System.out.println("\n");
        LsLauncher.main(new String[]{"fbi"});


        Assert.assertEquals("""
                lol
                newDir
                superDiiir
                testdir
                    
                                
                lol 111 160 1613464462146
                newDir 111 192 1613464462096
                superDiiir 111 96 1613464462143
                testdir 111 160 1613538834100 
                                
                                
                lol rwx 160Kb
                newDir rwx 192Kb
                superDiiir rwx 96Kb
                testdir rwx 160Kb   
                                
                                
                abc 110 74600 1613464462100
                keker 110 96880 1613464462086
                names 110 2364 1613464462146
                                
                                
                gun rw 1Kb
                goodFile rw 2Kb
                ffff rw 1Kb
                327854358439 rw 0Kb
                                
                                
                jobs rw 11Kb 1613464462148
                haha rw 5Kb 1613538834100
                93939393993939393993 rw 0Kb 1613464443179
                                
                                
                A
                B
                C
                D
                E
                                
                                
                E
                D
                C
                B
                A
                                
                                
                keker 110 96880 1613464462086
                                
                                
                There is no such directory
                                
                                
                There is no such directory
                                
                                
                There is no such directory
                """, output.toString());

        LsLauncher.main(new String[]{"-o", "saveFiles/save", "files"});
        assertFileContent("saveFiles/save", """
                lol
                newDir
                superDiiir
                testdir
                """);
        LsLauncher.main(new String[]{"-o", "saveFiles/save", "files/lol"});
        assertFileContent("saveFiles/save", """
                abc
                keker
                names
                """);
        LsLauncher.main(new String[]{"-l", "-r", "--human","--output", "saveFiles/save", "files"});
        assertFileContent("saveFiles/save", """
                testdir rwx 160Kb 1613538834100
                superDiiir rwx 96Kb 1613464462143
                newDir rwx 192Kb 1613464462096
                lol rwx 160Kb 1613464462146
                """);
        LsLauncher.main(new String[]{"--long", "-o", "saveFiles/save", "files"});
        assertFileContent("saveFiles/save", """
                lol 111 160 1613464462146
                newDir 111 192 1613464462096
                superDiiir 111 96 1613464462143
                testdir 111 160 1613538834100
                """);
        LsLauncher.main(new String[]{"--output", "saveFiles/save", "abc"});
        assertFileContent("saveFiles/save", """
                A
                B
                C
                D
                E
                """);
        LsLauncher.main(new String[]{"--reverse", "-o", "saveFiles/save", "abc"});
        assertFileContent("saveFiles/save", """
                E
                D
                C
                B
                A
                """);




    }

}