package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void start() throws IOException, InterruptedException {
        int port = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 8080;

        Server server = ServerBuilder.forPort(port)
                .addService(new EstudianteServiceImpl())
                .build()
                .start();

        System.out.println("gRPC Server started on port " + port);
        server.awaitTermination();
    }
}