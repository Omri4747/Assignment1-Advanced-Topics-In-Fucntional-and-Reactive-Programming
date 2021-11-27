package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class InputDetails {
    public static class ComputerInput{
        @SerializedName("Type")
        String type;
        @SerializedName("Sig Success")
        long sigSuccess;
        @SerializedName("Sig Fail")
        long sigFail;

        public Computer createComputer(){
            return new Computer(type, sigFail, sigSuccess);
        }

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
        @SerializedName("Prerequisites")
        List<String> prerequisites;
        @SerializedName("Student")
        String studentId;
        @SerializedName("Students")
        List<String> studentsId;
        @SerializedName("Computer")
        String computerType;
        @SerializedName("Conditions")
        List<String> conditions;
        @SerializedName("Grade")
        int[] grade;
        @SerializedName("Preferences")
        Queue<String> preferences;


        public String toString() {
            return "\nActionsInput{" +
                    "action='" + action + '\'' +
                    ", department='" + department + '\'' +
                    ", course='" + course + '\'' +
                    ", space=" + space +
                    ", prerequisites=" + prerequisites +
                    ", studentId='" + studentId + '\'' +
                    ", studentsId=" + studentsId +
                    ", computerType='" + computerType + '\'' +
                    ", conditions=" + conditions +
                    ", grade=" + Arrays.toString(grade) +
                    ", preferences=" + preferences+
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
