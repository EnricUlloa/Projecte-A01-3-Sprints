package iticbcn.subscriber;

import java.io.InputStream;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

public class Main {

    private static final String CLIENT_ENDPOINT = "a2m49w5lny7qra-ats.iot.us-east-1.amazonaws.com"; 
    private static final String CLIENT_ID = "rfid-client-listener";     
    private static final String CERTIFICATE_FILE = "/certificate.pem.crt";
    private static final String PRIVATE_KEY_FILE = "/private.pem.key";
    private static final String TOPIC_NAME = "esp32/pub";
    public static final String FEEDBACK_TOPIC_NAME = "esp32/sub";   
    
    public static void main(String[] args) {
        try {
            System.out.println("Conectando a base de datos");
            System.out.println("Conexión exitosa a las:" + Database.getInstance().getCurrentDateAsString());

            // Inicializar cliente MQTT con certificados
            AWSIotMqttClient client = Util.initClient(CLIENT_ENDPOINT, CLIENT_ID, CERTIFICATE_FILE, PRIVATE_KEY_FILE);
            
            // Conectar al cliente
            client.connect();

            // Crear y registrar el listener para el tema
            RfidTopic topic = new RfidTopic(TOPIC_NAME, AWSIotQos.QOS0, client);
            client.subscribe(topic, true);

            // Mandar feedback de funcionando correctamente
            topic.returnFeedBack("CLIENTE ACTIVO", true);

            System.out.println("Suscrito al tema: " + TOPIC_NAME);
            
            
            // Mantener la aplicación corriendo para recibir mensajes
            while (true) {
                Thread.sleep(1000);
            }


        } catch (AWSIotException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // Método para cargar los archivos de recursos desde el JAR
    public static InputStream getResourceAsStream(String resourceName) {
        return Main.class.getResourceAsStream(resourceName);
    }
}
