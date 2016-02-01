package data;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

/**
 * communication interface between the core and the data layer of our project
 * class to handle all database interactions
 * 
 * @author Witsch Daniel
 * 
 */
public class DataHandler implements IDataHandler {

	// Singleton
	private static final IDataHandler instance = new DataHandler();

	public static IDataHandler getInstance() {
		return instance;
	}

	private SessionFactory sessionFactory;
	private ServiceRegistry serviceRegistry;
	private Connection connection;

	/**
	 * Constructor for Databasehandler, it is important that only one instance
	 * of this class will be created
	 * 
	 * @throws IllegalStateException
	 *             occurs when connection not possible or URI is wrong
	 */
	private DataHandler() throws IllegalStateException {

		try {
			// connect to db
			connection = connectToDatabase();

			// create session factory
			Configuration configuration = new Configuration();
			// productive DB
			/*
			 * configuration.addAnnotatedClass(Item.class);
			 * configuration.addAnnotatedClass(Comment.class);
			 * configuration.addAnnotatedClass(User.class);
			 * configuration.addAnnotatedClass(Category.class);
			 */
			configuration.configure();
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (HibernateException e) {
			System.out.println("Hibernate problems");
			throw new IllegalStateException("Hibernate problems");
		} catch (URISyntaxException e) {
			System.out.println("wrong URI to database");
			throw new IllegalStateException("wrong URI to database");
		} catch (IllegalStateException e) {
			System.out.println("no connection to database");
			throw new IllegalStateException("no connection to database");
		} catch (Exception e) {
			System.out.println("some connection error");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IDataHandler#closeDatabaseConnection()
	 */
	@Override
	public void closeDatabaseConnection() throws IllegalStateException {
		sessionFactory.close();
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("closing connection not possible");
			throw new IllegalStateException("closing connection not possible");
		}
	}

	/**
	 * connect to the database
	 * 
	 * @return the Connection to the database
	 * @throws URISyntaxException
	 *             throw this exception when the URI to the database is wrong
	 * @throws IllegalStateException
	 *             throw this exception when it is not possible to connect
	 */
	private Connection connectToDatabase() throws URISyntaxException, IllegalStateException {

		String url = "jdbc:postgresql://localhost:5432/finance?user=postgres&password=user";
		Connection conn;
		try {
			// for connection to database on the tomcat server
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println("closing connection not possible");
			throw new IllegalStateException("closing connection not possible");
		}

		return conn;
	}

	/**
	 * open a new Session
	 * 
	 * @return the session
	 */
	private Session openSession() {

		return sessionFactory.openSession();
	}

	/**
	 * save an object to the database, when it is an entity
	 * 
	 * @param the
	 *            object of an entity
	 * @return the ID of the entity
	 * @throws IllegalStateException
	 *             commit failed by saving from object
	 */
	private Integer saveObjectToDb(Object obj) throws IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// save an object
			int id = (int) session.save(obj);

			// commit
			session.getTransaction().commit();

			return id;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("saving from object not possible");
			throw new IllegalStateException("saving from object not possible", e);
		} finally {
			// close session
			session.close();
		}

	}

	/**
	 * deletes an object from the database, when it is an entity
	 * 
	 * @param obj
	 *            the object of an entity
	 * @throws IllegalArgumentException
	 *             deletion of object failed
	 */
	private void deleteObjectFromDb(Object obj) throws IllegalArgumentException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// save an object
			session.delete(obj);

			// commit
			session.getTransaction().commit();

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();

			// deletion failed
			System.out.println("deletion of object failed");
			throw new IllegalArgumentException("deletion of object failed", e);
		} finally {
			// close session
			session.close();
		}
	}

	/**
	 * get an Object from the Database with the id
	 * 
	 * @param id
	 * @param typeParameterClass
	 *            Class type of the searched class for example (Bar.class)
	 * @return Object with the corresponding id
	 * @throws IllegalStateException
	 *             commit failed by searching for object, no object with this ID
	 *             in the database
	 */
	private <T> T searchForID(int id, Class<T> typeParameterClass) throws IllegalArgumentException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(typeParameterClass);
			cr.add(Restrictions.eq("id", id));
			List<T> results = cr.list();

			// commit
			session.getTransaction().commit();

			// only one element in the list because the id is unique
			return results.get(0);

		} catch (IndexOutOfBoundsException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("object with this ID is not in the database");
			throw new IllegalArgumentException("object with this ID is not in the database", e);
		} finally {
			// close session
			session.close();
		}
	}

	@Override
	public Account getAccountByID(int id) throws IllegalArgumentException {
		return this.<Account> searchForID(id, Account.class);
	}

	@Override
	public Transaction getTransactionByID(int id) throws IllegalArgumentException {
		return this.<Transaction> searchForID(id, Transaction.class);
	}

	@Override
	public User getUserByID(int id) throws IllegalArgumentException {
		return this.<User> searchForID(id, User.class);
	}

	@Override
	public Transaction createTransaction(int fromAccountID, int toAccountID, int userID, double amount)
			throws IllegalStateException {
		// create transaction instance
		Transaction trans = new Transaction();
		trans.setFromAccountID(fromAccountID);
		trans.setToAccountID(toAccountID);
		trans.setUserID(userID);
		trans.setAmount(amount);
		trans.setDate(new Date());
		
		Session session = openSession();
		
		try {
			session.beginTransaction();

			Criteria cr = session.createCriteria(Account.class);
			cr.add(Restrictions.eq("id", fromAccountID));
			List<Account> resultsFrom = cr.list();
			Account fromAccount = resultsFrom.get(0);
			fromAccount.debit(amount);
			session.update(fromAccount);
			
			Criteria cr2 = session.createCriteria(Account.class);
			cr2.add(Restrictions.eq("id", toAccountID));
			List<Account> resultsTo = cr.list();
			Account toAccount = resultsTo.get(0);
			toAccount.credit(amount);
			session.update(toAccount);

			// commit
			session.getTransaction().commit();
			
			// save transaction in database
			saveObjectToDb(trans);
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item list");
		} finally {
			// close session
			session.close();
		}
		
		return trans;
	}

	@Override
	public Account createAccount(String name, String type, double accountBalance, int ownerID)
			throws IllegalStateException {
		// create account instance
		Account item = new Account();
		item.setName(name);
		item.setType(type);
		item.setAccountBalance(accountBalance);
		item.setOwnerID(ownerID);
		item.setAltertionDate(new Date());
		item.setCreationDate(new Date());

		// save account in database
		saveObjectToDb(item);
		return item;
	}

	@Override
	public User createUser(String name, String email, String password) throws IllegalStateException {

		// create user instance
		User user = new User();
		user.setEmail(email);
		user.setJoinedDate(new Date());
		user.setName(name);
		try {
			user.setPassword(PasswordHash.getSaltedHash(password));
		} catch (Exception e) {
			System.out.println("Creation of Hash failed");
			throw new IllegalStateException("Creation of Hash failed", e);
		}

		// save user in database
		saveObjectToDb(user);
		return user;
	}

	@Override
	public Collection<Account> getAllAccountFromUser(int userID) throws IllegalStateException {
		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(Account.class);
			cr.add(Restrictions.eq("ownerID", userID));
			List<Account> results = cr.list();

			// commit
			session.getTransaction().commit();

			return results;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item list");
		} finally {
			// close session
			session.close();
		}
	}

	@Override
	public Collection<Transaction> getAllTransactionFromUser(int userID) throws IllegalStateException {
		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(Transaction.class);
			cr.add(Restrictions.eq("userID", userID));
			List<Transaction> results = cr.list();

			// commit
			session.getTransaction().commit();

			return results;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item list");
		} finally {
			// close session
			session.close();
		}
	}

	@Override
	public Collection<Transaction> getAllTransactionFromAccount(int accountID) throws IllegalStateException {
		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(Transaction.class);
			cr.add(Restrictions.or(Restrictions.eq("fromAccountID", accountID),
					Restrictions.eq("toAccountID", accountID)));
			List<Transaction> results = cr.list();

			// commit
			session.getTransaction().commit();

			return results;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item list");
		} finally {
			// close session
			session.close();
		}
	}

	@Override
	public User getUserLogin(String name, String password) throws IllegalStateException {
		Session session = openSession();

		// System.out.println("getUserLogin started");

		try {

			// begin transaction
			session.beginTransaction();

			// System.out.println("Transaction started");

			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.eq("name", name));
			List<User> results = cr.list();

			// commit
			session.getTransaction().commit();

			// System.out.println("Transaction committed");

			// only one element in the list because the id is unique
			for (User user : results) {
				try {
					// System.out.println("PW check started");
					if (PasswordHash.check(password, user.getPassword()))
						// System.out.println("Return user");
						return user;
				} catch (Exception e) {
					// System.out.println("PW check failed");
					throw new IllegalStateException("Fail by checking the user password");
				}
			}
			// System.out.println("no users found");
		} catch (HibernateException e) {
			// Exception -> rollback
			// System.out.println("Error!!");
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the user");
		} finally {
			// close session
			session.close();
		}
		// no appropriate user found in database
		return null;
	}

	@Override
	public Collection<User> getAllUsers() throws IllegalStateException {
		Session session = openSession();
		
		try {
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			List<User> results = cr.list();
			session.getTransaction().commit();
			return results;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting all users");
		} finally {
			session.close();
		}
	}

}