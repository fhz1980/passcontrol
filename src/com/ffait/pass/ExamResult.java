package com.ffait.pass;

public class ExamResult
{
    public int point;
    public String projectName;

    public ExamResult() {
    }

    public ExamResult(int point, String projectName) {
        this.point = point;
        this.projectName = projectName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "point=" + point +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}