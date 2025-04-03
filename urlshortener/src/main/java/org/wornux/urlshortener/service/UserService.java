package org.wornux.urlshortener.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bson.types.ObjectId;
import org.wornux.urlshortener.dao.UserDAO;
import org.wornux.urlshortener.model.User;

public class UserService {

  private final UserDAO userDAO;

  public UserService(@Nonnull UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public Optional<User> authenticate(@Nonnull String email, @Nonnull String password) {
    return userDAO.findByEmailAndPassword(email, password);
  }

  public List<User> getUsers() {
    return userDAO.findAll();
  }

  public Optional<User> getUserById(@Nonnull ObjectId id) {
    return userDAO.findById(id);
  }

  public void saveUser(@Nonnull User user) {
    userDAO.save(user);
  }

  public void updateUser(@Nonnull User user) {
    userDAO.update(user);
  }

  public void deleteUser(@Nonnull ObjectId id) {
    userDAO.deleteById(id);
  }
}
