package com.example.application.user;

import javax.persistence.*;

/*
 * This class creates a table within the database that 
 * is going to store the user and all his/her's pertaining information
 */
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "swipedOn"), @JoinColumn(name = "swiper") })
	private Swipe swipe;

	private String name, dateofbirth, sex, gamertag, console, email, password;
	private int fortnite, minecraft, gta5, leagueoflegends, rainbowsixsiege, overwatch, pubg, rocketleague;
	private int usertype; // 1(user), 2(premium), or 3(admin)

	public User() {
		this.usertype = 1;
	}

	/*
	 * creates the user including name, dob, sex, gamertag, console, email,
	 * password, and usertype
	 */
	public User(String name, String dateOfBirth, String sex, String gamertag, String console, 
			String email, String password, int userType,
			int fortnite, int minecraft, int gta5, int leagueoflegends, 
			int rainbowsixsiege, int overwatch, int pubg, int rocketleague) {
		this.name = name;
		this.dateofbirth = dateOfBirth;
		this.sex = sex;
		this.gamertag = gamertag;
		this.console = console;
		this.email = email;
		this.password = password;
		this.usertype = userType;
		this.setFortnite(fortnite);
		this.setMinecraft(minecraft);
		this.setGta5(gta5);
		this.setLeagueoflegends(leagueoflegends);
		this.setRainbowsixsiege(rainbowsixsiege);
		this.setOverwatch(overwatch);
		this.setPubg(pubg);
		this.setRocketleague(rocketleague);
	}

	public User(String name) {
		this.name = name;
	}

	/*
	 * returns user ID
	 */
	public long getId() {
		return id;
	}

	/*
	 * return UserName
	 */
	public String getName() {
		return name;
	}

	/*
	 * sets user name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * return DOB
	 */
	public String getDateofbirth() {
		return dateofbirth;
	}

	/*
	 * Set DOB
	 */
	public void setDateofbirth(String dateOfBirth) {
		this.dateofbirth = dateOfBirth;
	}

	/*
	 * return sex
	 */
	public String getSex() {
		return sex;
	}

	/*
	 * set sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/*
	 * return gamertag
	 */
	public String getGamertag() {
		return gamertag;
	}

	/*
	 * setGamerTag
	 */
	public void setGamertag(String gamertag) {
		this.gamertag = gamertag;
	}

	/*
	 * return console
	 */
	public String getConsole() {
		return console;
	}

	/*
	 * set console
	 */
	public void setConsole(String console) {
		this.console = console;
	}

	/*
	 * return email
	 */
	public String getEmail() {
		return email;
	}

	/*
	 * set email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * return Usertype
	 */
	public int getUsertype() {
		return usertype;
	}

	/*
	 * set Usertype
	 */
	public void setUsertype(int userType) {
		this.usertype = userType;
	}

	/*
	 * return password
	 * 
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

	public int getFortnite() {
		return fortnite;
	}

	public void setFortnite(int fortnite) {
		this.fortnite = fortnite;
	}

	public int getMinecraft() {
		return minecraft;
	}

	public void setMinecraft(int minecraft) {
		this.minecraft = minecraft;
	}

	public int getGta5() {
		return gta5;
	}

	public void setGta5(int gta5) {
		this.gta5 = gta5;
	}

	public int getLeagueoflegends() {
		return leagueoflegends;
	}

	public void setLeagueoflegends(int leagueoflegends) {
		this.leagueoflegends = leagueoflegends;
	}

	public int getRainbowsixsiege() {
		return rainbowsixsiege;
	}

	public void setRainbowsixsiege(int rainbowsixsiege) {
		this.rainbowsixsiege = rainbowsixsiege;
	}

	public int getOverwatch() {
		return overwatch;
	}

	public void setOverwatch(int overwatch) {
		this.overwatch = overwatch;
	}

	public int getPubg() {
		return pubg;
	}

	public void setPubg(int pubg) {
		this.pubg = pubg;
	}

	public int getRocketleague() {
		return rocketleague;
	}

	public void setRocketleague(int rocketleague) {
		this.rocketleague = rocketleague;
	}

}