package basesoftware.com.aoffinal.model;

import androidx.databinding.ObservableArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import basesoftware.com.aoffinal.impl.Contract;
import basesoftware.com.aoffinal.model.domain.TrainingModel;
import basesoftware.com.aoffinal.model.roomdb.TrainingDao;
import basesoftware.com.aoffinal.model.roomdb.TrainingDbModel;
import dagger.hilt.android.scopes.ActivityScoped;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class TrainingRepository {

    private final TrainingDao trainingDao;

    private final CompositeDisposable compositeDisposable;

    private final TrainingModel trainingModel;

    private Contract.Presenter presenter;

    @Inject
    public TrainingRepository(TrainingDao dao, CompositeDisposable compositeDisposable) {

        this.trainingDao = dao;

        this.compositeDisposable = compositeDisposable;

        trainingModel = new TrainingModel();

    }

    public void initBinds(Contract.Presenter presenter) { this.presenter = presenter; }

    public void textChanged(@Nonnull Integer viewTag, String viewText) {

        switch (viewTag) {

            case -1 -> trainingModel.setMidtermExam(viewText);

            case -2 -> trainingModel.setFinalExam(viewText);

            case -3 -> trainingModel.setAverage(viewText);

            default -> trainingModel.trainingDataChanged(viewTag, viewText);

        }

    }

    public void deleteSavedDataInDb() {

        compositeDisposable.clear();

        compositeDisposable.add(
                trainingDao.deleteAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> presenter.deleteResult(true), e -> presenter.deleteResult(false)));

    }

    public void checkSavedDataInDb() {

        compositeDisposable.clear();

        compositeDisposable.add(trainingDao.getAll() // Veri alma fonksiyonu çağırıldı
                .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                .subscribe(this::checkSavedData, e -> checkSavedData(null)) // İşlem tamamlandığında çalıştırılacak fonksiyon
        );

    }

    private void checkSavedData(@Nullable TrainingDbModel trainingDbModel) {

        if (trainingDbModel != null) {
            Type arrayType = new TypeToken<ObservableArrayList<String>>() {}.getType();

            ObservableArrayList<String> arrayTraining = new Gson().fromJson(trainingDbModel.getTrainings(), arrayType);

            ObservableArrayList<String> arrayTrainingResult = new Gson().fromJson(trainingDbModel.getTrainingResults(), arrayType);

            trainingModel.setArrayTraining(arrayTraining);

            trainingModel.setArrayTrainingResult(arrayTrainingResult);

            trainingModel.setMidtermExam(trainingDbModel.getMidtermExam());

            trainingModel.setFinalExam(trainingDbModel.getFinalExam());

            trainingModel.setAverage(trainingDbModel.getAverage());

        }

        presenter.updateView(trainingModel);

    }

    public void saveData() {

        compositeDisposable.clear();

        TrainingDbModel trainingDbModel = new TrainingDbModel(
                trainingModel.getMidtermExam(),
                trainingModel.getFinalExam(),
                trainingModel.getAverage(),
                new Gson().toJson(trainingModel.getArrayTraining()),
                new Gson().toJson(trainingModel.getArrayTrainingResult())
        );

        compositeDisposable.add(
                trainingDao.insert(trainingDbModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> presenter.saveResult(true), e -> presenter.saveResult(false)));

    }

    public void trainingCalculate() {

        calculateControl();

        int average = Integer.parseInt(trainingModel.getAverage());

        double midtermExam = (double) Integer.parseInt(trainingModel.getMidtermExam()) / 100;

        double finalExam = (double) Integer.parseInt(trainingModel.getFinalExam()) / 100;

        for (int i = 0; i < trainingModel.getArrayTraining().size(); i++) {

            // Kullanıcıdan gelen not
            String training = trainingModel.getArrayTraining().get(i);

            // Not yok ise sonuç kısmını boş bırak
            if (training.isEmpty()) trainingModel.resultDataChanged(i, "");

            else {

                // Gerekli not hesaplanıyor
                double calculateResult = (average - (Double.parseDouble(training) * midtermExam)) / finalExam;

                // Eğer double küsüratı iki haneden büyük ise iki haneye düşürüldü
                // String tipindeki dönüşte , işareti . ile değiştirildi [double çevirme işlemi için]
                calculateResult = Double
                        .parseDouble((
                                new DecimalFormat((calculateResult < 10.00) ? "#.##" : "##.##")
                                        .format(calculateResult)).replace(",",".")
                        );

                // Sonuç küsüratlı sayı, yukarı yuvarlandı örn: 65.10 -> 66
                if (calculateResult % 1 != 0) calculateResult = Math.ceil(calculateResult);

                // Sonuç 5'e tam bölünmüyorsa 5 ve katına yuvarla [Her soru 5 puan]
                if (((int) calculateResult) % 5 > 0) calculateResult = calculateResult + (5 - calculateResult % 5);

                // Gereken Puan (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
                int requiredScore = Math.max((int) calculateResult, 0);

                // Gereken soru sayısı (Kullanıcının vize ortalaması eğer geçer notu fazlasıyla karşılıyor ise 0 yaz)
                int requiredQuestion = Math.max((int) calculateResult / 5, 0);

                // Çıktı yazısı hazırlanıyor
                String result = (requiredScore > 100) ? "Gereken Not : +100 (KALDINIZ) - Gereken Soru (20 üzerinden) : +20 (KALDINIZ)" :
                        String.format(Locale.getDefault(), "Gereken Not : %d - Gereken Soru (20 üzerinden) : %d", requiredScore, requiredQuestion);

                // Ekranda gösterilecek listeye çıktı eklendi
                trainingModel.resultDataChanged(i, result);

            }

        }

        presenter.calculateComplete();

    }

    private void calculateControl() {

        // Geçme notu boş veya 0'a eşit/küçük
        boolean avarageError = trainingModel.getAverage().isEmpty() || Integer.parseInt(trainingModel.getAverage()) <= 0;

        // Vize ortalaması boş veya 0'a eşit/küçük
        boolean unknownMidterm = trainingModel.getMidtermExam().isEmpty() || Integer.parseInt(trainingModel.getMidtermExam()) <= 0;

        // Final ortalaması boş veya 0'a eşit/küçük
        boolean unknownFinal = trainingModel.getFinalExam().isEmpty() || Integer.parseInt(trainingModel.getFinalExam()) <= 0;

        if (avarageError) trainingModel.setAverage("40"); // "Geçme Notu" değerini 40'a eşitle.

        if ((unknownMidterm || unknownFinal) || (Integer.parseInt(trainingModel.getMidtermExam()) + Integer.parseInt(trainingModel.getFinalExam()) != 100)) {

            trainingModel.setMidtermExam("30"); // Vize ortalaması %30'a eşitle.

            trainingModel.setFinalExam("70"); // Final ortalaması %70'e eşitle.

        }

    }


}
