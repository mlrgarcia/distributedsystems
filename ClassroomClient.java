import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import classroom.*;

import java.util.Scanner;

public class ClassroomClient {
    private static final ManagedChannel studentChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

    private static final ManagedChannel classroomChannel = ManagedChannelBuilder.forAddress("localhost", 50052)
            .usePlaintext()
            .build();

    private static final ManagedChannel notificationChannel = ManagedChannelBuilder.forAddress("localhost", 50053)
            .usePlaintext()
            .build();

    private static final StudentServiceGrpc.StudentServiceBlockingStub studentStub =
            StudentServiceGrpc.newBlockingStub(studentChannel);

    private static final ClassroomServiceGrpc.ClassroomServiceBlockingStub classroomStub =
            ClassroomServiceGrpc.newBlockingStub(classroomChannel);

    private static final NotificationServiceGrpc.NotificationServiceBlockingStub notificationStub =
            NotificationServiceGrpc.newBlockingStub(notificationChannel);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Classroom Automation CLI ---");
            System.out.println("1. Register a new student");
            System.out.println("2. Get student details");
            System.out.println("3. Grade an assignment");
            System.out.println("4. Get assignment grades");
            System.out.println("5. Start a class");
            System.out.println("6. Stop a class");
            System.out.println("7. Send a notification");
            System.out.println("8. Exit");

            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    registerStudent(scanner);
                    break;
                case 2:
                    getStudentDetails(scanner);
                    break;
                case 3:
                    gradeAssignment(scanner);
                    break;
                case 4:
                    getAssignmentGrades(scanner);
                    break;
                case 5:
                    startClass(scanner);
                    break;
                case 6:
                    stopClass(scanner);
                    break;
                case 7:
                    sendNotification(scanner);
                    break;
                case 8:
                    shutdown();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void registerStudent(Scanner scanner) {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age =  Integer.parseInt(scanner.nextLine());

        Student student = Student.newBuilder()
                .setId(id)
                .setName(name)
                .setAge(age)
                .build();

        RegisterStudentRequest request = RegisterStudentRequest.newBuilder()
                .setStudent(student)
                .build();

        GetStudentResponse response = studentStub.registerStudent(request);
        System.out.println("Registered student: " + response.getStudent().getName());
    }

    private static void getStudentDetails(Scanner scanner) {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        StudentId request = StudentId.newBuilder().setId(studentId).build();

        try {
            GetStudentResponse response = studentStub.getStudent(request);
            System.out.println("Student details: " + response.getStudent().getName());
        } catch (Exception e) {
            System.out.println("Student not found.");
        }
    }

    private static void gradeAssignment(Scanner scanner) {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter assignment name: ");
        String assignmentName = scanner.nextLine();
        System.out.print("Enter grade: ");
        int grade = Integer.parseInt(scanner.nextLine());

        GradeAssignmentRequest request = GradeAssignmentRequest.newBuilder()
                .setStudentId(StudentId.newBuilder().setId(studentId).build())
                .setAssignmentName(assignmentName)
                .setGrade(grade)
                .build();

        try {
            studentStub.gradeAssignment(request);
            System.out.println("Assignment graded.");
        } catch (Exception e) {
            System.out.println("Failed to grade assignment.");
        }
    }

    private static void getAssignmentGrades(Scanner scanner) {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        GetAssignmentGradesRequest request = GetAssignmentGradesRequest.newBuilder()
                .setStudentId(StudentId.newBuilder().setId(studentId).build())
                .build();

        try {
            GetAssignmentGradesResponse response = studentStub.getAssignmentGrades(request);
            System.out.println("Assignment grades for student:");
            for (AssignmentGrade grade : response.getAssignmentGradesList()) {
                System.out.println("  - " + grade.getAssignmentName() + ": " + grade.getGrade());
            }
        } catch (Exception e) {
            System.out.println("Failed to get assignment grades.");
        }
    }

    private static void startClass(Scanner scanner) {
        System.out.print("Enter classroom name: ");
        String classroomName = scanner.nextLine();
        System.out.print("Enter instructor name: ");
        String instructor = scanner.nextLine();

        StartClassRequest request = StartClassRequest.newBuilder()
                .setClassroom(Classroom.newBuilder()
                        .setName(classroomName)
                        .setInstructor(instructor)
                        .build())
                .build();

        try {
            classroomStub.startClass(request);
            System.out.println("Class started.");
        } catch (Exception e) {
            System.out.println("Failed to start class.");
        }
    }

    private static void stopClass(Scanner scanner) {
        System.out.print("Enter classroom name: ");
        String classroomName = scanner.nextLine();

        StopClassRequest request = StopClassRequest.newBuilder()
                .setClassroom(Classroom.newBuilder()
                        .setName(classroomName)
                        .build())
                .build();

        try {
            classroomStub.stopClass(request);
            System.out.println("Class stopped.");
        } catch (Exception e) {
            System.out.println("Failed to stop class.");
        }
    }

    private static void sendNotification(Scanner scanner) {
        System.out.print("Enter notification message: ");
        String message = scanner.nextLine();

        SendNotificationRequest request = SendNotificationRequest.newBuilder()
                .setNotification(Notification.newBuilder()
                        .setMessage(message)
                        .setTimestamp(System.currentTimeMillis())
                        .build())
                .build();

        try {
            notificationStub.sendNotification(request);
            System.out.println("Notification sent.");
        } catch (Exception e) {
            System.out.println("Failed to send notification.");
        }
    }

    private static void shutdown() {
        studentChannel.shutdown();
        classroomChannel.shutdown();
        notificationChannel.shutdown();
        System.out.println("Client shutting down.");
    }
}
