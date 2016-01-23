package data;

import java.util.Collection;

public interface IDataHandler {

	void closeDatabaseConnection() throws IllegalStateException;

	Account getAccountByID(int id) throws IllegalArgumentException;

	User getUserByID(int id) throws IllegalArgumentException;

	Transaction getTransactionByID(int id) throws IllegalArgumentException;

	Account createAccount(String name, String type, double accountBalance, int ownerID) throws IllegalStateException;

	User createUser(String name, String email, String password) throws IllegalStateException;

	Transaction createTransaction(int fromAccountID, int toAccountID, int userID, double amount)
			throws IllegalStateException;

	Collection<Account> getAllAccountFromUser(int userID) throws IllegalStateException;

	Collection<Transaction> getAllTransactionFromUser(int userID) throws IllegalStateException;

	Collection<Transaction> getAllTransactionFromAccount(int accountID) throws IllegalStateException;

	User getUserLogin(String name, String password) throws IllegalStateException;
}