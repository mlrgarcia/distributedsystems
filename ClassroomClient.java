import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
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
                case 
