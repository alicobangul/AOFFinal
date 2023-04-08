package basesoftware.com.aoffinal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Training")
public class TrainingModel implements Serializable {

    @PrimaryKey(autoGenerate = false)
    public int id;

    @ColumnInfo(name = "trainingCount")
    public int trainingCount;

    @ColumnInfo(name = "training")
    public String training;

    @ColumnInfo(name = "trainingresult")
    public String trainingResult;

    public TrainingModel(int id, int trainingCount, String training, String trainingResult) {
        this.id = id;
        this.trainingCount = trainingCount;
        this.training = training;
        this.trainingResult = trainingResult;
    }

}
