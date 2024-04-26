public class ClassroomServer {

    public static void main(String[] args) throws InterruptedException {
        Server studentServer = ServerBuilder.forPort(50051)
                .addService(new StudentServiceImpl())
                .build();

        Server classroomServer = ServerBuilder.forPort(50052)
                .addService(new ClassroomServiceImpl())
                .build();

        Server notificationServer = ServerBuilder.forPort(50053)
                .addService(new NotificationServiceImpl())
                .build();

        studentServer.start();
        classroomServer.start();
        notificationServer.start();

        System.out.println("gRPC servers are running.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            studentServer.shutdown();
            classroomServer.shutdown();
            notificationServer.shutdown();
        }));

        studentServer.awaitTermination();
        classroomServer.awaitTermination();
        notificationServer.awaitTermination();
    }
}
