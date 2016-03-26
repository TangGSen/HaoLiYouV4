package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sen on 2016/3/14.
 */
public class ExamAnswerJsonBean implements Serializable {
    private List<ExamUserAnswer> answer;

    public List<ExamUserAnswer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<ExamUserAnswer> answer) {
        this.answer = answer;
    }
}
