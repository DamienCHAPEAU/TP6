package testingwithhsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;


public class HsqlDBTest {
	private static DataSource myDataSource;
	private static Connection myConnection ;
	
	private DAO myObject;
	
	@Before
	public  void setUp() throws IOException, SqlToolError, SQLException {
		// On crée la connection vers la base de test "in memory"
		myDataSource = getDataSource();
		myConnection = myDataSource.getConnection();
		// On crée le schema de la base de test
		executeSQLScript(myConnection, "schema.sql");
		// On y met des données
		executeSQLScript(myConnection, "bigtestdata.sql");		

            	myObject = new DAO(myDataSource);
	}
	
	private void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
		// On initialise la base avec le contenu d'un fichier de test
		String sqlFilePath = HsqlDBTest.class.getResource(filename).getFile();
		SqlFile sqlFile = new SqlFile(new File(sqlFilePath));

		sqlFile.setConnection(connexion);
		sqlFile.execute();
		sqlFile.closeReader();		
	}
		
	@After
	public void tearDown() throws IOException, SqlToolError, SQLException {
		myConnection.close(); // La base de données de test est détruite ici
             	myObject = null; // Pas vraiment utile

	}

	@Test
	public void findExistingCustomer() throws SQLException {
		String name = myObject.nameOfCustomer(0);
		assertNotNull("Customer exists, name should not be null", name);
		assertEquals("Bad name found !", "Steel", name);
	}

	@Test
	public void nonExistingCustomerReturnsNull() throws SQLException {
		String name = myObject.nameOfCustomer(-1);
		assertNull("name should be null, customer does not exist !", name);
	}

	public static DataSource getDataSource() {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}
        
        
        
        
        //Test sur findProduct
        
        @Test
	public void nonExistingProductReturnsNull() throws SQLException {
		assertNull(myObject.findProduct(151515));
	}
	
	@Test
	public void findExistingProduct() throws SQLException {		
		Product productTest = myObject.findProduct(0);
		assertEquals("Iron Iron", productTest.getName());
	}
        
        //Test sur insertProduct
        
        @Test
	public void insertProduct() throws SQLException {
		Product productTestInsert = new Product(1000,"produitTest", 100);
		myObject.insertProduct(productTestInsert);
		assertEquals(productTestInsert, myObject.findProduct(1000));
	}

	@Test(expected = SQLException.class)
	public void CantCreateTwoProductWithSameId() throws SQLException {		
		Product existant = new Product(1, "produit Test", 10);
		myObject.insertProduct(existant);
	}

	@Test(expected = SQLException.class)
	public void ifPriceNegative() throws SQLException {		
		Product productTest = new Product(100, "produit test", -10);
		myObject.insertProduct(productTest); 
	}
	
}
