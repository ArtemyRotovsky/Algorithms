import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseballElimination {
    private Integer source;
    private Integer target;

    private final Team[] teams;
    private SET<String> certificate;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        teams = getTeamsFromFile(filename);
    }

    // number of teams
    public int numberOfTeams() {
        return teams.length;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.stream(teams)
                .map(Team::getName)
                .collect(Collectors.toList());
    }

    // number of wins for given team
    public int wins(String team) {
        return getTeam(team)
                .getWins();
    }

    // number of losses for given team
    public int losses(String team) {
        return getTeam(team)
                .getLoses();
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return getTeam(team)
                .getRemaining();
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        Team teamOne = getTeam(team1);
        int teamPosition = getTeamPosition(team2);
        return teamOne.gamesWithTeams[teamPosition];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        certificate = null;
        return isTriviallyEliminated(team) || isNonTriviallyEliminated(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return certificate;
    }

    private boolean isTriviallyEliminated(String team) {
        Team curTeam = getTeam(team);

        Team topTeam  = Arrays.stream(teams)
                .max(Comparator.comparingInt(Team::getWins))
                .orElseThrow(() -> new IllegalArgumentException("Not posssible to find top team"));

        boolean isEliminated = (curTeam.getWins() + curTeam.getRemaining() - topTeam.getWins()) < 0;
        if (isEliminated) {
            createTrivialCertificateOfElimination(topTeam);
        }

        return isEliminated;
    }

    private boolean isNonTriviallyEliminated(String teamName) {
        int numberOfTeams = numberOfTeams();
        int numberOfGames = ((numberOfTeams - 1)
                * (numberOfTeams - 2)) / 2;
        int numberOfFlowNetworkVertices = 1 // source
                + numberOfGames
                + numberOfTeams - 1
                + 1;                        // target
        source = numberOfFlowNetworkVertices - 2;
        target = numberOfFlowNetworkVertices - 1;

        // build vertexes numbers for teams -- they should be changed since we build a graph without a team validated
        int teamToCheckPosition = getTeamPosition(teamName);
        Map<Integer, Integer> teamVertexIndices = getTeamVertexIndices(teamToCheckPosition);

        // since graph doesnt' allow random vertex numbers, games vertices will have numbers from numberOfTeams - 1
        // (-1 because we not include teamToCheck in the graph) to source vertex number
        int gameVertexIndex = numberOfTeams - 1;
        Team teamToCheck = getTeam(teamName);
        FlowNetwork flowNetwork = new FlowNetwork(numberOfFlowNetworkVertices);

        for (int i = 0; i < numberOfTeams; i++) {
            // first/second team for game vertex, e.g. for (0-1), the firstTeam is 0, the second is 1
            Team curTeam = teams[i];
            if (teamToCheck.equals(curTeam)) {
                continue;
            }
            int firstTeamIndex = teamVertexIndices.get(i);
            int[] remainingGamesWithTeams = curTeam.getGamesWithTeams();

            // since matrix(reamining games table) is symmetric, will walk through only top right triangle
            for (int j = i + 1; j < remainingGamesWithTeams.length; j++) {
                if (j == teamToCheckPosition) {
                    continue;
                }
                int secondTeamIndex = teamVertexIndices.get(j);
                int remainingGamesCount = remainingGamesWithTeams[j];

                // add edges from the source to games
                flowNetwork.addEdge(new FlowEdge(source, gameVertexIndex, remainingGamesCount));

                // add edges from games to teams
                flowNetwork.addEdge(new FlowEdge(gameVertexIndex, firstTeamIndex, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameVertexIndex, secondTeamIndex, Double.POSITIVE_INFINITY));
                gameVertexIndex++;
            }

            // add edges from teams to the target
            int firstTeamCapacity = teamToCheck.getWins() + teamToCheck.getRemaining() - curTeam.getWins();
            flowNetwork.addEdge(new FlowEdge(firstTeamIndex, target, firstTeamCapacity));

        }

        // check if all edges from source to games have full flow
        FordFulkerson maxFlow = new FordFulkerson(flowNetwork, source, target);
        if (allEdgesFromSourceAreFull(flowNetwork)) {
            createNonTrivialCertificateOfElimination(maxFlow, teamVertexIndices);
            return true;
        }

        return false;
    }

    private boolean allEdgesFromSourceAreFull(FlowNetwork flowNetwork) {
        for(FlowEdge e : flowNetwork.adj(source)) {
            if (e.flow() != e.capacity()) {
                return true;
            }
        }
        return false;
    }

    private Map<Integer, Integer> getTeamVertexIndices(int teamToCheckPosition) {
        Map<Integer, Integer> teamVertexIndices = new HashMap<>();
        int teamVertexIndex = 0;
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamToCheckPosition) {
                continue;
            }
            teamVertexIndices.put(i, teamVertexIndex++);
        }

        return teamVertexIndices;
    }

    private Team[] getTeamsFromFile(String filename) {
        In in = new In(filename);

        int numberOfTeams = in.readInt();

        return Arrays.stream(in.readAllLines())
                .filter(line -> !line.trim().isEmpty())
                .map(line -> this.lineToTeam(line, numberOfTeams))
                .toArray(Team[]::new);
    }

    private Team lineToTeam(String s, int numberOfTeams) {
        String[] line = s.trim().split("\\s+");
        Team team = new Team();
        team.setName(line[0]);
        team.setWins(Integer.parseInt(line[1]));
        team.setLoses(Integer.parseInt(line[2]));
        team.setRemaining(Integer.parseInt(line[3]));

        int[] gamesWithTeams = new int[numberOfTeams];
        for (int i = 4; i < line.length; i++) {
            gamesWithTeams[i - 4] = Integer.parseInt(line[i]);
        }
        team.setGamesWithTeams(gamesWithTeams);

        return team;
    }

    private Team getTeam(String team) {
        return Arrays.stream(teams)
                .filter(t -> t.getName().equals(team))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Team \"%s\" is not found", team)));
    }

    private int getTeamPosition(String team) {
        return IntStream
                .range(0, teams.length)
                .filter(index -> teams[index].getName().equals(team))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Position for the team \"%s\" is not found", team)));
    }

    private void createNonTrivialCertificateOfElimination(FordFulkerson maxFlow, Map<Integer, Integer> teamVertexIndices) {
        certificate = new SET<>();
        for (int i = 0; i < numberOfTeams(); i++) {
            if (!teamVertexIndices.containsKey(i)) {
                continue;
            }
            if (maxFlow.inCut(teamVertexIndices.get(i))) {
                certificate.add(teams[i].getName());
            }
        }
    }

    private void createTrivialCertificateOfElimination(Team topTeam) {
        certificate = new SET<>();
        certificate.add(topTeam.getName());
    }

    private class Team {
        private String name;
        private int wins;
        private int loses;
        private int remaining;
        private int[] gamesWithTeams;

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        int getWins() {
            return wins;
        }

        void setWins(int wins) {
            this.wins = wins;
        }

        int getLoses() {
            return loses;
        }

        void setLoses(int loses) {
            this.loses = loses;
        }

        int getRemaining() {
            return remaining;
        }

        void setRemaining(int remaining) {
            this.remaining = remaining;
        }

        int[] getGamesWithTeams() {
            return gamesWithTeams;
        }

        void setGamesWithTeams(int[] gamesWithTeams) {
            this.gamesWithTeams = gamesWithTeams;
        }

    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("BaseballElimination/src/main/resources/teams29.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
