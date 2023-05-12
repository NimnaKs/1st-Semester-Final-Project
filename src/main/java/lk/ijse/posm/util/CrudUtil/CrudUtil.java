package lk.ijse.posm.util.CrudUtil;

import lk.ijse.posm.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {

    public static <T> T execute(String sql,Object... args) throws SQLException {
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        for(int i=0;i<args.length;i++)
            preparedStatement.setObject(i+1,args[i]);
        return (sql.startsWith("SELECT"))?
                (T) preparedStatement.executeQuery():
                (T)(Boolean)(preparedStatement.executeUpdate()>0);
    }

}
