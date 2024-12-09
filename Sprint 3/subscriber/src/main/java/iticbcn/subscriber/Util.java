package iticbcn.subscriber;


import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import com.amazonaws.services.iot.client.AWSIotMqttClient;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import java.io.BufferedReader;
import java.security.cert.X509Certificate;

public class Util {
    private static final String alias = "keyAlias";
    private static final String password = "askjdhlsjdapADSADSADESP32";

    // Cargar un KeyStore con el certificado y la clave privada
    public static KeyStore getKeyStorePasswordPair(String certificateFile, String privateKeyFile, String algorithm) {
        try {
            // Crear un KeyStore vacío
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password.toCharArray());

            // Cargar el certificado y la clave privada usando Bouncy Castle
            X509Certificate certificate = loadCertificateFromPEM(certificateFile);
            PrivateKey privateKey = loadPrivateKeyFromPEM(privateKeyFile, algorithm);

            // Agregar el certificado y la clave privada al KeyStore
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[]{certificate});

            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new RuntimeException("Error al cargar el KeyStore desde los archivos", e);
        }
    }

    // Cargar el certificado X.509 desde un archivo PEM
    public static X509Certificate loadCertificateFromPEM(String certificateFile) throws IOException, CertificateException {
        try (PEMParser pemParser = new PEMParser(
            new BufferedReader( new InputStreamReader(
                Main.getResourceAsStream(certificateFile)
            ))
        )) {
            X509CertificateHolder certHolder = (X509CertificateHolder) pemParser.readObject();
            return new JcaX509CertificateConverter().getCertificate(certHolder);
        }
    }

    // Cargar la clave privada desde un archivo PEM
    public static PrivateKey loadPrivateKeyFromPEM(String privateKeyFile, String algorithm) throws IOException {
        try (PEMParser pemParser = new PEMParser(
            new BufferedReader( new InputStreamReader(
                Main.getResourceAsStream(privateKeyFile)
            ))
        )) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PEMKeyPair pemKeyPair = (PEMKeyPair) object;
            return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    // Inicializar el cliente MQTT de AWS IoT
    public static AWSIotMqttClient initClient(String endpoint, String clientId, String certificateFile, String privateKeyFile) {
        KeyStore key = Util.getKeyStorePasswordPair(certificateFile, privateKeyFile, "RSA");
        return new AWSIotMqttClient(endpoint, clientId, key, password);
    }
}
