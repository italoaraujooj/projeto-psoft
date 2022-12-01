package com.ufcg.psoft.scrumboard.model.report;

import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

import java.util.*;

public abstract class ReportModel implements Report{

    public enum Info{DATA, COMMENTS, INFO}
    final Map<Info, List<String>> infos;

    private final Project project;
    private final User user;

    abstract static class Builder<T extends Builder<T>> {
        Map<Info, List<String>> infos = new HashMap<>();

        private Project project;
        private User user;

        public T addInfo(Info type, String info) {
            infos.computeIfAbsent(type, k-> new ArrayList<>()).add(info);
            return self();
        }

        public T addDescriptors(Project project, User user){
            this.project = project;
            this.user = user;
            return self();
        }

        abstract ReportModel build();
        protected abstract T self();
    }

    ReportModel(Builder<?> builder){
        infos = builder.infos;
        project = builder.project;
        user = builder.user;
    }


    @Override
    public String getReportScrum(){return null;}

    @Override
    public String getReport() {

        String msg =
                "{\n" +  " \"Report of Project" + "\n" +
                        " \"Project ID:\": " +  this.project.getId() + "  \"Project Title\": " + this.project.getName() + "\n" +
                        " \"Requester\": "   +  this.user.getName()  + " \"ID\": " + this.user.getId() + "\n" +
                        "}";


        return msg;
    }

    private String selection(Map<Info, List<String>> infos){
        String auxString = "";

        for (Map.Entry<Info, List<String>> entry : infos.entrySet()) {
            auxString += "    Type: " + entry.getKey() + "\n";
            for (String item: entry.getValue()) {
                auxString += "    " + item + ",\n";
            }
        }

        return auxString;
    }

    protected String stateAndUserStories (Map<String, List<UserStory>> listMap){
        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : listMap.entrySet()) {
            auxString += "    State: " + entry.getKey() + "\n";
            for (UserStory item: entry.getValue()) {
                auxString += "    " + item.toStringResume() + ",\n";
            }
        }

        return auxString;
    }

    protected Map<String, List<UserStory>> mapStateAndUserStories (List<UserStory> listMap){

        Map<String, List<UserStory>> entry = new HashMap<>();

        for (UserStory userStory: listMap) {
            entry.computeIfAbsent(userStory.getState(), k-> new ArrayList<>()).add(userStory);
        }

        return entry;
    }

    protected String userAndUserStories(Map<String, List<UserStory>> listMap) {
        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : listMap.entrySet()) {
            auxString += "    User: " + entry.getKey()  +"\n";
            for (UserStory item: entry.getValue()) {
                auxString += "    " + item.toStringResume() + ",\n";
            }
        }

        return auxString;
    }

    protected String userByStateAndUserStories(Map<String, List<UserStory>> map) {
        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : map.entrySet()) {
            auxString += "    User: " + entry.getKey()  +"\n";
            auxString += stateAndUserStories(mapStateAndUserStories(entry.getValue()));
        }

        return auxString;
    }

    protected String userStoryByState(Collection<UserStory> list){
        String msgAux = "";
        List<UserStory> listAux = verifyList(list);

        if(!listAux.isEmpty()){
            Map<String, List<UserStory>> map = mapStateAndUserStories(listAux);
            msgAux = stateAndUserStories(map);
        }

        return msgAux;
    }

    protected List<UserStory> verifyList(Collection<UserStory> collection){

        List<UserStory> listAux = new ArrayList<>();

        if (collection != null) {
            if (collection instanceof List) {
                listAux = (List) collection;
            } else {
                listAux = new ArrayList(collection);
            }
        }

        return listAux;
    }


    protected String resumeUserStoryByUser(Map<String, List<UserStory>> listMap, int totalOfUserStories){
        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : listMap.entrySet()) {
            auxString += "    User: " + entry.getKey()  + "has ( " + entry.getValue().size() + " )  UserStories assigned to him." + "( " +
                    calculatePercentage(totalOfUserStories, entry.getValue().size()) + "% ) \n";
        }

        return auxString;
    }

    protected String resumeSingleUserStoryByState(Map<String, List<UserStory>> listMap, int totalOfUserStories){

        String auxString = "";
        auxString += "    User: " + this.user.getId() + " has per State: \n";

        for (Map.Entry<String, List<UserStory>> entry : listMap.entrySet()) {
            auxString += "    State: " + entry.getKey()  + " - ( " + entry.getValue().size() + " )  UserStories assigned to him." + "( " +
                    calculatePercentage(totalOfUserStories, entry.getValue().size()) + "% ) \n";
        }
        /* User: X has per State:
            State - (Y) UserStories assigned to him. (percentual).
        */
        return auxString;
    }

    protected String resumeUserStoryByState(Map<String, List<UserStory>> userStoryByState, int totalOfUserStories){

        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : userStoryByState.entrySet()) {
            auxString += "    State: " + entry.getKey()  + " - ( " + entry.getValue().size() + " )  UserStories assigned to him." + "( " +
                    calculatePercentage(totalOfUserStories, entry.getValue().size()) + "% ) \n";
        }

        return auxString;
    }

    protected String resumeUserStoryByUserState(Map<String, List<UserStory>> userStoryByUser, int totalOfUserStories){

        String auxString = "";

        for (Map.Entry<String, List<UserStory>> entry : userStoryByUser.entrySet()) {
            auxString += "    User: " + entry.getKey()  + " - ( " + entry.getValue().size() + " )  UserStories assigned to him." +
                    "( " + calculatePercentage(totalOfUserStories, entry.getValue().size()) + "% ) \n";
            auxString += resumeUserStoryByState(mapStateAndUserStories(entry.getValue()), totalOfUserStories);
        }

        return auxString;
    }

    protected String resumeUserStoryByUser(List<UserStory> list, int totalOfUserStories){
        String auxString = "";
        auxString += "    User: " + this.user.getId()  + " has ( " + list.size() + " )  UserStories assigned to him." + "( " +
                calculatePercentage(totalOfUserStories, list.size()) + "% ) \n";
        return auxString;
    }

    private double calculatePercentage(int total, int value){
        int totalPercent = 100;
        double percentage =  ((double) value /total)*(totalPercent);
        return percentage;
    }

    protected String listOfUserWithoutUserStories(Set<String> keySet, Set<String> usersIdOfProject) {
        String auxString = "";

        for (String id: usersIdOfProject) {
            if (!keySet.contains(id)){
                auxString += "[ " +id + " ]";
            }
        }

        return auxString;
    }


}
