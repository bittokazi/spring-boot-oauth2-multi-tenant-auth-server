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
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Bitto Kazi
 */

public class SeedUserRole implements CustomTaskChange {

    private static final Logger logger = LoggerFactory.getLogger(SeedUserRole.class);

    private ResourceAccessor resourceAccessor;

    private String userFileName;

    private static final String ROLE_ = "ROLE_";

    public void setUserFileName(String userFileName) {
        this.userFileName = userFileName;
    }

    public void execute(Database database) throws CustomChangeException {
        JdbcConnection databaseConnection = (JdbcConnection) database.getConnection();
        try {
            logger.info(userFileName);
            InputStream stream = resourceAccessor.openStream(null, userFileName);
            Reader in = new InputStreamReader(stream);

            long roleId = 1L;
            for (String role : Arrays.asList(ROLE_ + "SUPER_ADMIN")) {
                String sql = "INSERT INTO role(id,name,title) VALUES(?,?,?)";
                PreparedStatement statement = databaseConnection.prepareStatement(sql);
                String title = role.toLowerCase();
                title = title.split("_")[1].substring(0, 1).toUpperCase() + title.split("_")[1].substring(1);
                if (role.equals(ROLE_ + "SUPER_ADMIN")) {
                    title = "Super Admin";
                }
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, role);
                statement.setString(3, title);
                statement.executeUpdate();
                statement.close();
                roleId++;
            }

            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in).getRecords().stream().collect(Collectors.toSet());
            long userId = 1L;
            for (CSVRecord record : records) {
                String sql = "INSERT INTO users (id,first_name,last_name,user_name,password,email,enabled,change_password,email_verified) VALUES(?,?,?,?,?,?,?,?,?)";

                PreparedStatement statement = databaseConnection.prepareStatement(sql);
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, record.get("firstName"));
                statement.setString(3, record.get("lastName"));
                statement.setString(4, record.get("username"));
                statement.setString(5, BCrypt.hashpw(record.get("password"), BCrypt.gensalt()));
                statement.setString(6, record.get("email"));
                statement.setBoolean(7, true);
                statement.setBoolean(8, false);
                statement.setBoolean(9, true);

                statement.executeUpdate();
                databaseConnection.commit();
                statement.close();

                for (String role : record.get("roles").split(",")) {
                    sql = "insert into user_role (user_id, role_id) VALUES (" + "(select id from users where email='"
                            + record.get("email") + "'), (select id from role where name='" + role + "') )";
                    logger.info(sql);
                    statement = databaseConnection.prepareStatement(sql);
                    statement.executeUpdate();
                    databaseConnection.commit();
                    statement.close();
                }
                userId++;
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
