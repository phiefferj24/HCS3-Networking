import com.jimphieffer.network.client.Client;
import com.jimphieffer.Message;

import java.util.Objects;

public class Test implements Client {

    //send --> to server
    //relay --> sent to everybody
    //message for message type
    //if type == message type disconnet someone just disconnected


    //send message r
    @Override
    public void onMessage(String message) {
        String s = Message.decode(message);
        boolean relay = (Objects.equals(Message.getProtocol(message), Message.MessageProtocol.RELAY));



    }

    @Override
    public String getName() {
        return null;
    }
}
