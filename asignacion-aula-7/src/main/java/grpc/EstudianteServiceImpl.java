package grpc;

import edu.pucmm.eict.grpc.*;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class EstudianteServiceImpl extends EstudianteServiceGrpc.EstudianteServiceImplBase {

    @Override
    public void obtenerEstudiante(EstudianteRequest request, StreamObserver<EstudianteReply> responseObserver) {
        int matricula = request.getMatricula();

        Estudiante estudiante = EstudianteServices.getInstancia().estudiantePorMatricula(matricula);

        if (estudiante == null) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Estudiante no encontrado")
                    .asRuntimeException());
            return;
        }

        EstudianteReply reply = EstudianteReply.newBuilder()
                .setMatricula(estudiante.getMatricula())
                .setNombre(estudiante.getNombre())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void listarEstudiantes(Empty request, StreamObserver<EstudianteList> responseObserver) {
        List<Estudiante> estudiantes = EstudianteServices.getInstancia().findAll();

        EstudianteList.Builder lista = EstudianteList.newBuilder();

        for (Estudiante e : estudiantes) {
            EstudianteReply er = EstudianteReply.newBuilder()
                    .setMatricula(e.getMatricula())
                    .setNombre(e.getNombre())
                    .build();
            lista.addEstudiantes(er);
        }

        responseObserver.onNext(lista.build());
        responseObserver.onCompleted();
    }

    @Override
    public void crearEstudiante(EstudianteReply request, StreamObserver<EstudianteReply> responseObserver) {
        Estudiante nuevo = new Estudiante(request.getMatricula(), request.getNombre());
        EstudianteServices.getInstancia().crear(nuevo);

        EstudianteReply reply = EstudianteReply.newBuilder()
                .setMatricula(nuevo.getMatricula())
                .setNombre(nuevo.getNombre())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void borrarEstudiante(EstudianteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        int matricula = request.getMatricula();
        boolean eliminado = EstudianteServices.getInstancia().eliminar(matricula);

        DeleteResponse response = DeleteResponse.newBuilder()
                .setExito(eliminado)
                .setMensaje(eliminado ?
                        "Estudiante eliminado correctamente" :
                        "Estudiante no encontrado")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
