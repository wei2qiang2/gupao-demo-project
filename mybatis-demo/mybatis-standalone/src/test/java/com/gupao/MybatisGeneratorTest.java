package com.gupao;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorTest {

    @Test
    public void generateEntityAndMapper() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {


        String path = MybatisGeneratorTest.class.getClassLoader().getResource("generator-config.xml").getPath();
        File configFile = new File(path);
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser configurationParser = new ConfigurationParser(warnings);
        Configuration configuration = configurationParser.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator generator = new MyBatisGenerator(configuration, callback, warnings);
        generator.generate(null);
    }
}
