package com.ufcg.psoft.scrumboard.model.report;

import com.ufcg.psoft.scrumboard.model.UserStory;

import java.util.*;

public class ScrumMasterReport extends ReportModel {

    private final Map<String, List<UserStory>> mapUserStoryByUser;
    private final Collection<UserStory> listUserStories;
    private final Set<String> usersIdOfProject;

    public static class Builder extends ReportModel.Builder<Builder> {

        private final Map<String, List<UserStory>> mapUserStoryByUser;
        private final Collection<UserStory> listUserStories;
        private final Set<String> usersIdOfProject;

        public Builder(Map<String, List<UserStory>> mapUserStoryByUser, Collection<UserStory> listUserStories, Set<String> usersIdOfProject){
            this.mapUserStoryByUser = mapUserStoryByUser;
            this.listUserStories = listUserStories;
            this.usersIdOfProject = usersIdOfProject;
        }

        @Override public ScrumMasterReport build() {
            return new ScrumMasterReport(this);
        }
        @Override protected Builder self() { return this; }
    }
    private ScrumMasterReport(Builder builder) {
        super(builder);
        mapUserStoryByUser = builder.mapUserStoryByUser;
        listUserStories = builder.listUserStories;
        usersIdOfProject = builder.usersIdOfProject;
    }

    @Override
    public String getReport() {

        String header = super.getReport();
        String firstChapter, secondChapter, thirdChapter;
        String msgAux, msgAux1, msgAux2, msgAux3, msgAux4, msgAux5, msgAux6, msgAux7;
        msgAux = msgAux4 = msgAux5 = msgAux6 = msgAux7 = "";

        Map<String, List<UserStory>> map = this.mapUserStoryByUser;
        Collection<UserStory> list = this.listUserStories;
        List<UserStory> listToMap = super.verifyList(list);
        Map<String, List<UserStory>> mapUserStoryByState = super.mapStateAndUserStories(listToMap);

        int totalOfUserStories = list.size();

        msgAux1 = super.userAndUserStories(map);
        msgAux2 = super.userStoryByState(list);
        msgAux3 = super.userByStateAndUserStories(map);
        msgAux4 = super.resumeUserStoryByUser(map, totalOfUserStories);
        msgAux5 = super.resumeUserStoryByState(mapUserStoryByState, totalOfUserStories);
        msgAux6 = super.resumeUserStoryByUserState(map,totalOfUserStories);
        msgAux7 = super.listOfUserWithoutUserStories(map.keySet(), this.usersIdOfProject);

        firstChapter  = "\n" +
                "{\n" + " \"Users\": [\n"
                        + msgAux1 + "\n  ]\n" +
                        "\n" +
                        "}";

        secondChapter = "\n" +
                "{\n" + " \"States\": [\n"
                        + msgAux2 + "\n  ]\n" +
                        "\n" +
                        "}";

        thirdChapter = "\n" +
                "{\n" + " \"User divided by State\": [\n"
                        + msgAux3 + "\n  ]\n" +
                        "\n" +
                        "}";

        String body =
                "{\n" + " \"Resume by User\": [\n"
                        + msgAux4 + "\n ]\n" +
                        "\n" +
                        "}";

        String body1 =
                "{\n" + " \"Resume by State\": [\n"
                        + msgAux5 + "\n ]\n" +
                        "\n" +
                        "}";

        String body2 =
                "{\n" + " \"Resume by User - State\": [\n"
                        + msgAux6 + "\n ]\n" +
                        "\n" +
                        "}";


        if (!msgAux7.equals("")) {
            msgAux =
                    "{\n" + " \"Ps:\": The following users (ID) doens't have any UserStories assign to them. [\n"
                            + msgAux7 + "\n ]\n" +
                            "\n" +
                            "}";
        }

        String msg = header + firstChapter + secondChapter + thirdChapter + body + body1 + body2 + msgAux;



        return msg;
    }

    /*
    O percentual e o número total de user stories que estão atribuídas a cada usuário
    O percentual e o número de user stories em cada estágio de desenvolvimento
    O percentual e o número de user stories atribuídas a cada usuário em cada estágio de desenvolvimento
    */




}


