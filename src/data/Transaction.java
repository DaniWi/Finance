package data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Transaction")
public class Transaction {
	@Id
	@GeneratedValue
	private int id;
	private int fromAccountID;
	private int toAccountID;
	private int userID;
	private double amount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromAccountID() {
		return fromAccountID;
	}

	public void setFromAccountID(int fromAccountID) {
		this.fromAccountID = fromAccountID;
	}

	public int getToAccountID() {
		return toAccountID;
	}

	public void setToAccountID(int toAccountID) {
		this.toAccountID = toAccountID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + fromAccountID;
		result = prime * result + id;
		result = prime * result + toAccountID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (fromAccountID != other.fromAccountID)
			return false;
		if (id != other.id)
			return false;
		if (toAccountID != other.toAccountID)
			return false;
		return true;
	}

}
