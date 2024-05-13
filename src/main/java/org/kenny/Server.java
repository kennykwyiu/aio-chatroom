package org.kenny;

import java.io.Closeable;
import java.io.IOException;

public class Server {

    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 8888;

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
}
