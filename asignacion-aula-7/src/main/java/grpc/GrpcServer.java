package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new EstudianteServiceImpl())
                .build()
                .start();

        System.out.println("gRPC Server started on port 9090");
        server.awaitTermination();
    }
}