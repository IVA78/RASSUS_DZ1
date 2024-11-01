package hr.fer.tel.rassus.client.rpc;


import com.google.protobuf.Empty;
import hr.fer.tel.rassus.client.Reading;
import hr.fer.tel.rassus.client.SensorGrpc;
import hr.fer.tel.rassus.client.utils.ReadingDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RPCClient {
    private static final Logger logger = Logger.getLogger(RPCClient.class.getName());

    private final ManagedChannel channel;
    private final SensorGrpc.SensorBlockingStub sensorBlockingStub;

    public RPCClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        sensorBlockingStub = SensorGrpc.newBlockingStub(channel);
    }

    public void stop() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public  ReadingDTO requestReading() {

        Empty request = Empty.newBuilder().build();
        logger.info("Sending reading request from client.");
        try {
            Reading response = sensorBlockingStub.requestReading(request);
            logger.info("Received: " + "SensorData{" +
                    "temperature=" + response.getTemperature() +
                    ", pressure=" + response.getPressure() +
                    ", humidity=" + response.getHumidity() +
                    ", co=" + response.getCo() +
                    ", no2=" + response.getNo2() +
                    ", so2=" + response.getSo2() +
                    '}');

            return new ReadingDTO(response.getTemperature(), response.getPressure(), response.getHumidity(), response.getCo(), response.getNo2(), response.getSo2());

        } catch (StatusRuntimeException e) {
            logger.info("This is exception, RPC failed: " + e.getMessage());
        }

        return null;
    }



}
