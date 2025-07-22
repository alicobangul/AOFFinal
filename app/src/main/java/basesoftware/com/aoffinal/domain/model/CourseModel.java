package basesoftware.com.aoffinal.domain.model;

import java.io.Serializable;

public class CourseModel implements Serializable {

    // Dersin sırası
    private final Integer courseIndex;

    // Dersin notu
    private String grade = "";

    // Dersi geçmek için gereken not
    private String neededGrade = "";

    // Çözülecek soru sayısı
    private String requiredQuestionCount = "";

    // Zorluk derecesi (Gereken not ve çözülecek soru sayısı üzerinden) [1-2-3] [0-35 - 35-65 - 65-100]
    private int difficultyLevel = 3;

    public CourseModel(Integer courseIndex, String grade, String neededGrade, String requiredQuestionCount, int difficultyLevel) {
        this.courseIndex = courseIndex;
        this.grade = grade;
        this.neededGrade = neededGrade;
        this.requiredQuestionCount = requiredQuestionCount;
        this.difficultyLevel = difficultyLevel;
    }

    public CourseModel(Integer courseIndex) { this.courseIndex = courseIndex; }

    public Integer getCourseIndex() {
        return courseIndex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNeededGrade() {
        return neededGrade;
    }

    public String getRequiredQuestionCount() {
        return requiredQuestionCount;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

}