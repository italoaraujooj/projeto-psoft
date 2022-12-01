package com.ufcg.psoft.scrumboard.model.state;

import com.ufcg.psoft.scrumboard.enums.Status;
import com.ufcg.psoft.scrumboard.interfaces.UserStoryState;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

import java.util.Map;

public class InProgress implements UserStoryState {

    UserStory userStory;

    public InProgress(UserStory userStory) {
        this.userStory = userStory;
    }

    @Override
    public void addResponsible(User user) {
        this.userStory.getUsers().put(user.getId(), user);
    }

    @Override
    public void finishTask(String idTask) {
        Map<String, Task> tasks = this.userStory.getTasks();
        if(tasks.containsKey(idTask)) {
            this.finishTask(tasks.get(idTask));
            this.verifyChangeState(tasks);
        }
    }

    private void finishTask(Task task) {
        if(task.getStatus() == Status.NAO_REALIZADA) {
            task.setStatus(Status.REALIZADA);
        }
    }

    private void verifyChangeState(Map<String, Task> tasks) {
        boolean changeState = true;
        for(Task task : tasks.values()) {
            if(task.getStatus() == Status.NAO_REALIZADA) {
                changeState = false;
                break;
            }
        }
        if(changeState) this.userStory.setState(new ToVerify(this.userStory));
    }

    @Override
    public String toString() {
        return "IN_PROGRESS";
    }

}
