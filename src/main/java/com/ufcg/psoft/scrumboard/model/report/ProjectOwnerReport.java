package com.ufcg.psoft.scrumboard.model.report;

import com.ufcg.psoft.scrumboard.model.UserStory;


import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectOwnerReport extends ReportModel {

    private final Collection<UserStory> listUserStories;

    public static class Builder extends ReportModel.Builder<Builder> {

        private final Collection<UserStory> listUserStories;

        public Builder(Collection<UserStory> listUserStories){
            this.listUserStories = listUserStories;
        }
        @Override public ProjectOwnerReport build() {
            return new ProjectOwnerReport(this);
        }
        @Override protected Builder self() { return this; }
    }
    private ProjectOwnerReport(Builder builder) {
        super(builder);
        listUserStories = builder.listUserStories;
    }

    @Override
    public String getReport() {
        Collection<UserStory> list = this.listUserStories;
        String header = super.getReport();
        String firstChapter;
        List<UserStory> listToMap = super.verifyList(list);
        int totalOfUserStories = list.size();
        Map<String, List<UserStory>> mapUserStoryByState = super.mapStateAndUserStories(listToMap);

        String msgAux  = super.userStoryByState(list);
        String msgAux1 = super.resumeUserStoryByState(mapUserStoryByState, totalOfUserStories);

        firstChapter  = "\n" +
                "{\n" + " \"States\": [\n"
                        + msgAux + "\n  ]\n" +
                        "\n" +
                        "}";

        String body =
                "{\n" + " \"Resume by State\": [\n"
                        + msgAux1 + "\n ]\n" +
                        "\n" +
                        "}";


        String msg = header + firstChapter + body;

        return msg;
    }


}


