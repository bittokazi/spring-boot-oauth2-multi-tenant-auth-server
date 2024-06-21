package com.bittokazi.oauth2.auth.server.database.seed;

import com.bittokazi.oauth2.auth.server.utils.Utils;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeedOauthClient implements CustomTaskChange {
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
                String sql = "INSERT INTO oauth_client_details (id,client_id,resource_ids,client_secret,scope,client_authentication_method,authorized_grant_types,web_server_redirect_uri,access_token_validity,refresh_token_validity,additional_information,require_consent,post_logout_url,token_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement statement = databaseConnection.prepareStatement(sql);
                statement.setString(1, record.get("id"));
                statement.setString(2, UUID.randomUUID().toString());
                statement.setString(3, record.get("resource_ids"));
                statement.setString(4, Utils.randomNumberGenerator(30));
                statement.setString(5, record.get("scope"));
                statement.setString(6, record.get("client_authentication_method"));
                statement.setString(7, record.get("authorized_grant_types"));
                statement.setString(8, record.get("web_server_redirect_uri"));
                statement.setInt(9, Integer.valueOf(record.get("access_token_validity")));
                statement.setInt(10, Integer.valueOf(record.get("refresh_token_validity")));
                statement.setString(11, record.get("additional_information"));
                statement.setBoolean(12, Boolean.valueOf(record.get("require_consent")));
                statement.setString(13, record.get("post_logout_url"));
                statement.setString(14, record.get("token_type"));

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
