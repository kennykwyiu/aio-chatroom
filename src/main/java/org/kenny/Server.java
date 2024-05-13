package org.kenny;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Server {

    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 8888;
    AsynchronousServerSocketChannel serverChannel;

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("Close " + closeable);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() {
        try {
            // connect listener port
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            System.out.println("Start server, listener port: " + DEFAULT_PORT);

            while (true) {
                serverChannel.accept(null, new AcceptHandler());
                System.in.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(serverChannel);
        }
    }
}
