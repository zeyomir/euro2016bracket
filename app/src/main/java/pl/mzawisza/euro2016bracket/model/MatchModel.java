package pl.mzawisza.euro2016bracket.model;

public class MatchModel {
    public final String teamA;
    public final String teamB;
    public final int teamAScore;
    public final int teamBScore;


    public MatchModel(String teamA, String teamB, int teamAScore, int teamBScore) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamAScore = teamAScore;
        this.teamBScore = teamBScore;
    }
}
