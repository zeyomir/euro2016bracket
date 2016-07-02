package pl.mzawisza.euro2016bracket.view;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.mzawisza.euro2016bracket.R;
import pl.mzawisza.euro2016bracket.model.MatchModel;
import pl.mzawisza.euro2016bracket.model.TeamModel;
import pl.mzawisza.euro2016bracket.view.custom.EuroBracketView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        setUpBracket();
    }

    private void setUpBracket() {
        EuroBracketView.Settings settings =
                new EuroBracketView.SettingsBuilder()
                        .withTeams(setUpTeams())
                        .withFirstDay(setUpFirstDay())
                        .withSecondDay(setUpSecondDay())
                        .withThirdDay(setUpThirdDay())
                        .build();

        EuroBracketView bracket = (EuroBracketView) findViewById(R.id.bracket);

        bracket.setSettings(settings);
    }

    private Map<String, TeamModel> setUpTeams() {
        Map<String, TeamModel> teams = new HashMap<>(16);

        teams.put("Poland", new TeamModel("Poland", "https://upload.wikimedia.org/wikipedia/en/1/12/Flag_of_Poland.svg"));
        teams.put("France", new TeamModel("France", "https://upload.wikimedia.org/wikipedia/en/c/c3/Flag_of_France.svg"));
        teams.put("Switzerland", new TeamModel("Switzerland", "https://upload.wikimedia.org/wikipedia/commons/0/08/Flag_of_Switzerland_(Pantone).svg"));
        teams.put("Wales", new TeamModel("Romania", "https://upload.wikimedia.org/wikipedia/commons/5/59/Flag_of_Wales_2.svg"));
        teams.put("Northern Ireland", new TeamModel("Northern Ireland", "https://upload.wikimedia.org/wikipedia/commons/f/f6/Flag_of_Northern_Ireland.svg"));
        teams.put("Croatia", new TeamModel("Croatia", "https://upload.wikimedia.org/wikipedia/commons/1/1b/Flag_of_Croatia.svg"));
        teams.put("Portugal", new TeamModel("Portugal", "https://upload.wikimedia.org/wikipedia/commons/5/5c/Flag_of_Portugal.svg"));
        teams.put("Republic of Ireland", new TeamModel("Republic of Ireland", "https://upload.wikimedia.org/wikipedia/commons/4/45/Flag_of_Ireland.svg"));
        teams.put("Germany", new TeamModel("Germany", "https://upload.wikimedia.org/wikipedia/en/b/ba/Flag_of_Germany.svg"));
        teams.put("Slovakia", new TeamModel("Slovakia", "https://upload.wikimedia.org/wikipedia/commons/e/e6/Flag_of_Slovakia.svg"));
        teams.put("Hungary", new TeamModel("Hungary", "https://upload.wikimedia.org/wikipedia/commons/c/c1/Flag_of_Hungary.svg"));
        teams.put("Belgium", new TeamModel("Belgium", "https://upload.wikimedia.org/wikipedia/commons/6/65/Flag_of_Belgium.svg"));
        teams.put("Italy", new TeamModel("Italy", "https://upload.wikimedia.org/wikipedia/en/0/03/Flag_of_Italy.svg"));
        teams.put("Spain", new TeamModel("Spain", "https://upload.wikimedia.org/wikipedia/en/9/9a/Flag_of_Spain.svg"));
        teams.put("England", new TeamModel("England", "https://upload.wikimedia.org/wikipedia/en/b/be/Flag_of_England.svg"));
        teams.put("Iceland", new TeamModel("Iceland", "https://upload.wikimedia.org/wikipedia/commons/c/ce/Flag_of_Iceland.svg"));

        return teams;
    }

    private List<MatchModel> setUpFirstDay() {
        List<MatchModel> matches = new ArrayList<>(8);

        matches.add(new MatchModel("Switzerland", "Poland", 5, 6));
        matches.add(new MatchModel("Croatia", "Portugal", 0, 1));
        matches.add(new MatchModel("Wales", "Northern Ireland", 1, 0));
        matches.add(new MatchModel("Hungary", "Belgium", 0, 4));
        matches.add(new MatchModel("Germany", "Slovakia", 3, 0));
        matches.add(new MatchModel("Italy", "Spain", 2, 0));
        matches.add(new MatchModel("France", "Republic of Ireland", 2, 1));
        matches.add(new MatchModel("England", "Iceland", 1, 2));

        return matches;
    }

    private List<MatchModel> setUpSecondDay() {
        List<MatchModel> matches = new ArrayList<>(4);

        matches.add(new MatchModel("Poland", "Portugal", 5, 6));
        matches.add(new MatchModel("Wales", "Belgium", 3, 1));
        matches.add(new MatchModel("Germany", "Italy", 0, 0));
        matches.add(new MatchModel("France", "Iceland", 0, 0));

        return matches;
    }

    private List<MatchModel> setUpThirdDay() {
        List<MatchModel> matches = new ArrayList<>(2);

        matches.add(new MatchModel("Portugal", "Wales", 0, 0));
        matches.add(new MatchModel(null, "Iceland", 0, 0));

        return matches;
    }
}

