package iticbcn.subscriber;

import java.security.KeyStore;

public class KeyStorePasswordPair {

    public final KeyStore keyStore;   // Almacén de claves que contiene el certificado y la clave privada
    public final String keyPassword; // Contraseña del almacén de claves (generalmente null para AWS IoT)

    public KeyStorePasswordPair(KeyStore keyStore, String keyPassword) {
        this.keyStore = keyStore;
        this.keyPassword = keyPassword;
    }

}
