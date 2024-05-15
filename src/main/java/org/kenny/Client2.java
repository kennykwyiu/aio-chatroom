package org.kenny;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client2 {

    final String LOCALHOST = "localhost";
    final int DEFAULT_PORT = 8888;
    AsynchronousSocketChannel clientChannel;

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
        // create channel
        try {
            clientChannel = AsynchronousSocketChannel.open();
            Future<Void> future = clientChannel.connect(new InetSocketAddress(LOCALHOST, DEFAULT_PORT));
            future.get();

            // wait for user input
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = consoleReader.readLine();

                byte[] inputBytes = input.getBytes();
                ByteBuffer bufffer = ByteBuffer.wrap(inputBytes);
                Future<Integer> writeResult = clientChannel.write(bufffer);
                writeResult.get();

                bufffer.flip();
                Future<Integer> readResult = clientChannel.read(bufffer);
                readResult.get();

                String echo = new String(bufffer.array());
                bufffer.clear();

                System.out.println(echo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            close(clientChannel);
        }
    }

    public static void main(String[] args) {
        Client2 client = new Client2();
        client.start();
    }
}
