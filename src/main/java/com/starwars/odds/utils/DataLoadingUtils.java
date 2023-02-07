package com.starwars.odds.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starwars.odds.model.dto.MillenniumFalconDto;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class DataLoadingUtils {

    public static  <T> T loadDataFromFile(File file, Class<T> clazz) throws IOException {
        if (file.exists()) {
            return new ObjectMapper().readValue(file, clazz);
        } else {
            throw new FileNotFoundException("File not found: " + file.getName());
        }
    }

    public static void loadUniverseDBFile(DriverManagerDataSource dataSource,
                                          File millenniumFalconFile,
                                          MillenniumFalconDto millenniumFalconDto) throws FileNotFoundException {
        var absolutePath = millenniumFalconDto.getRoutesDb();
        var relativePath = absolutePath + "/" + millenniumFalconFile.getAbsoluteFile().getParent();

        setUrl(dataSource, absolutePath, relativePath, millenniumFalconDto.getRoutesDb());
    }

    public static void setUrl(DriverManagerDataSource dataSource,
                               String absolutePath,
                               String relativePath,
                               String dbFile) throws FileNotFoundException {
        if (new File(absolutePath).exists()) {
            dataSource.setUrl(String.format("jdbc:sqlite:%s", absolutePath));
        } else if (new File(relativePath).exists()) {
            dataSource.setUrl(String.format("jdbc:sqlite:%s", relativePath));
        } else {
            throw new FileNotFoundException("Database file not found: " + dbFile);
        }
    }
}
