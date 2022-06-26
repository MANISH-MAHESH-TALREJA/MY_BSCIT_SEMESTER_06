package net.manish.mybscitsem06;

public class QuizModel
{
    final String question;
    final String option01;
    final String option02;
    final String option03;
    final String option04;
    final String answer;

    public QuizModel(String question, String option01, String option02, String option03, String option04, String answer)
    {
        this.question = question;
        this.option01 = option01;
        this.option02 = option02;
        this.option03 = option03;
        this.option04 = option04;
        this.answer = answer;
    }

    public String getAnswer()
    {
        return answer;
    }

    public String getOption01()
    {
        return option01;
    }

    public String getOption02()
    {
        return option02;
    }

    public String getOption03()
    {
        return option03;
    }

    public String getOption04()
    {
        return option04;
    }

    public String getQuestion()
    {
        return question;
    }
}
