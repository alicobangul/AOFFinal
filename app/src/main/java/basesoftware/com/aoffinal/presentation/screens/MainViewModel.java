package basesoftware.com.aoffinal.presentation.screens;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.callback.Result;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.domain.model.CourseModel;
import basesoftware.com.aoffinal.domain.use_case.DeleteDataUseCase;
import basesoftware.com.aoffinal.domain.use_case.GetDataUseCase;
import basesoftware.com.aoffinal.domain.use_case.SaveDataUseCase;
import basesoftware.com.aoffinal.domain.util.Calculater;
import basesoftware.com.aoffinal.domain.validator.Validator;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final SavedStateHandle savedStateHandle;

    private final GetDataUseCase getDataUseCase;
    private final SaveDataUseCase saveDataUseCase;
    private final DeleteDataUseCase deleteDataUseCase;

    private final Validator validator;

    private final Calculater calculater;

    private final MutableLiveData<MainScreenDialog> mainScreenDialog = new MutableLiveData<>(MainScreenDialog.EMPTY);

    private final MutableLiveData<Boolean> shareDialog = new MutableLiveData<>(false);

    @Inject
    public MainViewModel(
            SavedStateHandle savedStateHandle,
            GetDataUseCase getDataUseCase,
            SaveDataUseCase saveDataUseCase,
            DeleteDataUseCase deleteDataUseCase,
            Validator validator,
            Calculater calculater
    ) {
        this.savedStateHandle = savedStateHandle;
        this.getDataUseCase = getDataUseCase;
        this.saveDataUseCase = saveDataUseCase;
        this.deleteDataUseCase = deleteDataUseCase;
        this.validator = validator;
        this.calculater = calculater;
    }

    public LiveData<MainScreenSate> getState() { return savedStateHandle.getLiveData("STATE", new MainScreenSate()); }

    public void updateState(MainScreenSate state) { savedStateHandle.set("STATE", state); }


    public LiveData<MainScreenDialog> getMainScreenDialog() { return mainScreenDialog; }

    private void showDialog(MainScreenDialog dialog) { mainScreenDialog.setValue(dialog); }


    public LiveData<Boolean> getShareDialog() { return shareDialog; }


    public void inAppUpdateComplete() { checkDataInDb(); }

    private void checkDataInDb() {

        getDataUseCase.execute(new ResultCallBack<>() {
            @Override
            public void onSuccess(List<CourseDbModel> result) {

                Log.i("DATABASE_SUCCESS", "MainViewModel_checkDataInDb() / VERİ KONTROLÜ / Veritabanında veri " + (result.isEmpty() ? "yok" : "mevcut ve alındı"));

                if (!result.isEmpty()) createViewStateWithDbData(result);

            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                assert throwable != null;
                Log.e("DATABASE_ERROR", "MainViewModel_checkDataInDb() / VERİ KONTROLÜ / Veritabanından veri alma başarısız / onFailure:"+ "\n" + throwable.getMessage());
            }

        });

    }

    private void createViewStateWithDbData(@NonNull List<CourseDbModel> result) {

        MainScreenSate state = new MainScreenSate();

        // İlk gelen veri üzerinden state modelinde değerler atanıyor
        state.setMidtermExamAverage(result.get(0).getMidtermExamAverage());
        state.setFinalExamAverage(result.get(0).getFinalExamAverage());
        state.setSuccessAverage(result.get(0).getSuccessAverage());

        // Veritabanından alınan veriler state modelinde uygun yerlere yerleştirildi
        for (CourseDbModel course : result) {
            state.setCourses(
                    course.getCourseIndex(),
                    new CourseModel(
                            course.getCourseIndex(),
                            course.getGrade(),
                            course.getNeededGrade(),
                            course.getRequiredQuestionCount(),
                            course.getDifficultyLevel()
                    )
            );
        }

        savedStateHandle.set("STATE", state); // View güncellendi

    }



    public void settingsTextChanged(CharSequence text, int settingsIndex) {

        // Ekranın üstündeki ayarların input değişikliği

        MainScreenSate state = Objects.requireNonNull(getState().getValue());
        String input = (text != null) ? text.toString() : "";

        switch (settingsIndex) {
            case 1 -> state.setMidtermExamAverage(validator.checkChangedSettingsData(input));
            case 2 -> state.setFinalExamAverage(validator.checkChangedSettingsData(input));
            case 3 -> state.setSuccessAverage(validator.checkChangedSettingsData(input));
        }

        updateState(state);

    }

    public void courseTextChanged(CharSequence text, int courseIndex) {

        // Not input değeri değişikliği

        MainScreenSate state = Objects.requireNonNull(getState().getValue()); // Mevcut state alındı
        String input = text != null ? text.toString() : ""; // Gelen input verisi string tipine çevirildi

        state.updateCourseData(courseIndex, validator.checkChangedCourseData(input)); // State kısmındaki listede kontrol edilmiş veri ile güncelleme yapıldı
        updateState(state); // State güncellendi

    }


    // Hesaplama buton aksiyonu
    public void calculateClick() {

        MainScreenSate state = Objects.requireNonNull(getState().getValue()); // Mevcut state alındı
        boolean hasAnyGrade = state.getCourseList()
                .stream()
                .anyMatch(course -> !course.getGrade().isEmpty());

        if (hasAnyGrade) { calculateProcess(); } // Ekranda en az 1 tane ders notu var ise hesaplama yapılacak

    }

    private void calculateProcess() {

        MainScreenSate state = Objects.requireNonNull(getState().getValue()); // Mevcut state alındı

        List<String> validatedSettings = validator.validateSettingsData(
                state.getMidtermExamAverage(),
                state.getFinalExamAverage(),
                state.getSuccessAverage()
        );

        state.setAllSettingsData(validatedSettings);

        ArrayList<CourseModel> calculatedCourses = calculater.calculateCourse(
                state.getMidtermExamAverage(),
                state.getFinalExamAverage(),
                state.getSuccessAverage(),
                state.getCourseList()
        );

        state.updateCourseList(calculatedCourses);

        updateState(state); // Ayarlar kontrol edilerek state güncellendi

        showDialog(MainScreenDialog.SAVE_DATA_QUESTION);

    }

    public void deleteDbDataClick() { showDialog(MainScreenDialog.DELETE_DATA_QUESTION); }

    public void shareClick() { shareDialog.setValue(true); }

    public void saveDbDataProcess() {

        MainScreenSate state = Objects.requireNonNull(getState().getValue()); // Mevcut state alındı

        List<CourseDbModel> courses = state.getCourseDbDataList();

        saveDataUseCase.execute(courses, new ResultCallBack<>() {

            @Override
            public void onSuccess(Result result) {
                Log.i("DATABASE_SUCCESS", "MainViewModel_saveDbDataProcess() / VERİ EKLEME / Veritabanı veri ekleme işlemi başarılı");
                showDialog(MainScreenDialog.SAVE_DATA_SUCCESS);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                assert throwable != null;
                Log.e("DATABASE_ERROR", "MainViewModel_saveDbDataProcess() / VERİ EKLEME / Veritabanı veri ekleme işlemi başarısız / onFailure:"+ "\n" + throwable.getMessage());
                showDialog(MainScreenDialog.SAVE_DATA_FAIL);
            }

        });

    }

    public void deleteDbDataProcess() {

        deleteDataUseCase.execute(new ResultCallBack<>() {
            @Override
            public void onSuccess(Result result) {
                Log.i("DATABASE_SUCCESS", "MainViewModel_clearDataProcess() / VERİ SİLME / Veritabanı silme işlemi başarılı");
                showDialog(MainScreenDialog.DELETE_DATA_SUCCESS);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                assert throwable != null;
                Log.e("DATABASE_ERROR", "MainViewModel_clearDataProcess() / VERİ SİLME / Veritabanı silme işlemi başarısız / onFailure:"+ "\n" + throwable.getMessage());
                showDialog(MainScreenDialog.DELETE_DATA_FAIL);
            }

        });

    }

}