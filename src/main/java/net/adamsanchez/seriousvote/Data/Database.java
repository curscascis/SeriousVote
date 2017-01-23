package net.adamsanchez.seriousvote.Data;

import net.adamsanchez.seriousvote.U;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.util.concurrent.Executor;

/**
 * Created by adam_ on 01/22/17.
 */
public class Database {
    private String host = "localhost";
    private String port = "3306";
    private String username = "root";
    private String password = "password";
    private String dbType = "mySQL";
    private Connection db;
    private String table_prefix = "SV";
    private String playerTable = "players";




    public Database(){
        reconnect();
        terminateConnection();
        playerTable = table_prefix + "players";
    }





    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void reconnect(){
        db = getConnection();
    }

    public void terminateConnection(){
        try {
            db.close();
        } catch (SQLException e) {
            U.error("DB could not be closed...Maybe it's still in use?");
        }
    }

    public Connection getConnection(){
        Connection connection = null;
        U.info("Attempting to connect to the database...");
        try {
            connection = DriverManager.getConnection("jdbc:" + dbType + "://" + host + ":" + port + "/", username, password);
        } catch (SQLException e) {
            U.error("Failed to establish connection to the database");
        }
        return connection;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public Statement statement(){
        Statement statement = null;
        try{
            statement= db.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }
    public PreparedStatement preparedStatement(String string){
        PreparedStatement statement = null;
        try{
            statement= db.prepareStatement(string);

        } catch (SQLException e) {
            U.error("Error in DB Connection");
        }
        return statement;
    }

    private ResultSet genericQuery(String query){
        ResultSet results = null;
        try {
            results = statement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ResultSet genericSelectQuery(String table, String field, String value){
        String initial = "SELECT FROM %s WHERE %s='%s'";
        ResultSet results = genericQuery(String.format(initial,table,field,value));
        return  results;
    }



    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public PlayerRecord getPlayer(UUID uuid){
        ResultSet results = genericSelectQuery(playerTable, "player", uuid.toString());
        try {
            while(results.next()) {
                int sequentialVotes = results.getInt("voteSpree");
                Date lastVote = results.getDate("lastVote");
                int totalVote = results.getInt("totalVotes");
                results.close();
                return new PlayerRecord(uuid, totalVote,sequentialVotes,lastVote);
            }
        } catch (SQLException e) {
            U.error("Trouble getting information from the database");
        } finally {
            return new PlayerRecord(uuid, 0,0, new Date(new java.util.Date().getTime()));

        }

    }

    public void updatePlayer(PlayerRecord player){
        playerUpdateQuery(this.playerTable, player.uuid.toString(), player.totalVotes, player.voteSpree, player.lastVote);

    }

    public void playerUpdateQuery(String table, String uuid, int totalVotes, int voteSpree, Date lastVote){
        String initial = "REPLACE INTO %s(player, totalVotes, voteSpree, lastVote) VALUES(?,?,?,?)";
        PreparedStatement statement = preparedStatement(String.format(initial,table));
        try {
            statement.setString(1, uuid);
            statement.setInt(2, totalVotes);
            statement.setInt(3, voteSpree);
            statement.setDate(4, lastVote);
        } catch (SQLException e) {
            U.error("Error in trying to update player vote record!");
        }
    }

    public void createPlayerTable(){
        String table = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "player			STRING PRIMARY KEY" +
                "lastVote		DATE," +
                "totalVotes		INT," +
                "voteSpree		INT," +
                ")", playerTable);

        try {
            statement().executeUpdate(table);
        } catch (SQLException e) {
            U.error("Error Creating SQL TABLE-- CHECK YOUR DATA CONFIG");
        }

    }
}