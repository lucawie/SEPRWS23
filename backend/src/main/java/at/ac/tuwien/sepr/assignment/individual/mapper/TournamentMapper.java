package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * A class representing a mapper for converting tournaments to Dtos and vice versa
 */

@Component
public class TournamentMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Convert a tournament entity object to a {@link at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto}.
     *
     * @param tournament the horse to convert
     * @return the converted {@link TournamentListDto}
     */
    public TournamentListDto entityToListDto(Tournament tournament) {
        LOG.trace("entityToListDto({})", tournament);
        if (tournament == null) {
            return null;
        }

        return new TournamentListDto(
                tournament.getId(),
                tournament.getName(),
                tournament.getStartDate(),
                tournament.getEndDate()
        );
    }

    /**
     * Convert a tournament entity object to a {@link TournamentDetailDto}.
     *
     * @param tournament to convert
     * @param participants of the tournament entity to convert
     * @return the converted {@link TournamentDetailDto}
     */
    public TournamentDetailDto entityToDetailDto(Tournament tournament, TournamentDetailParticipantDto[] participants) {
        LOG.trace("entityToDetailDto({}, {})", tournament, participants);
        if (tournament == null) {
            return null;
        }

        return new TournamentDetailDto(
                tournament.getId(),
                tournament.getName(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                participants
        );
    }

    /**
     * Convert a TournamentDetailDto to a {@link TournamentStandingsDto}
     *
     * @param tournament to convert
     * @return the converted {@link TournamentStandingsDto}
     */
    public TournamentStandingsDto detailToStandingDto(TournamentDetailDto tournament) {
        LOG.trace("detailToStandingDto({})", tournament);
        if (tournament == null) {
            return null;
        }

        return new TournamentStandingsDto(
                tournament.id(),
                tournament.name(),
                tournament.participants(),
                null
        );
    }

}
