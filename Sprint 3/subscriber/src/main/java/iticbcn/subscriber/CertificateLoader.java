package iticbcn.subscriber;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.cert.X509CertificateHolder;

import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.Security;

public class CertificateLoader {

    static {
        // Asegúrate de que Bouncy Castle esté registrado
        Security.addProvider(new BouncyCastleProvider());
    }

    // Cargar el certificado X.509 desde un archivo
    public static X509Certificate loadCertificateFromFile(String certificateFile) throws IOException {
        PEMParser pemParser = null;
        try (FileReader fileReader = new FileReader(certificateFile)) {
            pemParser = new PEMParser(fileReader);
            X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();
            java.security.cert.CertificateFactory certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509", "BC");
            return (X509Certificate) certificateFactory.generateCertificate(new java.io.ByteArrayInputStream(certificateHolder.getEncoded()));
        } catch (Exception e) {
            throw new IOException("Error al cargar el certificado desde " + certificateFile, e);
        }
        finally {
            if (pemParser != null) pemParser.close();
        }
    }

    // Cargar la clave privada desde un archivo
    public static PrivateKey loadPrivateKeyFromFile(String privateKeyFile) throws IOException {
        PEMParser pemParser = null;

        try (FileReader fileReader = new FileReader(privateKeyFile)) {
            pemParser = new PEMParser(fileReader);
            Object pemObject = pemParser.readObject();
            
            if (pemObject instanceof org.bouncycastle.openssl.PEMKeyPair) {
                org.bouncycastle.openssl.PEMKeyPair pemKeyPair = (org.bouncycastle.openssl.PEMKeyPair) pemObject;
                return new JcaPEMKeyConverter().getPrivateKey(pemKeyPair.getPrivateKeyInfo());
            } else {
                throw new IOException("El archivo no contiene una clave privada válida");
            }
        } catch (Exception e) {
            throw new IOException("Error al cargar la clave privada desde " + privateKeyFile, e);
        }
        finally {
            if (pemParser != null) pemParser.close();
        }
    }
}
