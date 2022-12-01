package com.ufcg.psoft.scrumboard;

import com.ufcg.psoft.scrumboard.dto.ProjectDTO;
import com.ufcg.psoft.scrumboard.dto.TaskDTO;
import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.dto.UserStoryDTO;
import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.service.ProjectService;
import com.ufcg.psoft.scrumboard.service.TaskService;
import com.ufcg.psoft.scrumboard.service.UserService;
import com.ufcg.psoft.scrumboard.service.UserStoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.ufcg.psoft.scrumboard.*")
public class
ScrumBoardApplication {

	public static void main(String[] args) {

		SpringApplication.run(ScrumBoardApplication.class, args);

	}

	@Bean
	CommandLineRunner runner(UserService userService, ProjectService projectService, UserStoryService userStoryService, TaskService taskService){
		return	(args)-> {
			UserDTO userDTO1 = new UserDTO("Italo", "teste@gmail.com", "italoramalh");
			UserDTO userDTO2 = new UserDTO("Matheus", "teste2@gmail.com", "matheus-a-r");
			UserDTO userDTO3 = new UserDTO("Gabriel", "teste3@gmail.com", "gabi");
			User user1 = userService.getUserById(userService.addUser(userDTO1));
			User user2 = userService.getUserById(userService.addUser(userDTO2));
			User user3 = userService.getUserById(userService.addUser(userDTO3));
			ProjectDTO projectDTO1 = new ProjectDTO("Psoft", "Projeto psoft", "UFCG");
			ProjectDTO projectDTO2 = new ProjectDTO("LogComp", "Logica 123", "UFCG");
			Project project1 = projectService.findProjectById(projectService.addProject(user1.getId(), projectDTO1));
			Project project2 = projectService.findProjectById(projectService.addProject(user2.getId(), projectDTO2));
			projectService.addUserToProject(user1.getId(), project1.getId(), user2.getId(), Position.INTERN);
			projectService.changeUserProfile(project1.getId(), user1.getId(), user2.getId(), Position.PROJECT_OWNER);
			projectService.addUserToProject(user1.getId(), project1.getId(), user3.getId(), Position.DEVELOPER);
			String us01id = userStoryService.createUserStory(user1.getId(), project1.getId(), new UserStoryDTO("us_teste", "teste testando"));
			UserStory us01 = userStoryService.findUserStoryById(user1.getId(), project1.getId(), us01id);
			taskService.createTask(user1.getId(), us01id, project1.getId(), new TaskDTO("task_teste", "teste"));
			userStoryService.assignToUserStory(user3.getId(), project1.getId(), us01id);


		};
	}
}
