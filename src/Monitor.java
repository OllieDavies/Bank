/**
 * Used to monitor the state of CardHolder threads
 * Terminates when Thread.activeCount less than or equal to 1 during which time the Controller will be the only Thread active
 * @author Oliver Davies
 */
public class Monitor implements Runnable {
	Account mainAccount;
	Account backupAccount;
	
	/**
	 * Contructor for the Monitor class
	 * @param mainAccount Account to check the funds are within a range
	 * @param backupAccount Account to transfer money to/from if mainAccount is outside of range
	 */
	Monitor(Account mainAccount, Account backupAccount){
		this.mainAccount = mainAccount;
		this.backupAccount = backupAccount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("MONITOR THREAD started");
		// Check if any threads are still running
		while(Thread.activeCount() > 2) {
			// Check if mainAccount balance is within range via Account.transfer function
			mainAccount.transfer(backupAccount);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
