package iticbcn.subscriber;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class RfidTopic extends AWSIotTopic {

    private static final String FEEDBACK_TOPIC_NAME = Main.FEEDBACK_TOPIC_NAME;
    private final AWSIotMqttClient client;

    public RfidTopic(String topic, AWSIotQos qos, AWSIotMqttClient client) {
        super(topic, qos);
        this.client = client;
    }

    @Override
    public void onMessage(AWSIotMessage message) {
      String payload = message.getStringPayload();

      try {
        // Decoding the payload as JSON
        JSONObject jsonPayload = new JSONObject(payload);
        String rfid = jsonPayload.optString("rfid", "none");

        switch (rfid) {
          case "none":
            returnFeedBack("The payload must contain an rfid String field", false);
            break;

          default: storeData(jsonPayload, rfid);

        }
      } catch (JSONException e) {
        returnFeedBack("Body format invalid, not JSON format", false);
        System.err.println("Error decoding JSON payload: " + e.getMessage());
        return; // Exit the method if JSON is invalid
      } 

    }

    private void storeData(JSONObject jsonPayload, String rfid) {
      try {
        Database db = Database.getInstance();
        String room = jsonPayload.optString("room", "none");

        if(room.equalsIgnoreCase("none")) {
          String message = "The payload must contain an room String field";
          returnFeedBack(message, false);
          return;
        
        }

        if (!db.checkRoomExists(room)) {
          String message = String.format("Room: %s,  doesnt exist", room);
          returnFeedBack(message, false);
          System.out.println(message);
          return;
        }

        String userId = db.getUserIdFromRFID(rfid);
        if (userId == null) {
          String message = String.format("User with rfid: %s,  doesnt exist", rfid);
          returnFeedBack(message, false);
          System.out.println(message);
          return;
        }

        if (!db.canUserAccessRoom(Integer.parseInt(userId), room)) {
          String message = String.format("User with rfid: %s can not enter room: %s", rfid, room);
          returnFeedBack(message, false);
          System.out.println(message);
          return;
        }

        if (db.insertCheckin(Integer.parseInt(userId), room)){
          returnFeedBack("Usuario registrado correctamente", true); //* **** ENTRADA GUARDADA EXITOSAMENTE
          System.out.printf("User with rfid '%s' entered room '%s'", rfid, room);
        }
        else {
          returnFeedBack("Semantic error trying to insert user presence check in", false);
          System.out.println("Semantic error trying to insert user presence check in");
        }

      } 
      catch (Exception e) {
        returnFeedBack("Error en el procesamiento", false);
        System.out.println(e);
        e.printStackTrace();
      }
    }

    public void returnFeedBack(String feedbackMessage, boolean ok) {
      try {
        // Convirtiendo a JSON
        JSONObject feedbackJson = new JSONObject();
        feedbackJson.put("status", ok? "1": "0");
        feedbackJson.put("message", feedbackMessage);
        String feedbackMessageJson = feedbackJson.toString();
        // Debugging
        System.out.println("Feedback en " + FEEDBACK_TOPIC_NAME + ": " + feedbackMessageJson);
        // Objeto mensaje
        AWSIotMessage feedback = new NonBlockingPublishListener(FEEDBACK_TOPIC_NAME, qos, feedbackMessageJson);
        client.publish(feedback);
      } catch (Exception e) {
        System.err.println("Error publicando el feedback: " + e.getMessage());
      }
    }

    public void publishMessage(String messageBody, String topic) {
      try {
        client.publish(topic == null ? this.topic : topic, AWSIotQos.QOS0, messageBody);
        AWSIotMessage message = new NonBlockingPublishListener(FEEDBACK_TOPIC_NAME, qos, messageBody);
        client.publish(message);
        System.out.println("Publish en " + topic == null ? this.topic : topic + ": " + messageBody);
      } catch (Exception e) {
        System.err.println("Error publicando el mensaje: " + e.getMessage());
      }
    }
}
