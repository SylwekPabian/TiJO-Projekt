public class RxTxManager {
    public interface Command {
        public void execute(String data);
    }

    public static final RxTxManager instance = new RxTxManager();

    private RxTxConnection rxtxRfidReader = new RxTxConnection();
    private Thread rfidThread;
    private RfidThreadMethod rfidThreadMethod;
    private RxTxConnection rxtxBarcodeReader = new RxTxConnection();
    private Command command;

    private RxTxManager() {}

    public void enableRfidReader(Command newCommand) throws Exception {
        command = newCommand;
        rxtxRfidReader.connect("COM3");
        rfidThreadMethod = new RfidThreadMethod();
        rfidThread = new Thread(rfidThreadMethod);
        rfidThread.start();
    }

    public void disableRfidReader() {
        rfidThreadMethod.setActive(false);
        command = null;
    }

    public void close() {
        rxtxRfidReader.closeAll();
        rxtxBarcodeReader.closeAll();
    }

    public boolean isCommandSet() { return command != null; }

    public class RfidThreadMethod implements Runnable {
        private boolean active = true;

        @Override
        public void run() {
            String data;
            while(isActive()) {
                data = rxtxRfidReader.read();
                if (data != null) {
                    System.out.println(data);
                    if (command != null) {
                        command.execute(data);
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            System.out.println("RFID thread closed");
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
