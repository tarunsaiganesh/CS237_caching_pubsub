public class Threshold
{
    private double lambda;
    private double mu;
    private double threshold;

    public Threshold()
    {
        lambda = 0.5;
        mu = 0.5;
        threshold = 0.0;
    }

    public boolean update(double t)
    {
        //IF LESS THAN, RETURN true
        if(threshold<t)
        {
            threshold = threshhold + lambda*t;
            return true;
        }
        //IF GREATER, RETURN false
        else if(threshhold>t)
        {
            threshold = threshhold - mu*t;
            return false;
        }
    }
}