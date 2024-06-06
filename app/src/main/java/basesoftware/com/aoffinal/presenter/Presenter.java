package basesoftware.com.aoffinal.presenter;

import android.os.Build;
import androidx.databinding.ObservableArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import basesoftware.com.aoffinal.impl.IContract;
import basesoftware.com.aoffinal.impl.IModelObserver;
import basesoftware.com.aoffinal.model.TrainingRepository;
import basesoftware.com.aoffinal.model.domain.TrainingModel;
import basesoftware.com.aoffinal.model.roomdb.TrainingDbModel;
import basesoftware.com.aoffinal.util.Constant;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class Presenter implements IContract.IPresenter, IModelObserver {

    @Inject
    TrainingRepository repository;

    @Inject
    Validator validator;

    @Inject
    Calculater calculater;

    private IContract.IView view;

    @Inject
    public Presenter() { }

    public void detach() { repository.removeObserver(this); }

    @Override
    public void initBinds(IContract.IView view) {

        this.view = view;

        repository.addObserver(this);

    }

    @Override
    public void textChanged(TrainingModel trainingModel, @Nonnull Integer viewTag, String viewText) {

        switch (viewTag) {

            case -1 -> trainingModel.setMidtermExam(viewText);

            case -2 -> trainingModel.setFinalExam(viewText);

            case -3 -> trainingModel.setAverage(viewText);

            default -> trainingModel.trainingDataChanged(viewTag, viewText);

        }

    }

    @Override
    public void calculateClick(@Nonnull TrainingModel trainingModel) {

        if(checkTrainingAvailable(trainingModel.getArrayTraining())) {

            calculateDefaultControl(trainingModel);

            int average = Integer.parseInt(trainingModel.getAverage().getValue());

            double midtermExam = calculater.midtermCalculate(trainingModel.getMidtermExam().getValue());

            double finalExam = calculater.finalExamCalculate(trainingModel.getFinalExam().getValue());

            for (int count = 0; count < trainingModel.getArrayTraining().size(); count++) {

                // Kullanıcıdan gelen notu hesapla & not indeks değerine göre sonuç listesine kaydet.

                String training = trainingModel.getArrayTraining().get(count); // Ders notu

                String trainingCalculateResult = calculateResult(training, midtermExam, finalExam, average); // Ders notuna göre hesaplanan sonuç

                trainingModel.getArrayTraining().set(count, training); // Eğer verinin sonunda nokta var ise otomatik temizlendi

                trainingModel.getArrayTrainingResult().set(count, trainingCalculateResult); // Hesaplanan sonuç, sonuçların tutulduğu listeye eklendi
            }

            view.saveQuestion();

        }
    }

    public boolean checkTrainingAvailable(ObservableArrayList<String> trainings) {

        boolean isTrainingAvailable = false;

        // Ders notlarının tutulduğu listedeki tüm stringler empty değil -> min 1 adet ders notu var
        if (Build.VERSION.SDK_INT >= 24) if (!trainings.stream().allMatch(String::isEmpty)) isTrainingAvailable = true;

        else {
            for (String training : trainings)
                if (!training.isEmpty()) {
                    isTrainingAvailable = true; // Ders notlarının tutulduğu listedeki tüm stringler empty değil -> min 1 adet ders notu var
                    break;
                }
        }

        return isTrainingAvailable;

    }

    public void calculateDefaultControl(@Nonnull TrainingModel trainingModel) {

        // Geçme notu boş veya 0'a eşit/küçük
        String average = Objects.requireNonNull(trainingModel.getAverage().getValue());
        if (validator.averageError(average)) trainingModel.setAverage(Constant.DEFAULT_AVERAGE); // "Geçme Notu" değerini 40'a eşitle.

        /**
         * Vize/Final notu boş veya 0'a eşit/küçük,
         * Veya Vize+Final toplamı 100'e eşit değil
         */
        if (validator.examOrTotalError(trainingModel.getMidtermExam().getValue(), trainingModel.getFinalExam().getValue())) {

            trainingModel.setMidtermExam(Constant.DEFAULT_MIDTERM_EXAM); // Vize ortalaması %30'a eşitle.

            trainingModel.setFinalExam(Constant.DEFAULT_FINAL_EXAM); // Final ortalaması %70'e eşitle.

        }

    }

    public String calculateResult(@Nonnull String training, Double midtermExam, Double finalExam, Integer average) {

        // Not yok ise sonuç kısmını empty bırakılacak
        String result = "";

        if (!training.isEmpty()) {

            // Gerekli not hesaplanıyor
            double calculateResult = calculater.calculateResult(training, midtermExam, finalExam, average);

            // Gereken Puan (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
            int requiredScore = calculater.calculateRequiredScore(calculateResult);

            // Gereken soru sayısı (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
            int requiredQuestion = calculater.calculateRequiredQuestion(calculateResult);

            // Çıktı yazısı hazırlanıyor
            result = validator.selectOutput(
                    requiredScore,
                    String.format(Locale.getDefault(), Constant.CALCULATE_MESSAGE, requiredScore, requiredQuestion),
                    Constant.FAILED_MESSAGE
            );

        }

        return result;

    }

    @Override
    public void savedDataControl(@Nullable TrainingDbModel trainingDbModel) {

        if (trainingDbModel != null) {
            Type arrayType = new TypeToken<ObservableArrayList<String>>() {}.getType();

            ObservableArrayList<String> arrayTraining = new Gson().fromJson(trainingDbModel.getTrainings(), arrayType);

            ObservableArrayList<String> arrayTrainingResult = new Gson().fromJson(trainingDbModel.getTrainingResults(), arrayType);

            repository.getTrainingModel().setArrayTraining(arrayTraining);

            repository.getTrainingModel().setArrayTrainingResult(arrayTrainingResult);

            repository.getTrainingModel().setMidtermExam(trainingDbModel.getMidtermExam());

            repository.getTrainingModel().setFinalExam(trainingDbModel.getFinalExam());

            repository.getTrainingModel().setAverage(trainingDbModel.getAverage());

        }

        updateView(repository.getTrainingModel());

    }

    @Override
    public void checkSavedDataInDb() { repository.checkSavedDataInDb(); }

    @Override
    public void deleteSavedDataInDb() { repository.deleteSavedDataInDb(); }

    @Override
    public void saveData() { repository.saveData(); }

    @Override
    public void saveResult(Boolean isSuccess) { view.saveResult(isSuccess); }

    @Override
    public void deleteResult(Boolean isSuccess) { view.deleteResult(isSuccess); }

    @Override
    public void updateView(TrainingModel trainingModel) { view.updateView(trainingModel); }

}
