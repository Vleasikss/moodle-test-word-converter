package org.example;

import org.example.collector.RawHtmlCollector;
import org.example.collector.RawMoodleTestHtmlReportCollector;
import org.example.writer.MoodleTestReportJsonWriter;
import org.junit.Test;
import scala.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class MoodleTestHtmlToStringConverterTest {

    @Test
    public void shouldConvertHtmlToStringCorrectly() {
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String str = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q272284.html"));
        MoodleTestReport collect = toStringConverter.collect(str);

        assertEquals(collect.tests().size(), 30);
    }

    @Test
    public void shouldWriteHtmlToTextFileCorrectly() {
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String str = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q272284.html"));
        MoodleTestReportJsonWriter writer = new MoodleTestReportJsonWriter(toStringConverter);
        Option<Path> write = writer.write(str, Paths.get(""));
        System.out.println(write.get());
    }

    @Test
    public void shouldWriteReport() {
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String str = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q272284.html"));
        MoodleTestReport collect = toStringConverter.collect(str);
    }


    private static String readFile(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String loadResource(String path) {
        return Objects.requireNonNull(MoodleTestHtmlToStringConverterTest.class.getResource(path)).getPath();
    }
}
