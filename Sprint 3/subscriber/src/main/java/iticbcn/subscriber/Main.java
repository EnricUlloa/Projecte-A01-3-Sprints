package iticbcn.subscriber;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class Main {

    private static final String CLIENT_ENDPOINT = "alhkpl3ouqngv-ats.iot.us-east-1.amazonaws.com"; 
    private static final String CLIENT_ID = "rfid-subscriber";     
    private static final String CERTIFICATE_FILE = "src/main/resources/certificate.pem.crt";
    private static final String PRIVATE_KEY_FILE = "src/main/resources/private.pem.key";
    private static final String TOPIC_NAME = "prueba";          

    public static void main(String[] args) {
        try {
            // Inicializar cliente MQTT con certificados
            AWSIotMqttClient client = Util.initClient(CLIENT_ENDPOINT, CLIENT_ID, CERTIFICATE_FILE, PRIVATE_KEY_FILE);

            // Conectar al cliente
            client.connect();

            // Crear y registrar el listener para el tema
            AWSIotTopic topic = new RfidTopicListener(TOPIC_NAME, AWSIotQos.QOS0);
            client.subscribe(topic, true);

            System.out.println("Suscrito al tema: " + TOPIC_NAME);

            // Mantener la aplicación corriendo para recibir mensajes
            while (true) {
                Thread.sleep(1000);
            }

        } catch (AWSIotException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
