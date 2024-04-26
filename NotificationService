import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import classroom.NotificationServiceGrpc;
import classroom.SendNotificationRequest;
import classroom.Empty;

public class NotificationServiceImpl extends NotificationServiceGrpc.NotificationServiceImplBase {

    @Override
    public void sendNotification(SendNotificationRequest request, StreamObserver<Empty> responseObserver) {
        // Logic to send notification
        System.out.println("Notification: " + request.getNotification().getMessage());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
