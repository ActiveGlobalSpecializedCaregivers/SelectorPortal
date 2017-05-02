package com.toptal.migration.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * <code>CandidateEvaluation</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateEvaluation {
    private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    private String prospectId;
    private Date evaluationDate;
    private String evaluationName;
    private String evaluationNote;
    private String skillName;
    private String skillWeight;
    private String skillRating;
    private String ratingComment;

    public CandidateEvaluation(String[] fields) {
        prospectId = fields[0];
        evaluationDate = DateUtils.parse(fields[3], dateParser);
        evaluationName = fields[4];
        evaluationNote = StringUtil.stripQuotes(fields[5]);
        skillName = fields[6];
        skillWeight = fields[7];
        skillRating = fields[8];
        ratingComment = fields[9];
    }

    public static SimpleDateFormat getDateParser() {
        return dateParser;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public String getEvaluationName() {
        return evaluationName;
    }

    public String getEvaluationNote() {
        return evaluationNote;
    }

    public String getProspectId() {
        return prospectId;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillRating() {
        return skillRating;
    }

    public String getSkillWeight() {
        return skillWeight;
    }
}
