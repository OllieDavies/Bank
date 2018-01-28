
/**
 * Used to perform and track CardHolder transactions
 * @author Oliver Davies
 */
public class CardHolder implements Runnable {
	private int id;
	private Account account;
	final static int numIterations = 20; // Number of transactions to perform per CardHolder
	
	int totalWithdrawn = 0;
	int totalDeposited = 0;
	
	/**
	 * CardHolder constructor
	 * @param id ID of the CardHolder
	 * @param account Account to perform transactions against
	 */
	public CardHolder(int id, Account account) {
		this.id = id;
		this.account = account;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("THREAD "+ id + " started"); 
		for (int i = 0; i < numIterations; i++) {
			// Generate a random amount from 1 - 10
			int amount = (int)(Math.random() * 10) + 1;
			
			// Then with 50/50 chance, either deposit or withdraw it
			if (Math.random() > 0.5) {
				account.withdraw(id, amount);
				totalWithdrawn += amount;
			} else {
				account.deposit(id, amount);
				totalDeposited += amount;
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		
		System.out.println("THREAD "+ id + " finished with £" + (totalWithdrawn - totalDeposited)); 
	}
	
}
