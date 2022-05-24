package org.example;

import org.example.collector.RawHtmlCollector;
import org.example.collector.RawMoodleTestHtmlReportCollector;
import org.example.writer.MoodleTestReportWordWriter;
import org.junit.Test;
import scala.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MoodleTestReportWordFileWriterTest {


    @Test
    public void shouldWriteToWordFileCorrectly() {
        MoodleTestReportWordWriter writer = new MoodleTestReportWordWriter(new RawMoodleTestHtmlReportCollector());
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String rawHtml = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q272284.html"));
        writer.write(rawHtml, Paths.get(""));
    }

    @Test
    public void shouldWriteToWordq251633Correctly() {
        MoodleTestReportWordWriter writer = new MoodleTestReportWordWriter(new RawMoodleTestHtmlReportCollector());
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String rawHtml = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q251633.html"));
        Option<Path> write = writer.write(rawHtml, Paths.get(""));

    }

    @Test
    public void shouldWriteToWordFileCorrectly2() {
        MoodleTestReportWordWriter writer = new MoodleTestReportWordWriter(new RawMoodleTestHtmlReportCollector());
        RawHtmlCollector<MoodleTestReport> toStringConverter = new RawMoodleTestHtmlReportCollector();
        String rawHtml = readFile(loadResource("/test-moodle-page/moodle-test-assignment-result-full-q227987.html"));
        writer.write(rawHtml, Paths.get(""));
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

