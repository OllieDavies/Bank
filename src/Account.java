import java.util.ArrayList;

/**
 * Used to control the balance via transactions through the withdraw, deposit and transfer methods.
 * @author Oliver Davies 
 */
public class Account {
	private String name;
	int balance = 0;
	int startingBalance;
	int numTransactions = 0;
	
	int minThreshold = 10;
	int maxThreshold = 100;
	
	ArrayList<String> output = new ArrayList<String>();

	/**
	 * Account constructor. Sets the balance to the relevant amount, and the thresholds 
	 * to reasonable values based on expected input
	 * @param name : Name of the account
	 * @param startingBalance : Starting balance of the account
	 */
	public Account(String name, int startingBalance) {
		balance = startingBalance;
		minThreshold = 10;
		maxThreshold = startingBalance;
		this.name = name;
	}

	/** 
	 * Deposit amount into the account. 
	 * Notifies all blocked threads after balances has been added in order to let the 
	 * check if the transaction is possible yet
	 * @param id ID of the thread performing the deposit
	 * @param amount Amount to deposit into the account
	 */
	public synchronized void deposit(int id, int amount) {
		balance += amount;
		notify(); // Notify blocked threads when deposit is mode
		numTransactions++;
		output.add(String.format("%15s %15s %10s %10s", numTransactions + "(" + id + ")", "", "£" + amount, "£" + balance));
	}
	
	/**
	 * Withdraw amount from the account.
	 * Blocks the thread if Account has insufficient funds and repeatedly checks when the
	 * thread is woken, to see if there are sufficient funds yet
	 * @param id ID of the Thread that performed the wants to withdraw funds 
	 * @param amount Amount to withdraw from the Account
	 * @see Account#deposit(int, int)
	 * @see Account#transfer(Account)
	 */
	public synchronized void withdraw(int id, int amount) {
		// If account has insufficient funds
		while(balance - amount < 0) {
			try {
				wait(); // Block thread
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		balance -= amount;
		numTransactions++;
		output.add(String.format("%15s %15s %10s %10s", numTransactions + "(" + id + ")", "£" + amount, "", "£" + balance));
	}
	
	/**
	 * Transfer money from/to Account if balance is outside of range
	 * Notifies blocked threads after balance has changed to allow them to check
	 * if they can perform their withdrawal if Account previously had insufficient
	 * funds
	 * 
	 * Note: Application can still go into starvation state in the event that
	 * backupAccount runs out of funds
	 * 
	 * @param backupAccount Account to use for backup funds
	 */
	public synchronized void transfer(Account backupAccount) {
		int difference;
		if(balance < minThreshold) {
			difference = minThreshold - balance;
			backupAccount.withdraw(-1, difference);
			deposit(-1, difference); 
			notify(); // Notify blocked threads only if account has received funds
		} else if(balance > maxThreshold) {
			difference = balance - maxThreshold;
			withdraw(-1, difference);
			backupAccount.deposit(-1, difference);
		}
	}
	
	/**
	 * Print out the statement of transactions
	 */
	public void printStatement() {
		System.out.println("Account \"" + name + "\":");
		if(output.size() == 0) {
			System.out.println("No Transactions");
		} else {
			System.out.printf("%15s %15s %10s %10s\n", "Transaction", "Withdrawal", "Deposit", "Balance");
			for(String s : output) {
				System.out.println(s);
			}
		}
	}
}
