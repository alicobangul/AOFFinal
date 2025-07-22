package basesoftware.com.aoffinal.presentation.screens;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.model.CourseModel;

public class MainScreenSate implements Serializable {

    private final ArrayList<CourseModel> courses = new ArrayList<>();

    // Vize ortalaması (Ayar)
    private String midtermExamAverage = "30";

    // Final ortalaması (Ayar)
    private String finalExamAverage = "70";

    // Geçmek notu (Ayar)
    private String successAverage = "40";

    public MainScreenSate() {

        // Sadece index veriliyor geri kalan tüm alanlar boş olacak
        for (int i = 1; i <= 8; i++) courses.add( new CourseModel(i) );

    }

    public CourseModel getCourses(int index) { return courses.get(index - 1); }

    public void setCourses(int index, CourseModel course) { courses.set(index - 1, course); }

    public void updateCourseData(int index, String data) {
        // Dersin notu değiştiğinde kontrol edildi, listeden mevcut veri alındı ve not kısmı güncellendi
        CourseModel course = getCourses(index);
        course.setGrade(data);
        setCourses(index, course);
    }

    public ArrayList<CourseModel> getCourseList() { return courses; }

    public void updateCourseList(ArrayList<CourseModel> arrayCourses) {
        courses.clear();
        courses.addAll(arrayCourses);
    }

    public String getMidtermExamAverage() { return midtermExamAverage; }

    public void setMidtermExamAverage(String midtermExamAverage) { this.midtermExamAverage = midtermExamAverage; }

    public String getFinalExamAverage() { return finalExamAverage; }

    public void setFinalExamAverage(String finalExamAverage) { this.finalExamAverage = finalExamAverage; }

    public String getSuccessAverage() { return successAverage; }

    public void setSuccessAverage(String successAverage) { this.successAverage = successAverage; }

    public void setAllSettingsData(@NonNull List<String> settingsData) {
        this.midtermExamAverage = settingsData.get(0);
        this.finalExamAverage = settingsData.get(1);
        this.successAverage = settingsData.get(2);
    }

    public List<CourseDbModel> getCourseDbDataList() {

        List<CourseDbModel> courseDbList = new ArrayList<>();

        for (CourseModel course : getCourseList()) {
            courseDbList.add(
                    new CourseDbModel(
                            course.getCourseIndex(),
                            getMidtermExamAverage(),
                            getFinalExamAverage(),
                            getSuccessAverage(),
                            course.getGrade(),
                            course.getNeededGrade(),
                            course.getRequiredQuestionCount(),
                            course.getDifficultyLevel()
                    )
            );
        }

        return courseDbList;
    }

}