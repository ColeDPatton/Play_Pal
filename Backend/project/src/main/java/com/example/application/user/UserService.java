package com.example.application.user;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.file.DBfileRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	SwipeRepository swipeRepository;
	@Autowired
	DBfileRepository fileRepository;

	/*
	 * Creates user using the following fields: name, DOB, sex, gamertag, console,
	 * email, password usertype, and then return user.
	 */
	public User createUser(String name, String dateOfBirth, String sex, String gamertag, String console, 
			String email, String password, int userType,
			int fortnite, int minecraft, int gta5, int leagueoflegends, 
			int rainbowsixsiege, int overwatch, int pubg, int rocketleague) {
		return new User(name, dateOfBirth, sex, gamertag, console, email, password, userType,
				fortnite, minecraft, gta5, leagueoflegends, rainbowsixsiege, overwatch, pubg, rocketleague);
	}

	/*
	 * return true if email is not being used
	 */
	public Boolean addUser(User user) {
		if (userRepository.getUserByEmail(user.getEmail()).size() == 0) {
			userRepository.save(user);
			return true;
		}
		return false;
	}

	/*
	 * creates user with name
	 */
	public User createUser(String name) {
		return new User(name);
	}

	/*
	 * List all users
	 */
	public List<User> getAll() {
		return (List<User>) userRepository.findAll();
	}

	/*
	 * find User by ID
	 */
	public Optional<User> get(long id) {
		return userRepository.findById(id);
	}

	/*
	 * login with email and password
	 */
	public User login(String email, String password) {
		return userRepository.loginUser(email, password).get(0);
	}

	/*
	 * verifies login
	 */
	public Boolean checkLogin(String email, String password) {
		System.out.println("ASDFASDFA");
		return (userRepository.loginUser(email, password).size() > 0) ? true : false;
	}

	/*
	 *  Return true if the email exists in the database
	 */
	public Boolean checkIfEmailExists(String email) {
		return (userRepository.getUserByEmail(email).size() > 0) ? true : false;
	}

	/*
	 * finds next user to swipe on 
	 */
	public User searchByRandom(User user) {
		List<User> temp = userRepository.nextUser(user.getId());
		if (temp.size() != 0)
			return temp.get(0);
		return userRepository.findById((long) -1).get();
	}
	
	public User searchByConsole(User user) {
		List<User> temp = userRepository.searchByConsole(user.getConsole(), user.getId());
		if (temp.size() != 0)
			return temp.get(0);
		return userRepository.findById((long) -1).get();
	}

	public User searchByGame(User user, String game) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String methodName = "searchByGame"+game;
		Method searchMethod = UserRepository.class.getMethod(methodName, long.class);
		 List<User> temp  = (List<User>) searchMethod.invoke(userRepository, user.getId());
		if (temp.size() != 0)
			return temp.get(0);
		return userRepository.findById((long) -1).get();
	}
	
	/*
	 * adding user the the User swiped on if current user decides to swipe right
	 */
	public void addUserToSwipes(Swipe swipe) {
		swipeRepository.save(swipe);
	}

	/*
	 * List all users matched with the current user
	 */
	public List<User> getUsersMatchedWithCurrentUser(ArrayList<User> currentUser) {
		return userRepository.getUsersMatchedWithUser(currentUser.get(0).getId());
	}

	/*
	 * updates current user
	 */
	public User updateUser(User currentUser) {
		return userRepository.save(currentUser);
	}

	public Boolean deleteUser(User user) {
		swipeRepository.clearSwipesFromUser(user);
		fileRepository.clearImagesFromUser(user);
		userRepository.delete(user);
		return true;
	}
}
