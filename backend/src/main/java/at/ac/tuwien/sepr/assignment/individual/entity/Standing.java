package at.ac.tuwien.sepr.assignment.individual.entity;

/**
 * Represents a standing in the persistent data store.
 */

public class Standing {

    private long tournamentId;
    private long horseId;
    private long entryNumber;
    private long roundReached;

    public long getTournamentId() {
        return tournamentId;
    }

    public Standing setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
        return this;
    }

    public long getHorseId() {
        return horseId;
    }

    public Standing setHorseId(long horseId) {
        this.horseId = horseId;
        return this;
    }

    public long getEntryNumber() {
        return entryNumber;
    }

    public Standing setEntryNumber(long entryNumber) {
        this.entryNumber = entryNumber;
        return this;
    }

    public long getRoundReached() {
        return roundReached;
    }

    public Standing setRoundReached(long roundReached) {
        this.roundReached = roundReached;
        return this;
    }

    @Override
    public String toString() {
        return "Result{"
                + "tId=" + tournamentId
                + ", hId=" + horseId
                + ", entryNumber=" + entryNumber
                + ", roundReached=" + roundReached
                + '}';
    }
}
