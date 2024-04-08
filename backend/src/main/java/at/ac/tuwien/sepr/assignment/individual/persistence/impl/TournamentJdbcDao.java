package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Standing;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class TournamentJdbcDao implements TournamentDao {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TABLE_NAME = "tournament";

    private static final String STANDING_TABLE_NAME = "standings";

    private static final String TABLE_COLUMNS = "(name, start_date, end_date)";


    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

    private static final String SQL_SELECT_SEARCH = "SELECT "
            + "    t.id as \"id\", t.name as \"name\", t.start_date as \"start_date\", t.end_date as \"end_date\""
            + " FROM " + TABLE_NAME + " t"
            + " WHERE "
            + " (:name IS NULL OR UPPER(t.name) LIKE UPPER('%'||:name||'%'))"
            + " AND ("
            + "   (:startDate IS NULL OR t.end_date >= :startDate)"
            + "   AND (:endDate IS NULL OR t.start_date <= :endDate)"
            + " )"
            + " ORDER BY t.start_date DESC";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + TABLE_COLUMNS + " VALUES (?, ?, ?)";

    private static final String SQL_INSERT_PARTICIPATING_HORSES = "INSERT INTO tournament_horses (tournament_id, horse_id) VALUES (?, ?)";


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcNamed;


    //constructor
    public TournamentJdbcDao(NamedParameterJdbcTemplate jdbcNamed, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcNamed = jdbcNamed;
    }


    @Override
    public Collection<Tournament> search(TournamentSearchDto searchParameters) {
        LOG.trace("search({})", searchParameters);
        var query = SQL_SELECT_SEARCH;
        var params = new BeanPropertySqlParameterSource(searchParameters);
        LOG.info("params is {}", params);

        return jdbcNamed.query(query, params, this::mapRow);
    }

    @Override
    public Tournament create(TournamentCreateDto tournament) throws NotFoundException {
        LOG.trace("create({})", tournament);
        String sql = "INSERT INTO " + TABLE_NAME + TABLE_COLUMNS + " VALUES (?, ?, ?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, tournament.name());
            preparedStatement.setObject(2, tournament.startDate());
            preparedStatement.setObject(3, tournament.endDate());
            return preparedStatement;
        }, generatedKeyHolder);

        Long tournamentId = generatedKeyHolder.getKey().longValue();

        if (tournamentId == 0) {
            throw new FatalException("Error getting newly created tournament ID");
        }

        createTournamentStanding(tournament, tournamentId);

        return getById(tournamentId);
    }

    public void createTournamentStanding(TournamentCreateDto tournament, Long tournamentId) {
        LOG.trace("createTournamentStanding({}, {})", tournament, tournamentId);
        String sql = "INSERT INTO " + STANDING_TABLE_NAME + " (tournament_id, horse_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, tournamentId);
                ps.setLong(2, tournament.participants()[i].id());
            }

            @Override
            public int getBatchSize() {
                return 8;
            }
        });
    }

    @Override
    public Tournament getById(long tournamentId) throws NotFoundException {
        LOG.trace("getById({})", tournamentId);

        List<Tournament> tournaments = jdbcTemplate.query(SQL_SELECT_BY_ID, new Object[]{tournamentId}, this::mapRow);

        if (tournaments.isEmpty()) {
            throw new NotFoundException("No tournament with ID %d found".formatted(tournamentId));
        }else if (tournaments.size() > 1) {
            throw new FatalException("Too many tournaments with ID %d found".formatted(tournamentId));
        }
        return tournaments.get(0);
    }

    @Override
    public Standing getStandingById(long tournamentId, long horseId) throws NotFoundException {
        LOG.trace("getStandingById({}, {})", tournamentId, horseId);
        String query = "SELECT * FROM " + STANDING_TABLE_NAME + " WHERE tournament_id = ? AND horse_id = ?";
        List<Standing> standings = jdbcTemplate.query(query, new Object[]{tournamentId, horseId}, this::mapRowStanding);

        if (standings.isEmpty()) {
            throw new NotFoundException("No tournament standing with tournament ID %d and horse ID %d found".formatted(tournamentId, horseId));
        }
        if (standings.size() > 1) {
            throw new FatalException("Too many tournaments standings with tournament ID %d and horse ID %d found".formatted(tournamentId, horseId));
        }

        return standings.get(0);
    }

    @Override
    public List<Long> getStandingIds(long tournamentId) throws NotFoundException {
        LOG.trace("getStandingIds({})", tournamentId);
        String query = "SELECT * FROM " + STANDING_TABLE_NAME + " WHERE tournament_id = ?";
        List<Long> resultIds = jdbcTemplate.query(query, new Object[]{tournamentId}, (rs, rowNum) -> rs.getLong("horse_id"));

        if (resultIds.isEmpty()) {
            throw new NotFoundException("No tournament standings with tournament ID %d found".formatted(tournamentId));
        }

        return resultIds;
    }

    @Override
    public List<Long> getAllTournamentIdsByHorseId(long horseId) {
        LOG.trace("getAllTournamentIdsByHorseId({})", horseId);
        String query = "SELECT tournament_id FROM " + STANDING_TABLE_NAME + " WHERE horse_id = ?";
        return jdbcTemplate.query(query, new Object[]{horseId}, (rs, rowNum) -> rs.getLong("tournament_id"));
    }


    private Tournament mapRow(ResultSet result, int rownum) throws SQLException {
        LOG.trace("mapRow({}, {})", result, rownum);
        return new Tournament()
                .setId(result.getLong("id"))
                .setName(result.getString("name"))
                .setStartDate(result.getDate("start_date").toLocalDate())
                .setEndDate(result.getDate("end_date").toLocalDate())
                ;
    }

    private Standing mapRowStanding(ResultSet result, int rowNum) throws SQLException {
        LOG.trace("mapRowStanding({}, {})", result, rowNum);
        return new Standing()
                .setTournamentId(result.getLong("tournament_Id"))
                .setHorseId(result.getLong("horse_Id"))
                .setEntryNumber(result.getLong("entry_number"))
                .setRoundReached(result.getLong("round_reached"));
    }
}
