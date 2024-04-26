import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import classroom.Student;
import classroom.StudentServiceGrpc;
import classroom.RegisterStudentRequest;
import classroom.GetStudentResponse;
import classroom.StudentId;
import classroom.GradeAssignmentRequest;
import classroom.GetAssignmentGradesRequest;
import classroom.GetAssignmentGradesResponse;
import classroom.AssignmentGrade;
import classroom.Empty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, List<AssignmentGrade>> assignmentGrades = new HashMap<>();

    @Override
    public void registerStudent(RegisterStudentRequest request, StreamObserver<GetStudentResponse> responseObserver) {
        Student student = request.getStudent();
        students.put(student.getId(), student);
        responseObserver.onNext(GetStudentResponse.newBuilder().setStudent(student).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getStudent(StudentId request, StreamObserver<GetStudentResponse> responseObserver) {
        Student student = students.get(request.getId());
        if (student != null) {
            responseObserver.onNext(GetStudentResponse.newBuilder().setStudent(student).build());
        } else {
            responseObserver.onError(new RuntimeException("Student not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void gradeAssignment(GradeAssignmentRequest request, StreamObserver<Empty> responseObserver) {
        String studentId = request.getStudentId().getId();
        AssignmentGrade assignmentGrade = AssignmentGrade.newBuilder()
                .setAssignmentName(request.getAssignmentName())
                .setGrade(request.getGrade())
                .build();
        assignmentGrades.computeIfAbsent(studentId, k -> new ArrayList<>()).add(assignmentGrade);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAssignmentGrades(GetAssignmentGradesRequest request, StreamObserver<GetAssignmentGradesResponse> responseObserver) {
        String studentId = request.getStudentId().getId();
        List<AssignmentGrade> grades = assignmentGrades.getOrDefault(studentId, new ArrayList<>());

        responseObserver.onNext(GetAssignmentGradesResponse.newBuilder()
                .addAllAssignmentGrades(grades)
                .build());
        responseObserver.onCompleted();
    }
}
