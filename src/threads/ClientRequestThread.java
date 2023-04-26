package threads;

import java.io.PrintWriter;

public class ClientRequestThread extends Thread{

    PrintWriter requestWriter;
    String msg;

    public ClientRequestThread(PrintWriter printWriter, String msg) {
        this.requestWriter = printWriter;
        this.msg = msg;
    }

    @Override
    public void run() {
        requestWriter.println(msg);
    }
}
