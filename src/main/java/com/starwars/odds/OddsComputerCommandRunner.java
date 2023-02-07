package com.starwars.odds;

import com.starwars.odds.command.GiveMeTheOddsCommand;
import com.starwars.odds.configuration.SQLite.SQLiteDialect;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@TypeHint(types = {CommandLine.Mixin.class, SQLiteDialect.class})
@RequiredArgsConstructor
@Profile("!web")
@EnableAutoConfiguration
public class OddsComputerCommandRunner implements CommandLineRunner, ExitCodeGenerator {

    private final GiveMeTheOddsCommand giveMeTheOddsCommand;

    private int exitCode;

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(giveMeTheOddsCommand).execute(args);
        System.exit(exitCode);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
