package fr.upem.crazygame.bytebuffer_manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ByteBufferManager {

    /**
     * Permet de lire une requête tant que le buffer n'est pas plein
     * @param sc
     * @param buffer
     * @return
     * @throws IOException
     */
    public static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (sc.read(buffer) == -1) {
                return false;
            }
        }

        return true;
    }
}