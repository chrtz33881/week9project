package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
    private Scanner scanner = new Scanner(System.in);
    private ProjectService projectService = new ProjectService();
    
    // List of available operations for the user
    private List<String> operations = List.of(
        "1) Add a project"
    );

    public static void main(String[] args) {
        new ProjectsApp().processUserSelections();
    }

    // Process user selections until done
    private void processUserSelections() {
        boolean done = false;
        while (!done) {
            try {
                int selection = getUserSelection();
                
                switch (selection) {
                    case -1:
                        done = exitMenu(); // Exit the menu if -1 is selected
                        break;
                        
                    case 1:
                        createProject(); // Create a new project if 1 is selected
                        break;
                        
                    default:
                        System.out.println("\n" + selection + " is not a valid selection. Try again.");
                }
            } catch (Exception e) {
                System.out.println("\nError: " + e + " Try again.");
            }
        }
    }

    // Get user selection from available operations
    private int getUserSelection() {
        printOperations(); // Print available operations to the user
        Integer input = getIntInput("Enter a menu selection"); // Get integer input from the user

        return Objects.isNull(input) ? -1 : input;
    }

    // Print available operations to the user
    private void printOperations() {
        System.out.println("\nThese are the available selections. Press the Enter key to quit:");
        
        operations.forEach(line -> System.out.println("  " + line));
    }

    // Get integer input from the user
    private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);
        
        if (Objects.isNull(input)) {
            return null;
        }
        try {
            return Integer.valueOf(input);
        } 
        catch (NumberFormatException e) {
            throw new DbException(input + " is not a valid number.");
        }
    }

    // Get string input from the user
    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();
        
        return input.isBlank() ? null : input.trim();
    }

    // Exit the menu
    private boolean exitMenu() {
        System.out.println("Exiting the menu...");
        return true;
    }

    // Create a new project
    private void createProject() {
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
        String notes = getStringInput("Enter the project notes");

        // Create a new Project object and set its properties
        Project project = new Project();
        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        // Add the project to the database through the project service
        Project dbProject = projectService.addProject(project);
        System.out.println("You have successfully created project: " + dbProject);
    }

    // Get decimal input from the user
    private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);
        if (Objects.isNull(input)) {
            return null;
        }
        try {
            return new BigDecimal(input).setScale(2);
        } catch (NumberFormatException e) {
            throw new DbException(input + " is not a valid decimal number.");
        }
    }
}