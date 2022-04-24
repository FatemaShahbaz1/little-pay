import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class calTrip {
    public static void main(String[] args) throws Exception {

        // Initializing Variables
        String panId = null, arrivalDate = null, stopId = null, amount = "$0", status = null;
        boolean flag = true;

        // CSV output file
        String csv = "C:\\trips.csv";
        // CSV input file
        String strFile = "C:\\taps.csv";
        CSVReader reader = new CSVReader(new FileReader(strFile));
        String[] nextLine;
        int lineNumber = 0;

        // Traversing through the data in the file
        while ((nextLine = reader.readNext()) != null) {

            lineNumber++;

            // Check Tap On scenario
            if (nextLine[6] != null && nextLine[2].toString().trim().equals("ON")) {

                panId = nextLine[6].toString().trim();
                arrivalDate = nextLine[1].toString().trim();
                stopId = nextLine[3].toString().trim();


            }
            // Check Tap Off Scenatrio
            if (nextLine[2].toString().trim().equals("OFF")) {
                if (nextLine[6].toString().trim().equals(panId)) {
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

                        // Calculating Bus Trip Charges
                        switch (nextLine[3].toString().trim().toLowerCase()) {
                            case ("stop1"):
                                if (stopId.equals("Stop2"))
                                    amount = "$3.25";
                                if (stopId.equals("Stop3"))
                                    amount = "$7.30";
                                break;
                            case ("stop2"):
                                if (stopId.equals("Stop1"))
                                    amount = "$3.25";
                                if (stopId.equals("Stop3"))
                                    amount = "$5.50";
                                break;
                            case ("stop3"):
                                if (stopId.equals("Stop2"))
                                    amount = "$5.50";
                                if (stopId.equals("Stop1"))
                                    amount = "$7.30";
                                break;
                        }

                        // Calculating Duration of the Trip
                        SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");

                        Date started = formatter.parse(arrivalDate);
                        Date finished = formatter.parse(nextLine[1].toString().trim());
                        long diff = finished.getTime() - started.getTime();

                        // Saving Status Value
                        if (stopId.equals(nextLine[3].toString().trim())) {
                            status = "CANCELLED";
                            amount = "$0";
                        } else
                            status = "COMPLETED";

                        // Saving data in the CSV Output File
                        String[] data1 = {arrivalDate, nextLine[1].toString().trim(), diff + "", stopId, nextLine[3].toString().trim(),
                                amount, nextLine[4].toString().trim(), nextLine[5].toString().trim(), panId, status
                        };
                        writer.writeNext(data1);

                        // Closing writer connection
                        writer.close();
                        flag = false;
                        panId = null;
                        continue;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }

            // Checking Incomplete Trip
            if (nextLine[2].toString().trim().equals("ON") && panId != nextLine[6].toString().trim() && flag == false) {
                status = "INCOMPLETE";
                CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
                switch (nextLine[3].toString().trim().toLowerCase()) {
                    case ("stop2"):
                        amount = "$5.50";
                        break;
                    default:
                        amount = "$7.30";
                        break;
                }

                String[] data1 = {nextLine[1].toString().trim(), nextLine[1].toString().trim(), "0", nextLine[3].toString().trim(), nextLine[3].toString().trim(),
                        amount, nextLine[4].toString().trim(), nextLine[5].toString().trim(), nextLine[6].toString().trim(), status
                };
                writer.writeNext(data1);
                writer.close();


            }
        }

    }
}
