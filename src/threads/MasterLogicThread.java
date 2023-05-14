package threads;

public class MasterLogicThread extends Thread {

    String jobTypeSubmitted;
    ClientThread clientThread;
    int jobsOfTypeA;
    int jobsOfTypeB;
    SlaveThread slaveThreadA;
    SlaveThread slaveThreadB;
    int id;




    public MasterLogicThread(String jobTypeSubmitted, ClientThread clientThread, int jobsOfTypeA,
                             int jobsOfTypeB, SlaveThread slaveThreadA, SlaveThread slaveThreadB,
                             int id) {
        this.jobTypeSubmitted = jobTypeSubmitted;
        this.clientThread = clientThread;
        this.jobsOfTypeA = jobsOfTypeA;
        this.jobsOfTypeB = jobsOfTypeB;
        this.slaveThreadA = slaveThreadA;
        this.slaveThreadB = slaveThreadB;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("logic thread started");
            // Wait for job type to be set
            while (clientThread.getJobType() == null) {

                Thread.sleep(200);

            }
            jobTypeSubmitted = clientThread.getJobType();
            // Pass job type to slaveA threads
            if (jobTypeSubmitted.equals("A")) {
                if (jobsOfTypeA <= 5) {
                    slaveThreadA.setJobType(clientThread.getJobType());
                    jobsOfTypeA++;
                    slaveThreadA.setJobID(id);
                    System.out.println("Sent job id: " + id + " to Slave A");
                } else {
                    slaveThreadB.setJobType(clientThread.getJobType());
                    jobsOfTypeB++;
                    slaveThreadB.setJobID(id);
                    System.out.println("Sent job id: " + id + " to Slave B");
                }
            }
            // Pass job type to slaveB threads
            if (jobTypeSubmitted.equals("B")) {
                if (jobsOfTypeB <= 5) {
                    slaveThreadB.setJobType(clientThread.getJobType());
                    jobsOfTypeB++;
                    slaveThreadB.setJobID(id);
                    System.out.println("Sent job id: " + id + " to Slave B");
                } else {
                    slaveThreadA.setJobType(clientThread.getJobType());
                    jobsOfTypeA++;
                    slaveThreadA.setJobID(id);
                    System.out.println("Sent job id: " + id + " to Slave A");
                }
            }
            while (!slaveThreadA.getJobCompleted() && !slaveThreadB.getJobCompleted()) {
                Thread.sleep(200);
            }
            clientThread.setJobCompleted(true);
            if (clientThread.getJobType().equals("A")) {
                jobsOfTypeA--;
            }
            if (clientThread.getJobType().equals("B")) {
                jobsOfTypeB--;
            }
            id++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
