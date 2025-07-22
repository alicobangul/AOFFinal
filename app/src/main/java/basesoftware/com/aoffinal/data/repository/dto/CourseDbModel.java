package basesoftware.com.aoffinal.data.repository.dto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "CourseTable")
public class CourseDbModel implements Serializable {

    @PrimaryKey()
    @ColumnInfo(name = "courseindex") // Dersin sırası
    private Integer courseIndex;

    @ColumnInfo(name = "midtermexamaverage") // Vize ortalaması (Ayar)
    private String midtermExamAverage;

    @ColumnInfo(name = "finalexamaverage") // Final ortalaması (Ayar)
    private String finalExamAverage;

    @ColumnInfo(name = "successaverage") // Geçmek notu (Ayar)
    private String successAverage;

    @ColumnInfo(name = "grade") // Dersin notu
    private String grade;

    @ColumnInfo(name = "neededgrade") // Dersi geçmek için gereken not
    private String neededGrade;

    @ColumnInfo(name = "requiredquestioncount") // Çözülecek soru sayısı
    private String requiredQuestionCount;

    @ColumnInfo(name = "difficultylevel") // Zorluk derecesi (Gereken not ve çözülecek soru sayısı üzerinden) [1-2-3] [0-30 - 31-50 - 51-100]
    private int difficultyLevel;

    public CourseDbModel(
            Integer courseIndex,
            String midtermExamAverage,
            String finalExamAverage,
            String successAverage,
            String grade,
            String neededGrade,
            String requiredQuestionCount,
            int difficultyLevel) {
        this.courseIndex = courseIndex;
        this.midtermExamAverage = midtermExamAverage;
        this.finalExamAverage = finalExamAverage;
        this.successAverage = successAverage;
        this.grade = grade;
        this.neededGrade = neededGrade;
        this.requiredQuestionCount = requiredQuestionCount;
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getCourseIndex() { return courseIndex; }

    public String getMidtermExamAverage() { return midtermExamAverage; }

    public String getFinalExamAverage() { return finalExamAverage; }

    public String getSuccessAverage() { return successAverage; }

    public String getGrade() { return grade; }

    public String getNeededGrade() { return neededGrade; }

    public String getRequiredQuestionCount() { return requiredQuestionCount; }

    public int getDifficultyLevel() { return difficultyLevel; }

}
