import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class LsLauncherTest {
    private void assertFileContent(String expectedContent) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("testFiles/output/out")));
        assertEquals(expectedContent, content);
    }

    @Test
    public void main() throws IOException {
//      простой тест
        LsLauncher.main(new String[]{"testFiles/input"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "abc",
                "lol",
                "newDir",
                "superDiiir",
                "test",
                "testdir")), LsLauncher.getLss());

////        простой тест
        LsLauncher.main(new String[]{"testFiles/input/lol"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "abc",
                "keker",
                "names")), LsLauncher.getLss());

////        тесы флага -l
        LsLauncher.main(new String[]{"-l", "testFiles/input/lol"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "abc 110 74600 1613464462100",
                "keker 110 96880 1613464462086",
                "names 110 2364 1613464462146")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--long", "testFiles/input/newDir"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "327854358439 110 720 1613464462082",
                "ffff 110 1610 1613464462090",
                "goodFile 110 2366 1613464462079",
                "gun 110 1643 1613464462096")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--long", "testFiles/input/testdir"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "93939393993939393993 110 0 1613464443179",
                "haha 110 5134 1613538834100",
                "jobs 110 11837 1613464462148")), LsLauncher.getLss());

//        тесты флага -h
        LsLauncher.main(new String[]{"-h", "testFiles/input/superDiiir"});
        assertEquals(new ArrayList<>(Collections.singletonList("printer rw 4Kb")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--human", "testFiles/input/abc"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "A rw 0Byte",
                "B rw 0Byte",
                "C rw 0Byte",
                "D rw 0Byte",
                "E rw 0Byte")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"-h", "testFiles/input/newDir"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "327854358439 rw 90Byte",
                "ffff rw 201Byte",
                "goodFile rw 295Byte",
                "gun rw 205Byte")), LsLauncher.getLss());

//        тесты флага -r
        LsLauncher.main(new String[]{"-r", "testFiles/input/abc"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "E", "D", "C", "B", "A")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--reverse", "testFiles/input/lol"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "names", "keker", "abc")), LsLauncher.getLss());

//        тесты флага -o
        LsLauncher.main(new String[]{"-o", "testFiles/output/out", "testFiles/input"});
        assertFileContent("""
                abc
                lol
                newDir
                superDiiir
                test
                testdir
                """);

        LsLauncher.main(new String[]{"--output", "testFiles/output/out", "testFiles/input/lol"});
        assertFileContent("""
                abc
                keker
                names
                """);

//        тесты одиночного файла
        LsLauncher.main(new String[]{"-l", "testFiles/input/lol/names"});
        assertEquals(new ArrayList<>(Collections.singletonList("names 110 2364 1613464462146")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"-h", "testFiles/input/lol/keker"});
        assertEquals(new ArrayList<>(Collections.singletonList("keker rw 11Kb")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--output", "testFiles/output/out", "testFiles/input/newDir/ffff"});
        assertFileContent("ffff\n");

//        смешанные тесты
        LsLauncher.main(new String[]{"--long", "-h", "--reverse", "testFiles/input/newDir"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "gun rw 205Byte 1613464462096",
                "goodFile rw 295Byte 1613464462079",
                "ffff rw 201Byte 1613464462090",
                "327854358439 rw 90Byte 1613464462082")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--output", "testFiles/output/out", "--long", "-h", "--reverse", "testFiles/input/newDir/"});
        assertFileContent("""
                gun rw 205Byte 1613464462096
                goodFile rw 295Byte 1613464462079
                ffff rw 201Byte 1613464462090
                327854358439 rw 90Byte 1613464462082
                """);

        LsLauncher.main(new String[]{"-l", "--human", "testFiles/input/"});
        assertEquals(new ArrayList<>(Arrays.asList(
                "abc rwx 0Byte 1614528915840",
                "lol rwx 0Byte 1613464462146",
                "newDir rwx 0Byte 1613464462096",
                "superDiiir rwx 0Byte 1613464462143",
                "test rw 0Byte 1614539129897",
                "testdir rwx 0Byte 1613538834100")), LsLauncher.getLss());


//        проверка ошибок, связанных с неизвестными флагами:
        LsLauncher.main(new String[]{"-l", "--huma", "testFiles/input/"});
        assertEquals(new ArrayList<>(Collections.singletonList("Please use the existing flags")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"SUPERFLAG", "testFiles/input/"});
        assertEquals(new ArrayList<>(Collections.singletonList("Please use the existing flags")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"--0010102", "testFiles/input/"});
        assertEquals(new ArrayList<>(Collections.singletonList("Please use the existing flags")), LsLauncher.getLss());

//        проверка ошибок, связанных с неправильными директориями
        LsLauncher.main(new String[]{"-l", "testFiles/input/SUPERDIRRRRRRRRRR"});
        assertEquals(new ArrayList<>(Collections.singletonList("There is no such directory")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"directoria33333"});
        assertEquals(new ArrayList<>(Collections.singletonList("There is no such directory")), LsLauncher.getLss());

        LsLauncher.main(new String[]{"strangeDir"});
        assertEquals(new ArrayList<>(Collections.singletonList("There is no such directory")), LsLauncher.getLss());
    }
}