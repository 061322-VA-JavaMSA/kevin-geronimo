package com.revature.dao;

import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserPostgres implements Dao<User> {

    private static final Logger logger = LogManager.getLogger(UserPostgres.class);
    private String schema = "public";

    public UserPostgres() {
        super();
    }

    public UserPostgres(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public User get(int id) {
        logger.trace("Called UserPostgres get method");
        User user = null;
        String sql = "SELECT * FROM \"user\" WHERE user_id = ?;";

        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User.Role role = User.Role.valueOf(rs.getString("assigned_role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                user = new User(id, username, password, role);
            }
        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return user;
    }

    public User get(String username) {
        logger.trace("Called UserPostgres get method");
        User user = null;
        String sql = "SELECT * FROM \"user\" WHERE username = ?;";

        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String password = rs.getString("password");
                User.Role role = User.Role.valueOf(rs.getString("assigned_role"));
                user = new User(id, username, password, role);
            }
        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        logger.trace("Called UserPostgres getAll method");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM \"user\"";

        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User.Role role = User.Role.valueOf(rs.getString("assigned_role"));

                User user = new User(id, username, password, role);
                users.add(user);
            }
        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return users;
    }

    @Override
    public User insert(User user) {
        logger.trace("Called UserPostgres insert method");
        String sql = "INSERT INTO "
                + "\"user\"(username, password, assigned_role) "
                + "VALUES(?, ?, ?::role) returning user_id";

        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());

            ResultSet rs = ps.executeQuery(); // return the id generated by the database
            if (rs.next()) {
                user.setId(rs.getInt("user_id"));
            }

        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return user;
    }

    @Override
    public boolean update(User user) {
        logger.trace("Called UserPostgres update method");
        String sql = "UPDATE \"user\" "
                + "SET "
                + "username = ?, "
                + "password = ?, "
                + "assigned_role = ?::role "
                + "WHERE "
                + "user_id = ?";

        int rowsChanged = -1;

        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            ps.setInt(4, user.getId());

            rowsChanged = ps.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return rowsChanged >= 1;

    }

    @Override
    public boolean delete(int id) {
        logger.trace("Called UserPostgres delete method");
        String sql = "DELETE FROM \"user\" WHERE user_id = ?";

        int rowsChanged = -1;
        try (Connection c = ConnectionUtil.getConnectionFromFile()) {
            c.setSchema(schema);
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, id);

            rowsChanged = ps.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error(ex.getMessage());
        }

        return rowsChanged >= 1;
    }
}
