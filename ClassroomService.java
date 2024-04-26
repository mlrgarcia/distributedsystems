import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import classroom.ClassroomServiceGrpc;
import classroom.StartClassRequest;
import classroom.StopClassRequest;
import classroom.Empty;

public class ClassroomServiceImpl extends ClassroomServiceGrpc.ClassroomServiceImplBase {

    @Override
    public void startClass(StartClassRequest request, StreamObserver<Empty> responseObserver) {
        // Logic to start the class (e.g., sending a notification, updating the schedule)
        System.out.println("Class started: " + request.getClassroom().getName());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void stopClass(StopClassRequest request, StreamObserver<Empty> responseObserver) {
        // Logic to stop the class
        System.out.println("Class stopped: " + request.getClassroom().getName());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
