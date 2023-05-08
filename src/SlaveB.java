import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SlaveB {
    public static final int PREFERRED_WORK = 2;
    public static final int SLEEP_PREFERRED = 2000;
    public static final int SLEEP_NON_PREFERRED = 10000;


    public static Socket connectToServer(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    public static void doWorkIndefinitely(Socket connectionToServer) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(connectionToServer.getInputStream()));
        PrintWriter pw = new PrintWriter(connectionToServer.getOutputStream());
        String work;
        while ((work = br.readLine()) != null) {
            System.out.println("Received work string: "+work);
            int workType = Character.getNumericValue(work.charAt(0));
            work = work.substring(1);
            int result;
            if (workType == 1) {
                result = work1(work);
            } else if (workType == 2) {
                result = work2(work);
            } else {
                throw new IllegalArgumentException("Worktype: " + workType + " is unrecognized");
            }
            sleepAfterWork((workType));
            pw.println(result);
            pw.flush();
        }
    }

    public static void sleepAfterWork(int workType) {
        try {
            if (workType == PREFERRED_WORK) {
                System.out.println("Sleeping for: "+SLEEP_PREFERRED);
                Thread.sleep(SLEEP_PREFERRED);
            } else {
                System.out.println("Sleeping for: "+SLEEP_NON_PREFERRED);
                Thread.sleep(SLEEP_NON_PREFERRED);
            }
        } catch (InterruptedException ignore) {
        }
        System.out.println("Waking");
    }

    // 5 3 1 = 9
    public static int work1(String work) {
        String[] strings = work.split(" ");
        int res = 0;
        for (String s : strings) {
            res += Integer.parseInt(s);
        }
        return res;
    }

    // 5 3 1 = 15
    public static int work2(String work) {
        String[] strings = work.split(" ");
        int res = 1;
        for (String s : strings) {
            res *= Integer.parseInt(s);
        }
        return res;
    }


    public static void main(String[] args) throws IOException {
//        String host = args[0];
//        int port = Integer.parseInt((args[1]));
        String host = "127.0.0.1";
        int port = 30122;
        Socket connectionToServer = connectToServer(host, port);
        doWorkIndefinitely(connectionToServer);
    }
}
