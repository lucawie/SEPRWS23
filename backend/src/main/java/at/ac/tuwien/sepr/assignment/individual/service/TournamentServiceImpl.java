package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TournamentServiceImpl implements TournamentService{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TournamentDao dao;

    private final HorseDao horseDao;
    private final TournamentMapper mapper;

    private final TournamentValidator validator;

    public TournamentServiceImpl(TournamentDao dao, HorseDao horseDao, TournamentMapper mapper, TournamentValidator validator) {
        this.dao = dao;
        this.horseDao = horseDao;
        this.mapper = mapper;
        this.validator = validator;
    }


    @Override
    public Stream<TournamentListDto> search(TournamentSearchDto searchParameters) {
        LOG.trace("search({})", searchParameters);
        var tournaments = dao.search(searchParameters);

        return tournaments.stream()
                .map(tournament -> mapper.entityToListDto(tournament));
    }

    @Override
    public TournamentDetailDto create(TournamentCreateDto tournamentCreateDto) throws NotFoundException, ValidationException {
        LOG.trace("create({})", tournamentCreateDto);
        List<Long> allHorseIds = horseDao.getAllHorseIDs();
        validator.validateCreate(tournamentCreateDto, allHorseIds);

        Tournament tournament = dao.create(tournamentCreateDto);
        return mapper.entityToDetailDto(tournament, getDetailParticipantList(tournament.getId()));
    }

    @Override
    public TournamentDetailDto getTournamentDetailById(long id) throws NotFoundException, ValidationException {
        LOG.trace("getTournamentDetailById({})", id);
        validator.validateForID(id);

        return mapper.entityToDetailDto(dao.getById(id), getDetailParticipantList(id));
    }

    @Override
    public TournamentStandingsDto getTournamentStandingById(long id) throws NotFoundException, ValidationException {
        LOG.trace("getTournamentStandingById({})", id);
        validator.validateForID(id);

        TournamentDetailDto tournamentDetailDto = mapper.entityToDetailDto(dao.getById(id), getDetailParticipantList(id));
        return mapper.detailToStandingDto(tournamentDetailDto);
    }

    /**
     * Get all participants of a tournament with given {@param tournamentId}
     *
     * @param tournamentId ID of tournament to get participating horses
     * @return array of all participants in the tournament
     * @throws NotFoundException if no tournament with ID {@param tournamentId} exists
     */
    private TournamentDetailParticipantDto[] getDetailParticipantList(Long tournamentId) throws NotFoundException, ValidationException {
        LOG.trace("getDetailParticipantList({})", tournamentId);
        validator.validateForID(tournamentId);

        var horseIdList = dao.getStandingIds(tournamentId);
        var participants = new TournamentDetailParticipantDto[8];

        for (int i = 0; i < horseIdList.size(); i++) {
            participants[i] = new TournamentDetailParticipantDto(
                    dao.getStandingById(tournamentId, horseIdList.get(i)).getHorseId(),
                    horseDao.getById(horseIdList.get(i)).getName(),
                    horseDao.getById(horseIdList.get(i)).getDateOfBirth(),
                    dao.getStandingById(tournamentId, horseIdList.get(i)).getEntryNumber(),
                    dao.getStandingById(tournamentId, horseIdList.get(i)).getRoundReached()
            );
        }
        return participants;
    }
}
