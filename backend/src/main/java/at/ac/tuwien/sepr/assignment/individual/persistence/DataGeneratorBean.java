package at.ac.tuwien.sepr.assignment.individual.persistence;

import jakarta.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

/**
 * This component is only created, if the profile {@code datagen} is active
 * You can activate this profile by adding {@code -Dspring.profiles.active=datagen} to your maven command line
 */
@Component
@Profile("datagen")
public class DataGeneratorBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final DataSource dataSource;

  /**
   * Executed once when the component is instantiated. Inserts some dummy data.
   */
  public DataGeneratorBean(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostConstruct
  public void generateData() throws SQLException {
    LOGGER.info("Generating data…");
    try (var connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("sql/insertData.sql"));
      LOGGER.info("Finished generating data without error.");
    }
  }

  public void clearData() throws SQLException {
    LOGGER.info("Clearing data…");
    try (var connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, new ClassPathResource("sql/deleteData.sql"));
      LOGGER.info("Finished clearing data without error");
    }
  }

    public static interface TournamentDao {
    }
}
