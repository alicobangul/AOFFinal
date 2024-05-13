package basesoftware.com.aoffinal.model.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "TrainingTable")
public class TrainingDbModel implements Serializable {
    @PrimaryKey()
    public int id = 0;

    @ColumnInfo(name = "midtermexam")
    private String midtermExam;

    @ColumnInfo(name = "finalexam")
    private String finalExam;

    @ColumnInfo(name = "average")
    private String average;

    @ColumnInfo(name = "trainings")
    private String trainings;

    @ColumnInfo(name = "trainingresults")
    private String trainingResults;

    public TrainingDbModel(String midtermExam, String finalExam, String average, String trainings, String trainingResults) {
        this.midtermExam = midtermExam;
        this.finalExam = finalExam;
        this.average = average;
        this.trainings = trainings;
        this.trainingResults = trainingResults;
    }

    public String getMidtermExam() {
        return midtermExam;
    }

    public String getFinalExam() {
        return finalExam;
    }

    public String getAverage() {
        return average;
    }

    public String getTrainings() {
        return trainings;
    }

    public String getTrainingResults() {
        return trainingResults;
    }
}
