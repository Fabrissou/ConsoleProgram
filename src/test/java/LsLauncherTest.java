import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class LsLauncherTest {
    private void assertFileContent(String expectedContent) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("testFiles/output/out")));
        assertEquals(expectedContent, content);
    }

//    простой тест
    @Test
    public void simpleTest() {
        assertEquals(Arrays.asList(
                "abc",
                "lol",
                "newDir",
                "superDiiir",
                "test",
                "testdir"), new LsLauncher().launch("testFiles/input"));

        assertEquals((Arrays.asList(
                "abc",
                "keker",
                "names")), new LsLauncher().launch("testFiles/input/lol"));
    }

//    тесы флага -l
    @Test
    public void testL() {
        assertEquals(Arrays.asList(
                "abc 110 74600 1613464462100",
                "keker 110 96880 1613464462086",
                "names 110 2364 1613464462146"), new LsLauncher().launch("-l", "testFiles/input/lol"));

        assertEquals(Arrays.asList(
                "327854358439 110 720 1613464462082",
                "ffff 110 1610 1613464462090",
                "goodFile 110 2366 1613464462079",
                "gun 110 1643 1613464462096"), new LsLauncher().launch("--long", "testFiles/input/newDir"));

        assertEquals(Arrays.asList(
                "93939393993939393993 110 0 1613464443179",
                "haha 110 5134 1613538834100",
                "jobs 110 11837 1613464462148"), new LsLauncher().launch("--long", "testFiles/input/testdir"));
    }

//    тесты флага -h
    @Test
    public void testH() {
        assertEquals(Collections.singletonList("printer rw- 4Kb"), new LsLauncher().launch("-h", "testFiles/input/superDiiir"));

        assertEquals(Arrays.asList(
                "A rw- 0Byte",
                "B rw- 0Byte",
                "C rw- 0Byte",
                "D rw- 0Byte",
                "E rw- 0Byte"), new LsLauncher().launch("--human", "testFiles/input/abc"));

        assertEquals(Arrays.asList(
                "327854358439 rw- 90Byte",
                "ffff rw- 201Byte",
                "goodFile rw- 295Byte",
                "gun rw- 205Byte"), new LsLauncher().launch("-h", "testFiles/input/newDir"));
    }

//    тесы флага -r
    @Test
    public void testR() {
        assertEquals(Arrays.asList(
                "E", "D", "C", "B", "A"), new LsLauncher().launch("-r", "testFiles/input/abc"));

        assertEquals(Arrays.asList(
                "names", "keker", "abc"), new LsLauncher().launch("--reverse", "testFiles/input/lol"));
    }

//    тесты флага -o
    @Test
    public void testO() throws IOException {
        new LsLauncher().launch("-o", "testFiles/output/out", "testFiles/input");
        assertFileContent("""
                abc
                lol
                newDir
                superDiiir
                test
                testdir
                """);

        new LsLauncher().launch("--output", "testFiles/output/out", "testFiles/input/lol");
        assertFileContent("""
                abc
                keker
                names
                """);
    }

//    тесты одиночного файла
    @Test
    public void testSimple() throws IOException {
        LsLauncher.main(new String[]{"-l", "testFiles/input/lol/names"});
        assertEquals(Collections.singletonList("names 110 2364 1613464462146"), new LsLauncher().launch("-l", "testFiles/input/lol/names"));

        assertEquals(Collections.singletonList("keker rw- 11Kb"), new LsLauncher().launch("-h", "testFiles/input/lol/keker"));

        new LsLauncher().launch("--output", "testFiles/output/out", "testFiles/input/newDir/ffff");
        assertFileContent("ffff\n");
    }

//    смешанные тесты
    @Test
    public void tests() throws IOException {
        assertEquals(Arrays.asList(
                "gun rw- 205Byte 1613464462096",
                "goodFile rw- 295Byte 1613464462079",
                "ffff rw- 201Byte 1613464462090",
                "327854358439 rw- 90Byte 1613464462082"), new LsLauncher().launch("--long", "-h", "--reverse", "testFiles/input/newDir"));

        new LsLauncher().launch("--output", "testFiles/output/out", "--long", "-h", "--reverse", "testFiles/input/newDir/");
        assertFileContent("""
                gun rw- 205Byte 1613464462096
                goodFile rw- 295Byte 1613464462079
                ffff rw- 201Byte 1613464462090
                327854358439 rw- 90Byte 1613464462082
                """);

        assertEquals(Arrays.asList(
                "abc rwx 0Byte 1614528915840",
                "lol rwx 0Byte 1613464462146",
                "newDir rwx 0Byte 1613464462096",
                "superDiiir rwx 0Byte 1613464462143",
                "test rw- 0Byte 1614539129897",
                "testdir rwx 0Byte 1613538834100"), new LsLauncher().launch("-l", "--human", "testFiles/input/"));
    }

//    проверки ошибок
    @Test
    public void exceptTest() {
        assertEquals(Collections.singletonList("Please use the existing flags"), new LsLauncher().launch("-l", "--huma", "testFiles/input/"));

        assertEquals(Collections.singletonList("Please use the existing flags"), new LsLauncher().launch("SUPERFLAG", "testFiles/input/"));

        assertEquals(Collections.singletonList("Please use the existing flags"), new LsLauncher().launch("--0010102", "testFiles/input/"));

        assertEquals(Collections.singletonList("There is no such directory"), new LsLauncher().launch("-l", "testFiles/input/SUPERDIRRRRRRRRRR"));

        assertEquals(Collections.singletonList("There is no such directory"), new LsLauncher().launch("directoria33333"));

        assertEquals(Collections.singletonList("There is no such directory"), new LsLauncher().launch("strangeDir"));
    }
}