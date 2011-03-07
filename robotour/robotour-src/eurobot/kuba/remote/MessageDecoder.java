package eurobot.kuba.remote;

import eurobot.kuba.KubaPuppet;

/**
 *
 * @author Kotuc
 */
public class MessageDecoder implements StringMessageListener {

    final KubaPuppet puppet;
    
    public MessageDecoder(KubaPuppet puppet) {
        this.puppet = puppet;
    }

    public synchronized void message(String line) {
        System.out.println("Income: " + line);
        try {
            String[] split = line.split("\\s");
            if ("lr".equals(split[0])) {
                int left = Integer.parseInt(split[1]);
                int right = Integer.parseInt(split[2]);
                puppet.setSpeediLR(left, right);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

