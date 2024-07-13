#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_LENGTH 1024
#define MAX_NUM_ROWS 1000

typedef struct {
    int date;
    int daily_gen;
    int cum_power;
} GenerationData;

void daily_avggen();

int main() {
    FILE *fp;
    char line[MAX_LINE_LENGTH];
    char *token;
    GenerationData data[MAX_NUM_ROWS];
    int cum_power_sum = 0, daily_gen_sum = 0, num_rows = 0;

    fp = fopen("data.csv", "r");
    if (fp == NULL) {
        printf("Error opening file.\n");
        return 1;
    }

    // Skip header line
    fgets(line, MAX_LINE_LENGTH, fp);

    while (fgets(line, MAX_LINE_LENGTH, fp)) {
        // Parse CSV fields
        token = strtok(line, ",");
        data[num_rows].date = atoi(token);
        token = strtok(NULL, ",");
        data[num_rows].daily_gen = atoi(token);
        token = strtok(NULL, ",");
        data[num_rows].cum_power = atoi(token);

        // Calculate cumulative and daily power sums
        cum_power_sum += data[num_rows].cum_power;
        daily_gen_sum += data[num_rows].daily_gen;
        num_rows++;
    }

    // Calculate averages
    double avg_cum_power = (double) cum_power_sum / num_rows;
    double avg_daily_gen = (double) daily_gen_sum / num_rows;

    printf("Average Cumulative Power: %lf\n", avg_cum_power);
    printf("Average Daily Generation: %lf\n", avg_daily_gen);

    // Store results in a structure
    typedef struct {
        double avg_cum_power;
        double avg_daily_gen;
    } Result;
    Result result = { avg_cum_power, avg_daily_gen };

    fclose(fp);
    return 0;
}
void daily_avggen()
{
     int cum_power_sum = 0, daily_gen_sum = 0, num_rows = 0;
        
}