package data;

public class InitialUpload {

	public static void main(String[] args) {
		// get handler
		IDataHandler handler = DataHandler.getInstance();

		int userID1 = handler.createUser("Daniel", "daniel.witsch@gmx.at", "Daniel").getId();
		int userID2 = handler.createUser("Lukas", "test@gmx.at", "Lukas").getId();

		int accID1 = handler.createAccount("Test1", "giro", 100.0, userID1).getId();
		int accID2 = handler.createAccount("Test2", "credit", 200.0, userID1).getId();
		int accID3 = handler.createAccount("Test3", "giro", 50.0, userID2).getId();
		
		/*
		handler.createTransaction(3, 4, 1, 10.0);
		handler.createTransaction(3, 5, 1, 5.0);
		handler.createTransaction(5, 3, 2, 20);
		*/

		handler.closeDatabaseConnection();
	}

}