package iticbcn.subscriber;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class RfidTopicListener extends AWSIotTopic {

    public RfidTopicListener(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        String payload = message.getStringPayload();
        System.out.println("Mensaje recibido: " + payload);

        // Guardar en base de datos
        storeData(payload);
    }

    private void storeData(String datos) {
        // Lógica para guardar en base de datos (por ejemplo, MySQL o MongoDB)
        System.out.println("Guardando en base de datos: " + datos);
    }
}