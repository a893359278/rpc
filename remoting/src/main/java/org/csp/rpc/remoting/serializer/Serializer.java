package org.csp.rpc.remoting.serializer;

import java.io.IOException;

public interface Serializer {
    byte[] encoder(Object msg) throws IOException;

    Object decoder(byte [] bytes) throws IOException;
}
