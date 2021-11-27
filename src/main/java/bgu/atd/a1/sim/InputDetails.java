package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class InputDetails {
    public static class ComputerInput{
        @SerializedName("Type")
        String type;
        @SerializedName("Sig Success")
        long sigSuccess;
        @SerializedName("Sig Fail")
        long sigFail;

        @Override
        public String toString() {
            return "\nComputerInput{" +
                    "type='" + type + '\'' +
                    ", sigSuccess=" + sigSuccess +
                    ", sigFail=" + sigFail +
                    '}';
        }
    }
    public static class ActionsInput{
        @SerializedName("Action")
        String action;
        @SerializedName("Department")
        String department;
        @SerializedName("Course")
        String course;
        @SerializedName("Space")
        int space;
        @SerializedName("Prequisites")
        String[] prerequisites;
        @SerializedName("Student")
        String studentId;
        @SerializedName("Students")
        String[] studentsId;
        @SerializedName("Computer")
        String computerType;
        @SerializedName("Conditions")
        String[] conditions;
        @SerializedName("Grade")
        int[] grade;

        @Override
        public String toString() {
            return "\nActionsInput{" +
                    "action='" + action + '\'' +
                    ", department='" + department + '\'' +
                    ", course='" + course + '\'' +
                    ", space=" + space +
                    ", prerequisites=" + Arrays.toString(prerequisites) +
                    ", studentId='" + studentId + '\'' +
                    ", studentsId=" + Arrays.toString(studentsId) +
                    ", computerType='" + computerType + '\'' +
                    ", conditions=" + Arrays.toString(conditions) +
                    ", grade=" + Arrays.toString(grade) +
                    "}";
        }
    }
    public int threads;
    @SerializedName("Computers")
    public ComputerInput[] computersInput;
    @SerializedName("Phase 1")
    public ActionsInput[] phase1;
    @SerializedName("Phase 2")
    public ActionsInput[] phase2;
    @SerializedName("Phase 3")
    public ActionsInput[] phase3;

    @Override
    public String toString() {
        return "InputDetails{" +
                "threads=" + threads +
                ",\n computersInput=" + Arrays.toString(computersInput) +
                ",\n phase1=" + Arrays.toString(phase1) +
                ",\n phase2=" + Arrays.toString(phase2) +
                ",\n phase3=" + Arrays.toString(phase3) +
                '}';
    }
}
