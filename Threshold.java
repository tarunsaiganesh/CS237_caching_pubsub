public class Threshold
{
    private double lambda;
    private double mu;
    private double threshold;
	private int itr;
	private double sum;
	private int batch_sz;
	private double prev_val;
	private double gamma;
	private double alpha;

    public Threshold()
    {
        lambda = 0.3;
        mu = 0.2;
        threshold = 0.0;
		itr = 0;
		sum = 0.0;
		batch_sz = 50;
		prev_val = 0;
		gamma = 0.7;
		alpha = 0.3;
    }

	public boolean policy_1(double t){
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
	}

	public boolean policy_2(double t){
		sum = sum + t;
		itr++;

		if(itr % (batch_sz) == 1){
			System.out.println("Threshold: " + threshold);
			threshold = sum/batch_sz;
			sum = 0;
		}
		
		if(threshold<=t)
        {
            return true;
        }
        //IF GREATER, RETURN false
        else
        {
            return false;
        }


	}

	public boolean policy_3(double t){
		threshold = gamma * t + alpha * prev_val;
		prev_val = t;
		
		if(threshold<=t)
        {
            return true;
        }
        //IF GREATER, RETURN false
        else
        {
            return false;
        }
	}

    public boolean update(double t, int policy)
    {
		switch(policy){
			case 1: 
				return false;
			case 2:
        		return true;
			case 3:
				return policy_1(t);
			case 4:
				return policy_2(t);
			case 5:
				return policy_3(t);
			default:
				return false;
		}
		
		
    }
}
