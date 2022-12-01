package com.ufcg.psoft.scrumboard.model.report;

import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.UserStory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserReport extends ReportModel {

    private final List<UserStory> listUserStoryByUser;
    private final Profile profileOfUser;
    private final Collection<UserStory>  listUserStories;


    public static class Builder extends ReportModel.Builder<Builder> {

        private final  List<UserStory> listUserStoryByUser;
        private final Profile profileOfUser;
        private final Collection<UserStory>  listUserStories;

        public Builder (Profile profile,  List<UserStory>  listUserStoryByUser, Collection<UserStory> listUserStories){
            this.profileOfUser = profile;
            this.listUserStoryByUser = listUserStoryByUser;
            this.listUserStories = listUserStories;
        }

        @Override public UserReport build() {
            return new UserReport(this);
        }
        @Override protected Builder self() { return this; }
    }
    private UserReport(Builder builder) {
        super(builder);
        listUserStoryByUser = builder.listUserStoryByUser;
        profileOfUser = builder.profileOfUser;
        listUserStories = builder.listUserStories;
    }

    @Override
    public String getReport() {
        List<UserStory> listToMap = this.listUserStoryByUser;
        int totalOfUserStories = this.listUserStories.size();

        String header = super.getReport();
        String msgAux, msgAux1, msgAux2;

        if(listToMap == null){
            msgAux  = "    User don't have any UserStory assign to him.";
            msgAux1 = msgAux;
            msgAux2 = msgAux;

        }else{
            Map<String, List<UserStory>> mapUserStoryByState = super.mapStateAndUserStories(listToMap);
            msgAux = super.stateAndUserStories(mapUserStoryByState);
            msgAux1 = super.resumeUserStoryByUser(listToMap,totalOfUserStories);
            msgAux2 = super.resumeSingleUserStoryByState(mapUserStoryByState,totalOfUserStories);
        }

        String msg =
                "{\n" + " \"States\": [\n"
                        + msgAux + "\n  ]\n" +
                        "\n" +
                "}";

        String body1 =
                "{\n" + " \"Resume by User\": [\n"
                        + msgAux1 + "\n ]\n" +
                        "\n" +
                "}";

        String body2 =
                "{\n" + " \"Resume by State\": [\n"
                        + msgAux2 + "\n ]\n" +
                        "\n" +
                        "}";


        String msgFinal = header + msg + body1 + body2;

        return msgFinal;
    }


}
