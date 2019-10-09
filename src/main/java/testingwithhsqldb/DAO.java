package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfCustomer(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT LastName FROM Customer WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("LastName");
				}
			}
		}
		// dernière ligne : on renvoie le résultat
		return result;
	}
        /**
	 * Insére un nouveau produit à la BD
	 * @param product le produit à ajouter
	 * 
	 * @throws SQLException 
	 */
        public void insertProduct(Product product) throws SQLException {		
		String sql = "INSERT INTO PRODUCT VALUES(?, ?, ?)";
		try (   Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
                ) {
                        // On insère la valeur des paramètres
			stmt.setInt(1, product.getProductId());
			stmt.setString(2, product.getName());
			stmt.setInt(3, product.getPrix());
			stmt.executeUpdate();
                }
                
	}
        /**
	 * Trouver un Product à partir de sa clé
	 *
	 * @param productId la clé du Product à rechercher
	 * @return l'enregistrement correspondant dans la table Product, ou null si pas trouvé
	 * @throws SQLException
	 */
        public Product findProduct(int productId) throws SQLException {
		Product result = null;
                String sql = "SELECT * FROM PRODUCT WHERE ID = (?)";
                try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
			PreparedStatement stmt = connection.prepareStatement(sql)
		) {
                    // On insère la valeur des paramètres
                    stmt.setInt(1, productId);

                    try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                            String name = rs.getString("name");
                            int prix = rs.getInt("price");
                            result = new Product(productId, name, prix);
                        } 
                    }                
                } 
                //On retourne le résultat 
                return result;
	}
}
