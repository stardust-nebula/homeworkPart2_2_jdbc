import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbUserStorage implements IDbInfo {

    private Connection connection;


    public void getConnectionDM() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void save(User user) {
        try {

            ConsoleWriter.write("Запрашиваю установку коннекшена");
            getConnectionDM();

            // устанавиливаем setAutoCommit в false, чтобы метод save() выполнился одно транзакцией
            connection.setAutoCommit(false);

            ConsoleWriter.write("Добавляю адрес");
            // добавляем запись об адресе
            PreparedStatement addAddressPrepStat = connection.prepareStatement("INSERT INTO addresses VALUES (default , ?) RETURNING id");
            addAddressPrepStat.setString(1, user.getAddress().getStreet());

            // получение id адреса
            ResultSet resultSet = addAddressPrepStat.executeQuery();
            resultSet.next();
            int newAddressId = resultSet.getInt(1);

            ConsoleWriter.write("Добавляю телефон");
            // добавляем телефон
            PreparedStatement addTelephonePrepStat = connection.prepareStatement("INSERT INTO telephone VALUES (default, ?) RETURNING id");
            addTelephonePrepStat.setString(1, user.getTelephone().getPhone());

            // получение id телефона
            resultSet = addTelephonePrepStat.executeQuery();
            resultSet.next();
            int newTelephone = resultSet.getInt(1);

            ConsoleWriter.write("Добавляю юзера");
            // добавляем запись о user
            PreparedStatement addUserPrepStat = connection.prepareStatement("INSERT INTO users VALUES (default , ?, ?, ?, ?, ?)");
            addUserPrepStat.setString(1, user.getName());
            addUserPrepStat.setString(2, user.getUsername());
            addUserPrepStat.setString(3, user.getPassword());
            addUserPrepStat.setInt(4, newAddressId);
            addUserPrepStat.setInt(5, newTelephone);

            ConsoleWriter.write("Запускаю execute");
            // выполнение addUserPrepStat
            addUserPrepStat.execute();

            // после выполнения всех изменений выполняет изменения

            ConsoleWriter.write("Выполнение коммита");
            connection.commit();

        } catch (SQLException throwables) {
            try {
                ConsoleWriter.write("Выаполняется rollback");
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {

            try {
                // возвращаем setAutoCommit в true
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Optional<List> getByUsername(String username) {
        getConnectionDM();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users u join addresses a on a.id = u.address_id join telephone t on u.telephone_id = t.id where u.username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> userList = new ArrayList();

            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String currentUsername = resultSet.getString(3);
                String password = resultSet.getString(4);
                int addressId = resultSet.getInt(5);
                String street = resultSet.getString(6);
                int telephoneId = resultSet.getInt(7);
                String telephone = resultSet.getString(8);
                userList.add(new User(userId, name, username, password, new Address(addressId, street), new Telephone(telephoneId, telephone)));
            }

            return Optional.of(userList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List> getAllByName(String name) {
        getConnectionDM();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT u.id, u.name, u.username, u.password, a.id, a.street, t.id, t.telephone FROM users u join addresses a on a.id = u.address_id join telephone t on u.telephone_id = t.id where u.name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> userList = new ArrayList();

            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String currentName = resultSet.getString(2);
                String username = resultSet.getString(3);
                String password = resultSet.getString(4);
                int addressId = resultSet.getInt(5);
                String street = resultSet.getString(6);
                int telephoneId = resultSet.getInt(7);
                String telephone = resultSet.getString(8);
                userList.add(new User(userId, currentName, username, password, new Address(addressId, street), new Telephone(telephoneId, telephone)));
            }
            return Optional.of(userList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<List> getAll() {
        getConnectionDM();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users join addresses a on a.id = users.address_id join telephone t on users.telephone_id = t.id");

            List<User> userList = new ArrayList();

            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String currentName = resultSet.getString(2);
                String username = resultSet.getString(3);
                String password = resultSet.getString(4);
                int addressId = resultSet.getInt(5);
                String street = resultSet.getString(6);
                int telephoneId = resultSet.getInt(7);
                String telephone = resultSet.getString(8);
                userList.add(new User(userId, currentName, username, password, new Address(addressId, street), new Telephone(telephoneId, telephone)));
            }
            return Optional.of(userList);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List> getAllByStreet(String street) {
        getConnectionDM();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT u.id, u.name, u.username, u.password, a.id, a.street, t.id, t.telephone FROM users u join addresses a on a.id = u.address_id join telephone t on u.telephone_id = t.id where a.street = ?");
            preparedStatement.setString(1, street);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> userList = new ArrayList();

            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String username = resultSet.getString(3);
                String password = resultSet.getString(4);
                int addressId = resultSet.getInt(5);
                String currentStreet = resultSet.getString(6);
                int telephoneId = resultSet.getInt(7);
                String telephone = resultSet.getString(8);
                userList.add(new User(userId, name, username, password, new Address(addressId, currentStreet), new Telephone(telephoneId, telephone)));
            }
            return Optional.of(userList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public void updateNameByID(int userID, String newName) {
        getConnectionDM();

        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, userID);

            preparedStatement.execute();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void updatePasswordByID(int userID, String newPassword) {
        getConnectionDM();

        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);

            preparedStatement.execute();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void deleteById(int userId) {
        getConnectionDM();

        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            preparedStatement.setInt(1, userId);

            preparedStatement.execute();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean userExist(User user) {
        getConnectionDM();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  u.name, u.username, u.password FROM users u WHERE u.name = ? AND u.username = ? and u.password = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);

                if (name.equals(user.getName()) && username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean userExistById(int userId) {
        getConnectionDM();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  u.id  FROM users u WHERE u.id = ?");
            preparedStatement.setInt(1, userId);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);

                if (id == userId) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }


}
