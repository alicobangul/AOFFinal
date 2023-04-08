package basesoftware.com.aoffinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import basesoftware.com.aoffinal.databinding.ActivityMainBinding;
import basesoftware.com.aoffinal.model.TrainingModel;
import basesoftware.com.aoffinal.roomdb.TrainingDao;
import basesoftware.com.aoffinal.roomdb.TrainingDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * <h2>AOF FINAL - JAVA Language</h2>
 * <hr>
 *
 * <ul>
 * <li>RxJAVA</li>
 * <li>Room</li>
 * <li>ViewBinding</li>
 * <br />
 * <li>inApp-Review</li>
 * <li>inApp-Update</li>
 * </ul>
 */

public class MainActivity extends AppCompatActivity {

    double training;
    double calculateResult;
    int resultText;
    int average;
    int question;
    boolean emptyData;

    ArrayList<EditText> arrayTraining;
    ArrayList<TextView> arrayTrainingResult;

    private final int REQUEST_CODE = 11;
    private final int IMMEDIATE = AppUpdateType.IMMEDIATE;
    private final int UPDATE_AVAILABLE = UpdateAvailability.UPDATE_AVAILABLE;

    ReviewManager reviewManager;
    ReviewInfo reviewInfo;

    private ActivityMainBinding binding;

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private TrainingDao trainingDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();

        btnListener();

        callAppUdate();

        getSavedDataInDb();

    }


    public void callAppUdate(){

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnCompleteListener(task -> {

            if(task.isSuccessful() && task.getResult().updateAvailability() == UPDATE_AVAILABLE && task.getResult().isUpdateTypeAllowed(IMMEDIATE)) {
                try { appUpdateManager.startUpdateFlowForResult(task.getResult(), IMMEDIATE, MainActivity.this, REQUEST_CODE); } // IMMEDIATE türünde güncellemeyi başlat
                catch (IntentSender.SendIntentException e) {e.getStackTrace();}
            }
            else {
                adMob();
                getSavedDataInDb();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode != RESULT_OK) {
            adMob();
            Log.d("updateTag", "UpdateFailed" + resultCode); // inApp-Update başarısız oldu
        }
    }



    public void initialize() {

        TrainingDatabase trainingDb = Room.databaseBuilder(getApplicationContext(), TrainingDatabase.class, "Training").allowMainThreadQueries().build(); // Veritabanı tanımlandı

        trainingDao = trainingDb.trainingDao(); // DAO tanımlandı

        emptyData = true; // Şuan bütün alanlar boş olarak kabul ediliyor

        arrayTraining = new ArrayList<>(); // Vize notlarının döngüsü için arraylist oluşturuldu
        arrayTraining.add(binding.txtTraining1);
        arrayTraining.add(binding.txtTraining2);
        arrayTraining.add(binding.txtTraining3);
        arrayTraining.add(binding.txtTraining4);
        arrayTraining.add(binding.txtTraining5);
        arrayTraining.add(binding.txtTraining6);
        arrayTraining.add(binding.txtTraining7);
        arrayTraining.add(binding.txtTraining8);

        arrayTrainingResult = new ArrayList<>(); // Final sonuçlarının döngüsü için arraylist oluşturuldu
        arrayTrainingResult.add(binding.txtResult1);
        arrayTrainingResult.add(binding.txtResult2);
        arrayTrainingResult.add(binding.txtResult3);
        arrayTrainingResult.add(binding.txtResult4);
        arrayTrainingResult.add(binding.txtResult5);
        arrayTrainingResult.add(binding.txtResult6);
        arrayTrainingResult.add(binding.txtResult7);
        arrayTrainingResult.add(binding.txtResult8);

    }


    public void getSavedDataInDb() {

        mDisposable.add(trainingDao.getAll() // Veri alma fonksiyonu çağırıldı
                .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                .subscribe(this::checkSavedData) // İşlem tamamlandığında çalıştırılacak fonksiyon
        );

    }

    public void checkSavedData(List<TrainingModel> trainingList) {

        mDisposable.clear(); // CompositeDisposable temizlendi

        if (!trainingList.isEmpty()) {
            Snackbar
                    .make(binding.getRoot(), "Kayıtlı notlar getirilsin mi ?", Snackbar.LENGTH_LONG)
                    .setAction("EVET", v -> {
                        try {
                            for (int i = 0; i < trainingList.size(); i++) {
                                arrayTraining.get(i).setText(String.valueOf(trainingList.get(i).training)); // Kayıtlı not getirildi
                                arrayTrainingResult.get(i).setText(String.valueOf(trainingList.get(i).trainingResult)); // Kayıtlı hesaplanmış not getirildi
                            }
                        }
                        catch (Exception e) { e.getStackTrace(); }

                    })
                    .show();
        }
    }

    public void deleteSavedDataInDb() {

        mDisposable.clear(); // CompositeDisposable temizlendi

        mDisposable.add(trainingDao.delete() // Veri silme fonksiyonu çağırıldı
                .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                .subscribe(this::deleteSavedDataStatus) // İşlem tamamlandığında çalıştırılacak fonksiyon
        );

    }

    @SuppressLint("SetTextI18n")
    public void deleteSavedDataStatus() {

        mDisposable.clear(); // CompositeDisposable temizlendi

        Snackbar.make(binding.getRoot(), "Kayıtlı notlar silindi", Snackbar.LENGTH_SHORT).show(); // Not silindi ui bilgisi

        Log.i("Delete-Data", "Kayıtlı notlar silindi"); // Log yazıldı

        for (int i = 0; i < arrayTraining.size(); i++) {
            arrayTraining.get(i).getText().clear(); // İlgili not componenti temizlendi
            arrayTrainingResult.get(i).setText("Min.Final Notu"); // İlgili not hesaplama componenti defaulta getirildi
        }

    }

    public void saveData() {

        for (int i = 0; i < arrayTraining.size(); i++) {

            trainingDao.insert(new TrainingModel(i, i, arrayTraining.get(i).getText().toString(), arrayTrainingResult.get(i).getText().toString())) // Yazılacak veri modellendi
                    .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                    .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                    .subscribe();

        }

    }


    @SuppressLint("MissingPermission")
    public void adMob() {

        MobileAds.initialize(this, initializationStatus -> {}); // Ads initialize edildi

        AdRequest adRequest = new AdRequest.Builder().build(); // Reklam isteği için AdRequest oluşturuldu

        AdView adView = new AdView(this); // AdView oluşturuldu
        adView.setAdSize(AdSize.BANNER); // Banner boyutu ayarlandı
        adView.setAdUnitId(BuildConfig.BANNERID); // Banner id verildi

        binding.linearAd.addView(adView); // AdView layout içerisine eklendi

        adView.loadAd(adRequest); // Reklam isteği yapıldı

    }


    @SuppressLint("SetTextI18n")
    public void btnListener(){

        binding.btnCalculate.setOnClickListener(view -> {

            if(binding.txtAverage.getText().length() != 0) average = Integer.parseInt(binding.txtAverage.getText().toString()); // Girilen "gerekli ortalama" değerini al
            else {

                binding.txtAverage.setText("40"); // Gerekli ortalama verisi yok default olarak 40 al

                average = 40; // Ortalamayı eşitle

                Toast.makeText(MainActivity.this, "Geçme notu 40 olarak alınmıştır", Toast.LENGTH_LONG).show(); // Bilgilendirme yapıldı
            }

            for (int i = 0; i < arrayTraining.size(); i++) {

                if(arrayTraining.get(i).getText().length() != 0) {

                    emptyData = false;

                    training = Double.parseDouble(arrayTraining.get(i).getText().toString()); // Vize notu alındı

                    calculateResult = (average - (training * 0.30)) / 0.70; // Vize notunun %30'u alındı gerekli not hesaplanıyor

                    // Eğer double küsüratı iki haneden büyük ise iki haneye düşürüldü
                    // String tipindeki dönüşte , işareti . ile değiştirildi [double çevirme işlemi için]
                    calculateResult = Double.parseDouble((new DecimalFormat( (calculateResult < 10.00) ? "#.##" : "##.##" ).format(calculateResult)).replace(",","."));

                    if(calculateResult % 1 != 0) calculateResult = Math.ceil(calculateResult); // Sonuç küsüratlı sayı, yukarı yuvarlandı örn: 65.10 -> 66

                    if(((int) calculateResult) % 5 > 0) calculateResult = calculateResult + (5 - calculateResult % 5); // Sonuç 5'e tam bölünmüyorsa 5 ve katına yuvarla [Her soru 5 puan]

                    resultText = (int) calculateResult; // Çıktı int tipine çevrildi

                    question = resultText / 5; // Yapılması gereken soru sayısı

                    arrayTrainingResult.get(i).setText(String.format(Locale.getDefault(), "Gereken Not : %d \n(%d Soru)", resultText,question)); // Çıktı yazdırıldı

                    training = 0; // Ders notu döngü için sıfırlandı

                    calculateResult = 0; // Sonuç döngü için sıfırlandı

                }

            }

            userComment();

        });

        binding.btnClearData.setOnClickListener(view -> Snackbar
                .make(binding.getRoot(), "Kayıtlı notlar silinsin mi ?", Snackbar.LENGTH_LONG)
                .setAction("EVET", v -> deleteSavedDataInDb())
                .show());
    }

    public void userComment() {

        reviewManager = ReviewManagerFactory.create(MainActivity.this); 
        final Task<ReviewInfo> requast = reviewManager.requestReviewFlow();

        requast.addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);

                flow.addOnCompleteListener(result -> {

                    Log.i("UX - Review", "inApp-Review Başarılı");

                    saveQuestion();

                });

            }

            else saveQuestion();

        });
    }

    public void saveQuestion() {

        new Handler(Looper.myLooper()).postDelayed(() -> Snackbar
                .make(binding.getRoot(), "Notlar kaydedilsin mi ?", Snackbar.LENGTH_LONG)
                .setAction("EVET", v -> saveData())
                .show(), 500);

    }

}
