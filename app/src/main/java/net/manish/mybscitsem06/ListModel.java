package net.manish.mybscitsem06;

public class ListModel
{
    public final String question;
    public final String selectedAnswer;
    public final String actualAnswer;
    public ListModel(String question, String selectedANS, String actualANS)
    {
        this.question = question;
        selectedAnswer = selectedANS;
        actualAnswer = actualANS;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getSelectedAnswer()
    {
        return selectedAnswer;
    }

    public String getActualAnswer()
    {
        return actualAnswer;
    }

}
