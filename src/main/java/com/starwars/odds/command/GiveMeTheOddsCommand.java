package com.starwars.odds.command;

import com.starwars.odds.model.dto.EmpireDto;
import com.starwars.odds.model.dto.MillenniumFalconDto;
import com.starwars.odds.service.OddsService;
import com.starwars.odds.utils.DataLoadingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@Component
@Profile("!web")
@RequiredArgsConstructor
@CommandLine.Command(
        name = "give-me-the-odds",
        exitCodeOnInvalidInput = 74,
        mixinStandardHelpOptions = true,
        version = "give-me-the-odds 1.0",
        description = "Prints the odds of the millennium falcon")
public class GiveMeTheOddsCommand implements Callable<Integer> {

    private final DriverManagerDataSource dataSource;

    private final OddsService oddsService;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Parameters(index = "0", arity = "1",
            description = "Configuration file containing the properties of the spaceship")
    private File millenniumFalconFile;

    @CommandLine.Parameters(index = "1", arity = "1",
            description = "The file containing the information about the empire")
    private File empireFile;


    @Override
    public Integer call() throws Exception {
        var empireData = DataLoadingUtils.loadDataFromFile(empireFile, EmpireDto.class);
        var millenniumFalconData = DataLoadingUtils.loadDataFromFile(millenniumFalconFile, MillenniumFalconDto.class);
        DataLoadingUtils.loadUniverseDBFile(dataSource, millenniumFalconFile, millenniumFalconData);
        var result = oddsService.compute(empireData, millenniumFalconData);
        spec.commandLine().getOut().println(result);
        return 0;
    }
}
