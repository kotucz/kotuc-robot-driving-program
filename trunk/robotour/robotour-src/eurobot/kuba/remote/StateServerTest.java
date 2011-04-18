package eurobot.kuba.remote;

import state.State;

/**
 *
 * @author Kotuc
 */
public class StateServerTest {

    public static void main(String args[]) {
        State st = new State();

        if (st.query("test")) {
            System.out.println(st.get());
        }

        st.set("test", "value");
        st.set("test", 0.32);

        if (st.query("test")) {
            System.out.println(st.get());
        }

        st.close();
    }
}
