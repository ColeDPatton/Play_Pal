package com.example.application.user;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is the User Controller and handles adding USer to the database.
 */
@RestController
public class UserController {

	@Autowired
	UserService userService;

	/*
	 * ADD USER TO DATABASE RETURNS BOOLEAN AND ID
	 */

	@PostMapping("/users/addUser")
	public booleanResponse post(@RequestBody User user) {
		return new booleanResponse(userService.addUser(user), user.getId());
	}
	
	@PostMapping("/users/deleteUser")
	public booleanResponse deleteUser(@RequestBody User user) {
		return new booleanResponse(userService.deleteUser(user));
	}
	public static class booleanResponse {
		private long id;
		private Boolean b;
		private String email;
		private String password;

		public booleanResponse() {
		}

		public booleanResponse(Boolean b) {
			this.setB(b);
		}

		public booleanResponse(Boolean b, long id) {
			this.setB(b);
			this.setId(id);
		}

		public booleanResponse(Boolean b, String email, String password) {
			this.setB(b);
			this.setEmail(email);
			this.setPassword(password);
		}

		/*
		 * return B
		 */
		public Boolean getB() {
			return b;
		}

		/*
		 * set B
		 */
		public void setB(Boolean b) {
			this.b = b;
		}

		/*
		 * return email
		 */
		public String getEmail() {
			return email;
		}

		/*
		 * set Email
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/*
		 * return password
		 */
		public String getPassword() {
			return password;
		}

		/*
		 * set password
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/*
		 * return id
		 */
		public long getId() {
			return id;
		}

		/*
		 * set ID
		 */
		public void setId(long id) {
			this.id = id;
		}
	}

	/*
	 * create a Converter class that is used to add field to the database
	 */
	public static class Converter {
		private int swipingUserId;
		private int swipedOnId;
		private int liked;

		public Converter() {
		}

		/*
		 * Initializes a, b, and c into appropriate categories 
		 * 
		 */
		public Converter(int a, int b, int c) {
			swipingUserId = a;
			swipedOnId = b;
			liked = c;
		}

		/*
		 * return swiping User ID
		 */
		public int getSwipingUserId() {
			return swipingUserId;
		}

		/*
		 * set swiping user ID
		 */
		public void setSwipingUserId(int swipingUserId) {
			this.swipingUserId = swipingUserId;
		}

		/*
		 * return swipedOnId
		 */
		public int getSwipedOnId() {
			return swipedOnId;
		}

		/*
		 * set swiped onID
		 */
		public void setSwipedOnId(int swipedOnId) {
			this.swipedOnId = swipedOnId;
		}

		/*
		 * return liked
		 */
		public int getLiked() {
			return liked;
		}

		/*
		 * set liked
		 */
		public void setLiked(int liked) {
			this.liked = liked;
		}
	}

	/*
	 * ADDS A SWIPE TO DATABASE. SWIPES CONSIST OF THE USER, THE USER THEY SWIPED
	 * ON, AND WHETHER OR NOT THE SWIPED YES RETURNS BOOLEAN
	 */

	@PostMapping("/users/swipe")
	public booleanResponse swipe(@RequestBody Converter converter) {
		User swiper = userService.get(converter.swipingUserId).get();
		User swipedOn = userService.get(converter.swipedOnId).get();
		if (swiper == null || swipedOn == null)
			return new booleanResponse(false);
		userService.addUserToSwipes(new Swipe(swiper, swipedOn, converter.liked));
		return new booleanResponse(true);
	}

	/*
	 * RETURNS A USER
	 */
	@PostMapping("/users/login")
	public User loginUser(@RequestBody booleanResponse emailAndPassword) {
		return userService.login(emailAndPassword.getEmail(), emailAndPassword.getPassword());
	}

	/*
	 * CHECKS IF EMAIL AND PASSWORD ARE VALID FOR SOME USER RETURNS BOOLEAN, EMAIL,
	 * AND PASSWORD
	 */
	@PostMapping("/users/loginCheck")
	public booleanResponse loginCheck(@RequestBody booleanResponse emailAndPassword) {
		return new booleanResponse(userService.checkLogin(emailAndPassword.getEmail(), emailAndPassword.getPassword()),
				emailAndPassword.getEmail(), emailAndPassword.getPassword());
	}

	/*
	 * GET NEXT USER TO VIEW AND SWIPE ON
	 */
	@PostMapping("/users/searchByRandom")
	public User searchByRandom(@RequestBody User user) {
		return userService.searchByRandom(user);
	}

	/*
	 * GET NEXT USER TO VIEW AND SWIPE ON
	 */
	@PostMapping("/users/searchByConsole")
	public User searchByConsole(@RequestBody User user) {
		User u = userService.get(user.getId()).get();
		return userService.searchByConsole(u);
	}
	public static class IdAndGame{
		private long id;
		private String game;
		public IdAndGame() {}
		public IdAndGame(long id, String game) {
			setId(id);
			setGame(game);
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getGame() {
			return game;
		}
		public void setGame(String game) {
			this.game = game;
		}
	}
	@PostMapping("/users/searchByGame")
	public User searchByGame(@RequestBody IdAndGame idandgame) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		User u = userService.get(idandgame.getId()).get();
		return userService.searchByGame(u, idandgame.getGame());
	}
	
	/*
	 * GET ALL USERS
	 */
	@GetMapping("/users")
	public List<User> get() {
		return userService.getAll();
	}

	/*
	 * GET USER BY ID
	 */
	@GetMapping("/users/{id}")
	public User get(@PathVariable long id) {
		return userService.get(id).get();
	}

	/*
	 * GET MATCHED USERS
	 */
	@PostMapping("/users/matchedUsers")
	public List<User> getUsersMatchedWithCurrentUser(@RequestBody ArrayList<User> currentUser) {
		return userService.getUsersMatchedWithCurrentUser(currentUser);
	}

	@PostMapping("/users/editUser")
	public booleanResponse editUser(@RequestBody User updatedUser) {
		booleanResponse re = new booleanResponse();
		User currentUser = get(updatedUser.getId());

		if (currentUser.getEmail() == null)
			return new booleanResponse(false);
		else if (!(updatedUser.getEmail().equals(currentUser.getEmail())) &&
				userService.checkIfEmailExists(updatedUser.getEmail())) {
			re.setB(false);
			re.setEmail(updatedUser.getEmail());
			return re;
		}
		currentUser = updatedUser;

		userService.updateUser(currentUser);
		re.setB(true);
		re.setEmail(updatedUser.getEmail());
		return re;
	}

	/*
	 * RETURNS A TEST USER (NOT USING THE DATABASE)
	 */
	@RequestMapping(value = "/users/test", method = RequestMethod.GET)
	public User testUser() {
		User dummy = userService.createUser("bill", "November 11, 1997", "Male", "GamerDummy", "XboxOne",
				"email@email.com", "password", 1,0,0,0,0,0,0, 0, 0);
		return dummy;
	}
}
