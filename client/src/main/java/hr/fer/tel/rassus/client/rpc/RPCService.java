package hr.fer.tel.rassus.client.rpc;

import com.google.protobuf.Empty;
import hr.fer.tel.rassus.client.Application;
import hr.fer.tel.rassus.client.Reading;
import hr.fer.tel.rassus.client.SensorGrpc;
import hr.fer.tel.rassus.client.utils.ReadingDTO;
import io.grpc.stub.StreamObserver;
import java.util.logging.Logger;

public class RPCService extends SensorGrpc.SensorImplBase {

    private static final Logger logger = Logger.getLogger(RPCService.class.getName());

    @Override
    public void requestReading(Empty request, StreamObserver<Reading> responseObserver) {
        logger.info("Got a new request for reading!");

        ReadingDTO readingDTO = Application.getSensorReading();

        //creating response
        Reading response = Reading.newBuilder()
                .setTemperature(readingDTO.getTemperature())
                .setPressure(readingDTO.getPressure())
                .setHumidity(readingDTO.getHumidity())
                .setCo(readingDTO.getCo())
                .setNo2(readingDTO.getNo2() == null ? -1 : readingDTO.getNo2())
                .setSo2(readingDTO.getSo2() == null ? -1 : readingDTO.getSo2())
                .build();

        responseObserver.onNext(response);

        logger.info("Responding with: " + readingDTO.toString());
        // Send a notification of successful stream completion.
        responseObserver.onCompleted();


    }
}
