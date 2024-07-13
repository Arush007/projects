import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class GenerationData {
    int date_ts;
    float cumulative_gen;
    float daily_generation;
}

public class Solar_res_toDB {

    private static final int MAX_LINE_LENGTH = 100;
    private static final int MAX_ROWS = 1000;
    private static final int SECONDS_PER_DAY = 60 * 60 * 24;
    private static final int DATE_STR_LEN = 20;

    // Read the input data per row into GenerationData
    // Row format: <timestamp>,<number>
    // Returns the number of rows read.
    private static int readInput(GenerationData[] data, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header line
            br.readLine();

            int num_rows = 0;
            String line;
            while ((line = br.readLine()) != null) {
                // Parse input row: <date>,<number>
                String[] tokens = line.split(",");
                data[num_rows] = new GenerationData();
                data[num_rows].date_ts = Integer.parseInt(tokens[0]);
                data[num_rows].cumulative_gen = Float.parseFloat(tokens[1]);
                num_rows++;
            }
            return num_rows;
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
            return -1;
        }
    }

    // Compute daily generation
    private static void computeDailyGeneration(GenerationData[] data, int num_rows) {
        data[0].daily_generation = 0;
        for (int row_index = 1; row_index < num_rows; ++row_index) {
            float diff_seconds = data[row_index].date_ts - data[row_index - 1].date_ts;
            float diff_days = diff_seconds / SECONDS_PER_DAY;

            float diff_generation =
                    data[row_index].cumulative_gen - data[row_index - 1].cumulative_gen;
            data[row_index].daily_generation = diff_generation / diff_days;
        }
    }

    // Compute daily average generation
    private static float computeDailyAverageGeneration(GenerationData[] data, int num_rows) {
        float total = 0;
        for (int row_index = 1; row_index < num_rows; ++row_index) {
            total += data[row_index].daily_generation;
        }
        return total / (num_rows - 1);
    }

    private static String getDateString(int timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date((long) timestamp * 1000);
        return sdf.format(date);
    }

    // Get days with generation below daily average
    private static int printDaysBelowAverage(GenerationData[] data, int num_rows, float avg) {
        int num_days = 0;
        for (int row_index = 1; row_index < num_rows; ++row_index) {
            if (data[row_index].daily_generation < avg) {
                // Convert timestamp to date string
                String date_str = getDateString(data[row_index].date_ts);
                // Print date and daily generation
                System.out.printf("Timestamp: %d, Date: %s, Daily generation: %f\n",
                        data[row_index].date_ts, date_str, data[row_index].daily_generation);
                num_days++;
            }
        }
        System.out.printf("Number of days below average %f: %d/%d", avg, num_days, num_rows);
        return num_days;
    }

    public static void main(String[] args) {
        GenerationData[] gen_data = new GenerationData[MAX_ROWS];

        // Read input from file into array
        int num_rows = readInput(gen_data, "C:\\Users\\ashwi\\OneDrive\\Documents\\Project_docs_java_sem5\\data.csv");

        if (num_rows == -1) {
            return;
        }

        // Compute daily generation
        computeDailyGeneration(gen_data, num_rows);

        float daily_avg = computeDailyAverageGeneration(gen_data, num_rows);
        System.out.printf("Daily average generation: %f, num_days: %d\n", daily_avg, num_rows);

        printDaysBelowAverage(gen_data, num_rows, daily_avg);
    }
}


