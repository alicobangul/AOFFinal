package basesoftware.com.aoffinal.domain.model;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TrainingModel {

    private ObservableArrayList<String> arrayTraining;

    private ObservableArrayList<String> arrayTrainingResult;

    private final MutableLiveData<String> midtermExam = new MutableLiveData<>("");

    private final MutableLiveData<String> finalExam = new MutableLiveData<>("");

    private final MutableLiveData<String> average = new MutableLiveData<>("");

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



    public LiveData<String> getMidtermExam() { return midtermExam; }

    public void setMidtermExam(String midtermExam) { this.midtermExam.setValue(midtermExam); }

    public LiveData<String> getFinalExam() { return finalExam; }

    public void setFinalExam(String finalExam) { this.finalExam.setValue(finalExam); }

    public LiveData<String> getAverage() { return average; }

    public void setAverage(String average) { this.average.setValue(average); }

}
