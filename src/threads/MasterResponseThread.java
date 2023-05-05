package threads;

import java.io.PrintWriter;

public class MasterResponseThread extends Thread{

    PrintWriter requestWriter;
    String msg;

    public MasterResponseThread(PrintWriter printWriter, String msg) {
        this.requestWriter = printWriter;
        this.msg = msg;
    }

    @Override
    public void run() {
        requestWriter.println(msg);
    }



}
