public class Threshold
{
    private double lambda;
    private double mu;
    private double threshold;

    public Threshold()
    {
        lambda = 0.3;
        mu = 0.2;
        threshold = 0.0;
    }

    public boolean update(double t)
    {
        //IF LESS THAN, RETURN true
        if(threshold<=t)
        {
            threshold = threshold + lambda*t;
            return true;
        }
        //IF GREATER, RETURN false
        else
        {
            threshold = threshold - mu*t;
            return false;
        }
		//return false;
		
    }
}
