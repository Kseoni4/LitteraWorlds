package org.litteraworlds.database;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.jdbc.*;
import com.mysql.cj.xdevapi.PreparableStatement;
import org.litteraworlds.dto.PlayerDTO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class DBService {

    private final String SQL_DRIVER = "jdbc:mysql://";

    private final String SELECT_FROM_USERS =
            "SELECT * " +
            "FROM users "+
            "WHERE name = ?";

    private final String INSERT_INTO_USERS =
            "INSERT INTO users (tokenID, name)" +
                    "VALUES (?,?)";

    private final String UPDATE_USER =
            "UPDATE users " +
                    "SET ";
    private final String UPDATE_USER_WHERE =
            "WHERE name = ?";

    private static DBService instance;

    private Connection mysqlConnection;

    private DBService(){
        try {
            Properties dbConnectionProp = new Properties();
            dbConnectionProp.load(DBService.class.getResource("/DataBaseConnection.properties").openStream());
            Logger.getGlobal().info(dbConnectionProp.toString());

            mysqlConnection = DriverManager.getConnection(
                    SQL_DRIVER
                            + dbConnectionProp.getProperty("host")+"/"
                            + dbConnectionProp.getProperty("database_name"),
                    dbConnectionProp.getProperty("username"),
                    dbConnectionProp.getProperty("password"));
        } catch (IOException | SQLException e){
            Logger.getGlobal().info("Connection error, caused by "+e.getCause().toString());
        }
    }

    static {
        instance = new DBService();
    }

    /**
     * Осуществляет поиск в БД по имени игрока.
     * Возвращает массив строк, сформированный по полученным полям, если пользователь найден
     * Если нет, то вернёт пустой массив.
     * @param playerName имя игрока
     * @return массив полей пользователя, либо пустой массив
     */
    public String[] selectPlayerRowByName(String playerName) {
        String[] outArray = new String[0];
        try {
            ResultSet set = getResultSetFromPrepStatement(makePreparedStatementByData(SELECT_FROM_USERS, playerName));

            if(set != null) {
                if(set.next()) {

                    outArray = new String[]{
                            set.getString(1),//TokenID
                            set.getString(2),//name
                            set.getString(3),//password
                            set.getString(4)};//place_hash

                    System.out.println(Arrays.toString(outArray));

                    set.close();
                } else {
                    return outArray;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outArray;
    }

    /**
     * Производит вставку нового игрока в таблицу users
     * Принимает на вход контейнер с данными.
     * @param playerDTO контейнер с данными игрока
     */
    public void insertNewPlayer(PlayerDTO playerDTO){
        makePreparedStatementByData(INSERT_INTO_USERS, playerDTO.getTokenID(), playerDTO.getPlayerName());
    }

    /**
     * Обновляет данные об игроке, используя в качестве ключа поиска - имя игрока
     * Принимает два массива - название полей и значений
     * @param playerName имя игрока
     * @param fields массив полей
     * @param data массив значений
     */
    public void updatePlayerRow(String playerName, String[] fields, String[] data){
        String fieldAndValue = "";

        for(int i = 0; i < fields.length;i++){
            fieldAndValue = fieldAndValue.concat(fields[i]).concat(" = ").concat("?");

            if(i+1 < fields.length){
                fieldAndValue = fieldAndValue.concat(",");
            }
        }

        String updateQuery = UPDATE_USER.concat(fieldAndValue).concat(UPDATE_USER_WHERE);

        String[] finalData = new String[data.length+1];
        System.arraycopy(data, 0, finalData, 0, data.length);
        finalData[finalData.length-1] = playerName;

        makePreparedStatementByData(updateQuery, finalData);
    }

    /**
     * Осуществляет магию, не трогать.
     * @param SQLQuery
     * @param values
     * @return
     */
    private PreparedStatement makePreparedStatementByData(String SQLQuery, String... values){
        PreparedStatement prepState = null;

        System.out.println(SQLQuery);
        try {
            prepState = mysqlConnection.prepareStatement(SQLQuery);
            int indexColumn = 1;

            for(String value : values){
                System.out.println(value);
                prepState.setString(indexColumn++, value);
            }

            if(SQLQuery.startsWith("INSERT") || SQLQuery.startsWith("UPDATE")){
                System.out.println(SQLQuery);
                prepState.execute();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return prepState;
    }

    /**
     * Возвращает результаты магии, не трогать
     * @param preparedStatement
     * @return
     */
    private ResultSet getResultSetFromPrepStatement(PreparedStatement preparedStatement){
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static DBService getInstance() {
        return instance;
    }
}
