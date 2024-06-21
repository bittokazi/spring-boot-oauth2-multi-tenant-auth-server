package com.bittokazi.oauth2.auth.server.database.seed;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.util.Set;
import java.util.stream.Collectors;

public class SeedCompany implements CustomTaskChange {
    private static final Logger logger = LoggerFactory.getLogger(SeedOauthClient.class);

    private ResourceAccessor resourceAccessor;

    private String clientFileName;

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public void execute(Database database) throws CustomChangeException {
        JdbcConnection databaseConnection = (JdbcConnection) database.getConnection();
        try {
            logger.info(clientFileName);
            InputStream stream = resourceAccessor.openStream(null, clientFileName);
            Reader in = new InputStreamReader(stream);

            Set<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in).getRecords().stream().collect(Collectors.toSet());
            for (CSVRecord record : records) {
                String sql = "INSERT INTO tenant (id,company_key,enabled,name,domain) VALUES(?,?,?,?,?)";

                PreparedStatement statement = databaseConnection.prepareStatement(sql);
                statement.setString(1, record.get("id"));
                statement.setString(2, record.get("company_key"));
                statement.setBoolean(3, Boolean.valueOf(record.get("enabled")));
                statement.setString(4, record.get("name"));
                statement.setString(5, record.get("domain"));



                statement.executeUpdate();
                databaseConnection.commit();
                statement.close();
            }
            in.close();
        } catch (Exception e) {
            throw new CustomChangeException(e);
        }
    }

    public String getConfirmationMessage() {
        return null;
    }

    public void setUp() throws SetupException {
    }

    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

    public ValidationErrors validate(Database database) {
        return null;
    }
}
