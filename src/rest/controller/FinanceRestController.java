package rest.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import data.Account;
import data.DataHandler;
import data.IDataHandler;
import data.Transaction;

@Path("user")
public class FinanceRestController {

	@Path("/transaction/new")
	@POST
	public String newTransaction(@FormParam("userID") Integer userID, @FormParam("fromAccount") String fromAccount,
			@FormParam("toAccount") String toAccount, @FormParam("amount") double amount) {

		System.out.println("Hello from new Transaction method!");
		IDataHandler dataHandler = DataHandler.getInstance();
		for (Account account : dataHandler.getAllAccountFromUser(userID)) {
			if (account.getName().equalsIgnoreCase(fromAccount)) {
				int toAccountID = Integer.valueOf(toAccount.replaceAll("(.*)#", ""));
				dataHandler.createTransaction(account.getId(), toAccountID, userID, amount);
				account.debit(amount);
				dataHandler.getAccountByID(toAccountID).credit(amount);
				return "Successfully transferred!";
			}
		}

		return "Failed to transfer!";
	}

	@Path("/accounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Account> getAllAccountsByUserAsJson(@Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Integer userID = (Integer) session.getAttribute("ID");
		return DataHandler.getInstance().getAllAccountFromUser(userID);
	}

	@Path("/accounts")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getAllAccountsByUserAsHtml(@Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Integer userID = (Integer) session.getAttribute("ID");
		return AccountsToBootstrapTable(DataHandler.getInstance().getAllAccountFromUser(userID));
	}

	@Path("/transactions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Transaction> getAllTransactionsByUserAsJson(@Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Integer userID = (Integer) session.getAttribute("ID");
		return DataHandler.getInstance().getAllTransactionFromUser(userID);
	}

	@Path("/transactions")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getAllTransactionsByUserAsHtml(@Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Integer userID = (Integer) session.getAttribute("ID");
		return TransactionsToBootstrapTable(userID);
	}

	private String AccountsToBootstrapTable(Collection<Account> accounts) {
		String html = "<table class=\"table table-bordered\">";
		html += "<thead><tr><th>Name</th><th>Type</th><th>Balance</th><th>Details</th></tr></thead>";
		html += "<tbody>";

		for (Account account : accounts) {
			html += "<tr>";
			html += "<td>" + account.getName() + "</td><td>" + account.getType() + "</td><td>"
					+ account.getAccountBalance() + "</td>";
			html += "<td><button type=\"button\" class=\"btn btn-primary btn-xs\">Show</button></td>";
			html += "</tr>";
		}

		html += "</tbody></table>";
		return html;
	}

	private String TransactionsToBootstrapTable(int userID) {
		IDataHandler dataHandler = DataHandler.getInstance();

		// LinkedHashSet notwendig, weil wenn ein User zwischen seinen Accounts
		// überweist
		// würden in einer Collection 2 Einträge für dieselbe Transaction stehen
		Set<Transaction> transactions = new LinkedHashSet<Transaction>();
		for (Account account : dataHandler.getAllAccountFromUser(userID)) {
			transactions.addAll(dataHandler.getAllTransactionFromAccount(account.getId()));
		}

		// List necessarry for sorting (newest first)
		List<Transaction> t = new ArrayList<>();
		t.addAll(transactions);

		Collections.sort(t, new Comparator<Transaction>() {
			public int compare(Transaction one, Transaction other) {
				if (one.getDate().before(other.getDate())) {
					return 1;
				} else if (one.getDate().after(other.getDate())) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		String html = "<table class=\"table table-bordered\">";
		html += "<thead><tr><th>Date</th><th>From</th><th>To</th><th>Out</th><th>In</th></tr></thead>";
		html += "<tbody>";

		for (Transaction transaction : t) {
			html += "<tr>";
			html += "<td>" + DateUtility.toGmtString(transaction.getDate()) + "</td>";

			String from, to;
			double in = 0, out = 0;

			if (transaction.getUserID() == userID) {
				// from user's account
				from = dataHandler.getAccountByID(transaction.getFromAccountID()).getName();
				out = transaction.getAmount();
				if (dataHandler.getAccountByID(transaction.getToAccountID()).getOwnerID() == userID) {
					// from & to user's account
					to = dataHandler.getAccountByID(transaction.getToAccountID()).getName();
					in = transaction.getAmount();
				} else {
					to = dataHandler.getUserByID(dataHandler.getAccountByID(transaction.getToAccountID()).getOwnerID())
							.getName();
				}
			} else {
				// to user's account
				from = dataHandler.getUserByID(dataHandler.getAccountByID(transaction.getFromAccountID()).getOwnerID())
						.getName();
				to = dataHandler.getAccountByID(transaction.getToAccountID()).getName();
				in = transaction.getAmount();
			}

			html += "<td>" + from + "</td><td>" + to + "</td>";

			if (out != 0) {
				html += "<td>" + out + "</td>";
			} else {
				html += "<td></td>";
			}

			if (in != 0) {
				html += "<td>" + in + "</td>";
			} else {
				html += "<td></td>";
			}

			html += "</tr>";
		}

		html += "</tbody></table>";
		return html;
	}

}
