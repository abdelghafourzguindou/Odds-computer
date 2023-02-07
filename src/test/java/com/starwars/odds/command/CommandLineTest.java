package com.starwars.odds.command;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ContextConfiguration(classes = GiveMeTheOddsCommand.class)
class CommandLineTest {

    @MockBean
    GiveMeTheOddsCommand giveMeTheOddsCommand;

    @Test
    void commandLineExecutionTest() {
        var millenniumFalconFilePath = "src/test/resources/millennium-falcon.json";
        var empireFilePath = "src/test/resources/empire.json";
        var exitCode = new CommandLine(giveMeTheOddsCommand).execute(millenniumFalconFilePath,  empireFilePath);
        assertEquals(0, exitCode);
    }

    @Test
    void helpCommandTest() {
        var expectedHelpMessage = "Usage: give-me-the-odds [-hV] <millenniumFalconFile> <empireFile>\n" +
                "Prints the odds of the millennium falcon\n" +
                "      <millenniumFalconFile>\n" +
                "                     Configuration file containing the properties of the\n" +
                "                       spaceship\n" +
                "      <empireFile>   The file containing the information about the empire\n" +
                "  -h, --help         Show this help message and exit.\n" +
                "  -V, --version      Print version information and exit.\n";
        var actualHelpMessage = new CommandLine(giveMeTheOddsCommand).getUsageMessage(CommandLine.Help.Ansi.OFF);
        assertEquals(expectedHelpMessage, actualHelpMessage);
    }

    @Test
    void commandLineArgsTest() {
        var arg1 = "millennium-falcon.json";
        var arg2 = "empire.json";
        CommandLine.ParseResult parseResult = new CommandLine(giveMeTheOddsCommand)
                .parseArgs(arg1,  arg2);
        assertEquals(arg1, parseResult.originalArgs().get(0));
        assertEquals(arg2, parseResult.originalArgs().get(1));
    }
}
