package scripts;

public interface Task {
    Priority priority();  // Returns the priority of the task
    boolean validate();    // Validates whether the task should be executed
    void execute();       // Executes the task
}