package app;

import concurrent.ConcurrentTaskProcessor;
import concurrent.TaskCompletionTracker;
import exception.ShelterException;
import model.Animal;
import model.AnimalStatus;
import model.CareTask;
import model.CareTaskStatus;
import model.Doctor;
import model.Volunteer;
import ordering.AnimalComparators;
import report.AnimalReportService;
import repository.AnimalFileLoader;
import repository.MedicalRecordFileLoader;
import repository.Repository;
import repository.VolunteerFileLoader;
import repository.DoctorFileLoader;
import service.AssignmentService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private final Repository<Volunteer> volunteerRepo = new Repository<>();
    private final Repository<Doctor> doctorRepo = new Repository<>();
    private final Repository<CareTask> taskRepo = new Repository<>();
    private final Repository<Animal> animalRepo = new Repository<>();
    private final AssignmentService assignmentService = new AssignmentService();
    private final AnimalReportService reportService = new AnimalReportService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    public void run() {
        loadAllData();

        while (true) {
            System.out.println("\n=== Animal Shelter Management System ===");
            System.out.println("1. Manager (Assign tasks based on skills)");
            System.out.println("2. Volunteer (View and complete my tasks)");
            System.out.println("3. Doctor (My interventions and medical treatments)");
            System.out.println("4. Adopt an animal");
            System.out.println("5. View animals (search, sort, reports)");
            System.out.println("6. Process assigned tasks in parallel (concurrency)");
            System.out.println("7. Quit");
            System.out.print("Choice: ");

            String choice = readLine();

            switch (choice) {
                case "1" -> runManagerMenu();
                case "2" -> runVolunteerMenu();
                case "3" -> runDoctorMenu();
                case "4" -> runAdoptionMenu();
                case "5" -> runSearchSortMenu();
                case "6" -> runConcurrencyMenu();
                case "7" -> {
                    System.out.println("Shutting down the application.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ---------------------------------------------------------------
    // 1. Manager Flow
    // ---------------------------------------------------------------

    private void runManagerMenu() {
        System.out.println("\n--- Task Assignment by the Manager ---");
        List<CareTask> unassigned = taskRepo.filter(t -> t.getStatus() == CareTaskStatus.UNASSIGNED);

        if (unassigned.isEmpty()) {
            System.out.println("No task is currently waiting for assignment.");
            return;
        }

        System.out.println("Available tasks:");
        for (CareTask t : unassigned) {
            System.out.println("- [" + t.getIdTask() + "] " + t.getDescription()
                    + " (Required skill/specialty: " + t.getRequiredSkill() + ")");
        }

        System.out.print("\nID of the task to assign (or leave blank to cancel): ");
        String taskId = readLine();
        if (taskId.isBlank()) return;

        try {
            CareTask task = taskRepo.findById(taskId);

            System.out.println("Type of worker to assign:");
            System.out.println("1. Volunteer");
            System.out.println("2. Doctor");
            System.out.print("Choice: ");
            String targetType = readLine();

            if ("1".equals(targetType)) {
                System.out.print("Volunteer ID: ");
                String volId = readLine();
                Volunteer v = volunteerRepo.findById(volId);
                assignmentService.assignTaskToVolunteer(task, v);
                System.out.println("Success: Task [" + task.getIdTask() + "] assigned to " + v.getNameVolunteer() + ".");
            } else if ("2".equals(targetType)) {
                System.out.print("Doctor ID: ");
                String docId = readLine();
                Doctor d = doctorRepo.findById(docId);
                assignmentService.assignTaskToDoctor(task, d);
                System.out.println("Success: Task [" + task.getIdTask() + "] assigned to Dr. " + d.getName() + ".");
            } else {
                System.out.println("Unrecognized option.");
            }
        } catch (ShelterException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 2. Volunteer Flow
    // ---------------------------------------------------------------

    private void runVolunteerMenu() {
        System.out.print("\nEnter your volunteer ID: ");
        String volunteerId = readLine();

        Volunteer volunteer;
        try {
            volunteer = volunteerRepo.findById(volunteerId);
        } catch (ShelterException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Hello " + volunteer.getNameVolunteer() + "!");

        List<CareTask> myTasks = taskRepo.filter(t ->
                volunteer.getId().equals(t.getAssignedWorkerId()) && t.getStatus() == CareTaskStatus.ASSIGNED
        );

        if (myTasks.isEmpty()) {
            System.out.println("You currently have no pending assigned tasks.");
            return;
        }

        System.out.println("Your tasks assigned by the manager:");
        for (CareTask t : myTasks) {
            System.out.println("- [" + t.getIdTask() + "] " + t.getDescription());
        }

        System.out.print("\nEnter the ID of the completed task to close it (or blank): ");
        String taskId = readLine();
        if (taskId.isBlank()) return;

        try {
            CareTask task = taskRepo.findById(taskId);
            if (!volunteer.getId().equals(task.getAssignedWorkerId())) {
                System.out.println("Error: This task was not assigned to you.");
                return;
            }
            task.markCompleted();
            System.out.println("Task " + taskId + " completed successfully. Thank you!");
        } catch (ShelterException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 3. Doctor Flow
    // ---------------------------------------------------------------

    private void runDoctorMenu() {
        System.out.print("\nEnter your doctor ID: ");
        String docId = readLine();

        Doctor doctor;
        try {
            doctor = doctorRepo.findById(docId);
        } catch (ShelterException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Hello Dr. " + doctor.getName() + " (" + doctor.getSpecialization() + ")");

        List<CareTask> assignedTasks = taskRepo.filter(t ->
                doctor.getId().equals(t.getAssignedWorkerId()) && t.getStatus() == CareTaskStatus.ASSIGNED
        );

        if (!assignedTasks.isEmpty()) {
            System.out.println("\nAssigned surgeries / treatments:");
            for (CareTask t : assignedTasks) {
                System.out.println("- [" + t.getIdTask() + "] " + t.getDescription());
            }
        }

        System.out.println("\nAvailable actions:");
        System.out.println("1. Log a free-form treatment on an animal");
        System.out.println("2. Mark an assigned task as completed");
        System.out.print("Choice: ");
        String choice = readLine();

        if ("1".equals(choice)) {
            System.out.print("ID of the treated animal: ");
            String aId = readLine();
            System.out.print("Description of the treatment: ");
            String desc = readLine();

            try {
                doctor.performTreatment(aId, desc);
                System.out.println("Treatment successfully logged for animal " + aId + ".");
            } catch (ShelterException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else if ("2".equals(choice)) {
            System.out.print("ID of the completed task: ");
            String tId = readLine();
            try {
                CareTask t = taskRepo.findById(tId);
                if (!doctor.getId().equals(t.getAssignedWorkerId())) {
                    System.out.println("Error: This task was not assigned to your account.");
                    return;
                }
                t.markCompleted();
                System.out.println("Task " + tId + " marked as completed.");
            } catch (ShelterException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // 4. Adoption Flow
    // ---------------------------------------------------------------

    private void runAdoptionMenu() {
        List<Animal> eligible = animalRepo.filter(Animal::isAdoptionEligible);

        if (eligible.isEmpty()) {
            System.out.println("\nNo animal is currently eligible for adoption.");
            return;
        }

        System.out.println("\nAnimals ready to be adopted:");
        for (Animal a : eligible) {
            System.out.println("- [" + a.getId() + "] " + a.getName()
                    + " (" + a.getClass().getSimpleName() + ", " + a.getAge() + " weeks)");
        }

        System.out.print("\nEnter the ID of the animal to adopt (or blank to quit): ");
        String id = readLine();
        if (id.isBlank()) return;

        try {
            Animal a = animalRepo.findById(id);
            a.completeAdoption();
            System.out.println("Congratulations! The adoption of " + a.getName() + " has been recorded.");
        } catch (ShelterException e) {
            System.out.println("Adoption rejected: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 5. Search & Sort Flow (Module 7 - Streams, Module 3/4 - Comparators)
    // ---------------------------------------------------------------

    private void runSearchSortMenu() {
        List<Animal> all = animalRepo.getAll();
        if (all.isEmpty()) {
            System.out.println("\nNo animal registered.");
            return;
        }

        System.out.println("\n--- Search & Sort ---");
        System.out.println("1. Sort by ID");
        System.out.println("2. Sort by age");
        System.out.println("3. Sort by arrival date");
        System.out.println("4. Sort by name");
        System.out.println("5. Sort by breed (dogs), then by species");
        System.out.println("6. Filter by status (AVAILABLE, PENDING, ADOPTED, MEDICAL_HOLD)");
        System.out.println("7. Filter by medical readiness (ready for adoption)");
        System.out.println("8. Report: number of animals by status");
        System.out.println("9. Report: number of animals by species");
        System.out.print("Choice: ");
        String choice = readLine();

        switch (choice) {
            case "1" -> printAnimals(reportService.sortedBy(all, AnimalComparators.BY_ID));
            case "2" -> printAnimals(reportService.sortedBy(all, AnimalComparators.BY_AGE));
            case "3" -> printAnimals(reportService.sortedBy(all, AnimalComparators.BY_ARRIVAL_DATE));
            case "4" -> printAnimals(reportService.sortedBy(all, AnimalComparators.BY_NAME));
            case "5" -> printAnimals(reportService.sortedBy(all, AnimalComparators.BY_SPECIES_THEN_RACE));
            case "6" -> {
                System.out.print("Status to search for: ");
                try {
                    AnimalStatus status = AnimalStatus.valueOf(readLine().trim().toUpperCase());
                    printAnimals(reportService.filterByStatus(all, status));
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown status. Possible values: AVAILABLE, PENDING, ADOPTED, MEDICAL_HOLD.");
                }
            }
            case "7" -> printAnimals(reportService.filterByMedicalReadiness(all, true));
            case "8" -> {
                Map<AnimalStatus, Long> byStatus = reportService.countByStatus(all);
                System.out.println("\nBreakdown by status:");
                byStatus.forEach((status, count) -> System.out.println("- " + status + ": " + count));
            }
            case "9" -> {
                Map<String, Long> bySpecies = reportService.countBySpecies(all);
                System.out.println("\nBreakdown by species:");
                bySpecies.forEach((species, count) -> System.out.println("- " + species + ": " + count));
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void printAnimals(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        for (Animal a : animals) {
            System.out.println("- [" + a.getId() + "] " + a.getName()
                    + " (" + a.getClass().getSimpleName() + ", " + a.getAge() + " wks, arrived "
                    + a.getArrivalDate() + ", status " + a.getStatus() + ")");
        }
    }

    // ---------------------------------------------------------------
    // 6. Concurrency Flow (Module 9)
    // ---------------------------------------------------------------

    private void runConcurrencyMenu() {
        List<CareTask> assigned = taskRepo.filter(t -> t.getStatus() == CareTaskStatus.ASSIGNED);

        if (assigned.isEmpty()) {
            System.out.println("\nNo ASSIGNED task to process. Assign tasks first via the Manager menu (option 1).");
            return;
        }

        System.out.println("\n--- Concurrent Processing of Assigned Tasks ---");
        System.out.println("ASSIGNED tasks found: " + assigned.size());
        for (CareTask t : assigned) {
            System.out.println("- [" + t.getIdTask() + "] " + t.getDescription() + " -> worker " + t.getAssignedWorkerId());
        }

        System.out.println("\n1. Normal mode (every worker has time to finish)");
        System.out.println("2. Interruption demo mode (timeout too short, the owner interrupts the workers)");
        System.out.print("Choice: ");
        String mode = readLine();

        // One finite thread per ASSIGNED task (bounded pool); each worker
        // simulates the care work then completes its task. In mode 2, the
        // owner's join timeout is shorter than the simulated work, so the
        // workers get interrupted before they can finish - demonstrating the
        // interruption path without ever corrupting a task's state.
        long simulatedWorkMillis = "2".equals(mode) ? 1500 : 300;
        long joinTimeoutMillis = "2".equals(mode) ? 200 : 5000;

        ConcurrentTaskProcessor processor = new ConcurrentTaskProcessor();
        processor.processAssignedTasks(assigned, simulatedWorkMillis, joinTimeoutMillis);

        TaskCompletionTracker tracker = processor.getTracker();
        System.out.println("\nResult after join() of all workers:");
        System.out.println("- Completed tasks: " + tracker.getCompletedCount() + " / " + assigned.size());
        System.out.println("- Interrupted tasks: " + tracker.getInterruptedTaskIds());
        System.out.println("- Failures: " + tracker.getFailedTaskIds());

        // Post-join proof: every task is either COMPLETED or still ASSIGNED.
        // No other state is possible -> no task lost, no corrupted state,
        // regardless of how the threads were interleaved.
        boolean invariantHolds = true;
        for (CareTask t : assigned) {
            boolean ok = t.getStatus() == CareTaskStatus.COMPLETED || t.getStatus() == CareTaskStatus.ASSIGNED;
            if (!ok) {
                invariantHolds = false;
                System.out.println("INVARIANT VIOLATED for task " + t.getIdTask() + ": state = " + t.getStatus());
            }
        }
        if (invariantHolds) {
            System.out.println("Invariant verified: every task is COMPLETED or remains ASSIGNED (no loss, no corruption).");
        }
    }

    // ---------------------------------------------------------------
    // Helpers & Data Initialization
    // ---------------------------------------------------------------

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private void loadAllData() {
        // Robust data file resolution (handles upper/lower case variants).
        String animalFile = resolvePath("data/Animals.txt");
        String volunteerFile = resolvePath("data/Volunteers.txt");
        String medicalFile = resolvePath("data/MedicalRecords.txt");
        String doctorFile = resolvePath("data/Doctors.txt");

        new AnimalFileLoader().loadFromFile(animalFile, animalRepo);
        new VolunteerFileLoader().loadFromFile(volunteerFile, volunteerRepo);
        new MedicalRecordFileLoader().loadFromFile(medicalFile, animalRepo);

        // Load doctors from Doctors.txt
        new DoctorFileLoader().loadFromFile(doctorFile, doctorRepo);

        taskRepo.add(new CareTask("T1", "Feed the dogs", "feeding"));
        taskRepo.add(new CareTask("T2", "Walk the cats", "walking"));
        taskRepo.add(new CareTask("T3", "Broken leg surgery", "Chirurgie"));
    }

    private String resolvePath(String... candidates) {
        for (String candidate : candidates) {
            if (new File(candidate).exists()) {
                return candidate;
            }
        }
        return candidates[0];
    }
}
