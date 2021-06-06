import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySQLAccess {
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/datab?"
                            + "user=root&password=radhit");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
			String subid = "2";
			String seqno = "5";
            resultSet = statement
                    .executeQuery("select value from food_pss where subid='" + subid +"' and seqno='" + seqno +"'");
            writeResultSet(resultSet);

			preparedStatement = connect
                    .prepareStatement("insert into  datab.food_pss values (default, ?, ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "2");
            preparedStatement.setString(2, "5");
            preparedStatement.setString(3, "10000");
            preparedStatement.executeUpdate();

			//preparedStatement = connect
                    //.prepareStatement("truncate table food_pss");
			//.executeUpdate();

			/*statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from food_pss");
            writeResultSet(resultSet);*/
			
			
			
		}catch (Exception e) {
            throw e;
        }
	}

	private static void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            //String subid = resultSet.getString("SUBID");
            //String seqno = resultSet.getString("SEQNO");
            String value = resultSet.getString("VALUE");
            
            //System.out.println("Sub ID: " + subid);
            //System.out.println("Seq No: " + seqno);
            System.out.println("Value: " + value);
            
        
        }
    }

	public static void main(String[] args) throws Exception {
        readDataBase();
    }
}
