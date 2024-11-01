package hr.fer.tel.rassus.client.rpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RPCServer {
    private static final Logger logger = Logger.getLogger(RPCServer.class.getName());

    private Server server;
    private final RPCService service;
    private final int port;

    public RPCServer(RPCService service, int port) {
        this.service = service;
        this.port = port;
    }

    public void start()  {
        // Register the service
        try {
            server = ServerBuilder.forPort(port)
                    .addService(service)
                    .build()
                    .start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Server started on " + port);

        //  Clean shutdown of server in case of JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            RPCServer.this.stop();
            System.err.println("Server shut down");
        }));
    }

    public void stop()  {
        if (server != null) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void blockUntilShutdown()  {
        if (server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
