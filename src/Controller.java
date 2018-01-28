
public class Controller {
	public static void main(String[] args) {
		// Check to make sure program has been called with correct number of
		// command line arguments
		
		if (args.length != 3) {
			System.err.println("Error: program should take exactly three command line arguments:");
			System.err.println("\t<No. of card holders> <main acct starting bal.> <backup acct. starting bal.>");
			System.exit(0);
		}
		
		// And then make sure that those args are all integers
		try {
			int numCards = Integer.parseInt(args[0]);
			
			// Create accounts
			Account mainAccount = new Account("Main", Integer.parseInt(args[1]));
			Account backupAccount = new Account("Backup", Integer.parseInt(args[2]));
			
			System.out.println("Starting Threads");
			
			// Create and start threads
			for(int i = 0; i < numCards; i++) {
				Thread t = new Thread(new CardHolder(i, mainAccount));
		        t.start();
			}
			
			// Create and start Monitor thread
			Thread mt = new Thread(new Monitor(mainAccount, backupAccount));
			mt.start();
			
			// Wait until Monitor thread has stopped
			mt.join();
			
			// Once Monitor thread has stopped, print out account statements
			System.out.println("Complete");
			mainAccount.printStatement();
			backupAccount.printStatement();
			
		} catch (NumberFormatException | InterruptedException e) {
			System.err.println("All three arguments should be integers");
			System.err.println("\t<No. of card holders> <main acct starting bal.> <backup acct. starting bal.>");
		}
	}
}
