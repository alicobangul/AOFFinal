package basesoftware.com.aoffinal.model.domain;

import androidx.databinding.ObservableArrayList;

public class TrainingModel {

    private ObservableArrayList<String> arrayTraining;

    private ObservableArrayList<String> arrayTrainingResult;

    private String midtermExam = "";

    private String finalExam = "";

    private String average = "";

    public TrainingModel() {

        this.arrayTraining = new ObservableArrayList<>();

        this.arrayTrainingResult = new ObservableArrayList<>();

        for (int i = 0; i < 8; i++) {

            arrayTraining.add("");

            arrayTrainingResult.add("");

        }

    }

    public ObservableArrayList<String> getArrayTraining() { return arrayTraining; }

    public void setArrayTraining(ObservableArrayList<String> arrayTraining) { this.arrayTraining = arrayTraining; }

    public void trainingDataChanged(Integer dataCount, String data) { arrayTraining.set(dataCount, data); }


    public ObservableArrayList<String> getArrayTrainingResult() { return arrayTrainingResult; }

    public void setArrayTrainingResult(ObservableArrayList<String> arrayTrainingResult) { this.arrayTrainingResult = arrayTrainingResult; }

    public void resultDataChanged(Integer resultCount, String data) { arrayTrainingResult.set(resultCount, data); }




    public String getMidtermExam() { return midtermExam; }

    public void setMidtermExam(String midtermExam) { this.midtermExam = midtermExam; }

    public String getFinalExam() { return finalExam; }

    public void setFinalExam(String finalExam) { this.finalExam = finalExam; }

    public String getAverage() { return average; }

    public void setAverage(String average) { this.average = average; }

}
