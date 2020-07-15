package com.example.p19;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class is a container for all data relevant to any user
 */
public class User implements Serializable {

    private String name;
    private String dateofbirth;
    private String sex;
    private String gamertag;
    private String console;
    private String email;
    private String password;


    private String gamepref;
    private int usertype, fortnite, minecraft, gta5, leagueoflegends, rainbowsixsiege,
            overwatch, pubg, rocketleague, searchPref;
    private long id,cache;
    private boolean reported;
    /**
     * Empty constructor used to make a test user
     */
    public User(){
        this.name = "test";
        this.dateofbirth = "test";
        this.sex = "test";
        this.gamertag = "test";
        this.console = "test";
        this.email = "test";
        this.password = "test";
        this.usertype = 0;
        this.reported = false;
        this.id = 123;
        this.fortnite = 0;
        this.minecraft = 0;
        this.gta5 = 0;
        this.leagueoflegends = 0;
        this.rainbowsixsiege = 0;
        this.overwatch = 0;
        this.pubg = 0;
        this.rocketleague = 0;
        this.searchPref = 0;
        this.gamepref = null;
    }

    /**
     * Constructor for making a new user
     * @param name Name of user
     * @param dateOfBirth Date of birth of user
     * @param sex Sex of user
     * @param gamertag Gamertag of user
     * @param console Console of user
     * @param email Email of user
     * @param password Password of user
     * @param userType Type of user (Admin, Paid, Standard)
     * @param id ID of the user
     */
    public User(String name, String dateOfBirth, String sex, String gamertag, String console, String email, String password, int userType, long id,
                int fortnite, int minecraft, int gta5, int leagueoflegends, int rainbowsixsiege, int overwatch, int pubg, int rocketleague,
                int searchPref, String gamepref){
        this.name = name;
        this.dateofbirth = dateOfBirth;
        this.sex = sex;
        this.gamertag = gamertag;
        this.console = console;
        this.email = email;
        this.password = password;
        this.usertype = userType;
        this.id = id;
        this.fortnite = fortnite;
        this.minecraft = minecraft;
        this.gta5 = gta5;
        this.leagueoflegends = leagueoflegends;
        this.rainbowsixsiege = rainbowsixsiege;
        this.overwatch = overwatch;
        this.pubg = pubg;
        this.rocketleague = rocketleague;
        this.searchPref = searchPref;
        this.gamepref = gamepref;
    }

    /**
     * Constructor for user
     * @param jsonString JSONString used to create a new user from a JSONString quickly
     */
    public User(String jsonString){
        try{
            JSONObject data = new JSONObject(jsonString);
            this.name = data.getString("name");
            this.dateofbirth = data.getString("dateofbirth");
            this.sex = data.getString("sex");
            this.gamertag = data.getString("gamertag");
            this.console = data.getString("console");
            this.email = data.getString("email");
            this.password = data.getString("password");
            this.usertype = data.getInt("usertype");
            this.id = data.getLong("id");
            this.fortnite = data.getInt("fortnite");
            this.minecraft = data.getInt("minecraft");
            this.gta5 = data.getInt("gta5");
            this.leagueoflegends = data.getInt("leagueoflegends");
            this.rainbowsixsiege = data.getInt("rainbowsixsiege");
            this.overwatch = data.getInt("overwatch");
            this.pubg = data.getInt("pubg");
            this.rocketleague = data.getInt("rocketleague");
            this.searchPref = data.getInt("searchpref");
            this.gamepref = data.getString("gamepref");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Constructor for user
     * @param data JSONObject used to create a new user from a JSONObject quickly
     */
    public User(JSONObject data){
        try{

            this.name = data.getString("name");
            this.dateofbirth = data.getString("dateofbirth");
            this.sex = data.getString("sex");
            this.gamertag = data.getString("gamertag");
            this.console = data.getString("console");
            this.email = data.getString("email");
            this.password = data.getString("password");
            this.usertype = data.getInt("usertype");
            this.id = data.getLong("id");
            this.fortnite = data.getInt("fortnite");
            this.minecraft = data.getInt("minecraft");
            this.gta5 = data.getInt("gta5");
            this.leagueoflegends = data.getInt("leagueoflegends");
            this.rainbowsixsiege = data.getInt("rainbowsixsiege");
            this.overwatch = data.getInt("overwatch");
            this.pubg = data.getInt("pubg");
            this.rocketleague = data.getInt("rocketleague");
            this.searchPref = data.getInt("searchpref");
            this.gamepref = data.getString("gamepref");
        }
        catch (Exception e){
            this.gamepref = null;
        }
    }

    /**
     * Returns the name
     * @return Name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this user
     * @param name Name to change to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Date of Birth
     * @return Date of Birth of user
     */
    public String getDateofbirth() {
        return dateofbirth;
    }

    /**
     * Sets the Date of Birth of this user
     * @param dateofbirth Birthday to change to
     */
    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    /**
     * Returns the sex
     * @return sex of user
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the sex of this user
     * @param sex Sex to change to
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Returns the gamertag
     * @return Gamertag of user
     */
    public String getGamertag() {
        return gamertag;
    }

    /**
     * Sets the gamertag of this user
     * @param gamertag Gamertag to change to
     */
    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    /**
     * Returns the console
     * @return Console of the user
     */
    public String getConsole() {
        return console;
    }

    /**
     * Sets the console of this user
     * @param console Console to change to
     */
    public void setConsole(String console) {
        this.console = console;
    }

    /**
     * Returns the email
     * @return Email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of this user
     * @param email Email to change to
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password
     * @return Password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this user
     * @param password Password to change to
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the usertype
     * @return Usertype of user
     */
    public int getUsertype() {
        return usertype;
    }

    /**
     * Sets the usertype of this user
     * @param usertype Usertype to set to
     */
    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    /**
     * Returns the ID of this user
     * @return ID of user
     */
    public long getId(){
        return id;
    }

    /**
     * Returns the skill for fortnite
     * @return Skill in fortnite
     */
    public int getFortnite() {
        return fortnite;
    }

    /**
     * Sets the skill for fortnite
     * @param fortnite Skill to set to
     */
    public void setFortnite(int fortnite) {
        this.fortnite = fortnite;
    }

    /**
     * Returns the skill for minecraft
     * @return Skill in minecraft
     */
    public int getMinecraft() {
        return minecraft;
    }

    /**
     * Sets the skill for minecraft
     * @param minecraft Skill to set to
     */
    public void setMinecraft(int minecraft) {
        this.minecraft = minecraft;
    }

    /**
     * Returns the skill for gta5
     * @return Skill in gta5
     */
    public int getGta5() {
        return gta5;
    }

    /**
     * Sets the skill for gta5
     * @param gta5 Skill to set to
     */
    public void setGta5(int gta5) {
        this.gta5 = gta5;
    }

    /**
     * Returns the skill for league of legends
     * @return Skill in league of legends
     */
    public int getLeagueoflegends() {
        return leagueoflegends;
    }

    /**
     * Sets skill for league of legends
     * @param leagueoflegends Skill to set to
     */
    public void setLeagueoflegends(int leagueoflegends) {
        this.leagueoflegends = leagueoflegends;
    }

    /**
     * Returns the skill for rainbow six siege
     * @return Skill in rainbow six siege
     */
    public int getRainbowsixsiege() {
        return rainbowsixsiege;
    }

    /**
     * Sets skill for rainbow six siege
     * @param rainbowsixsiege Skill to set to
     */
    public void setRainbowsixsiege(int rainbowsixsiege) {
        this.rainbowsixsiege = rainbowsixsiege;
    }

    /**
     * Returns the skill for overwatch
     * @return Skill in overwatch
     */
    public int getOverwatch() {
        return overwatch;
    }

    /**
     * Sets skill for overwatch
     * @param overwatch Skill to set to
     */
    public void setOverwatch(int overwatch) {
        this.overwatch = overwatch;
    }

    /**
     * Returns the skill for pubg
     * @return Skill in pubg
     */
    public int getPubg() {
        return pubg;
    }

    /**
     * Sets skill for pubg
     * @param pubg Skill to set to
     */
    public void setPubg(int pubg) {
        this.pubg = pubg;
    }

    /**
     * Returns the skill for rocket league
     * @return Skill in rocket league
     */
    public int getRocketleague() {
        return rocketleague;
    }

    /**
     * Sets skill for rocket league
     * @param rocketleague Skill to set to
     */
    public void setRocketleague(int rocketleague) {
        this.rocketleague = rocketleague;
    }

    /**
     * Returns the reported status
     * @return True if reported and false if not
     */
    public boolean isReported() {
        return reported;
    }

    /**
     * Sets the reported status
     * @param reported Status to set to
     */
    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public int getSearchPref(){
        return searchPref;
    }

    public void setSearchPref(int searchPref){
        this.searchPref = searchPref;
    }

    public String getGamepref() {
        return gamepref;
    }

    public void setGamepref(String gamepref) {
        this.gamepref = gamepref;
    }

    public String getDefaultGamepref(){
        if(this.fortnite>0)
            return "fortnite";
        if(this.minecraft>0)
            return "minecraft";
        if(this.gta5>0)
            return "gta5";
        if(this.leagueoflegends>0)
            return "leagueoflegends";
        if(this.rainbowsixsiege>0)
            return "rainbowsixsiege";
        if(this.overwatch>0)
            return "overwatch";
        if(this.pubg>0)
            return "pubg";
        if(this.rocketleague>0)
            return "rocketleague";
        return null;
    }

    public String getGameSkill(String game) {
        if (game.equals("fortnite")) {
            if (getFortnite() == 0)
                return "Doesn't play";
            else if (getFortnite() == 1)
                return "Beginner";
            else if (getFortnite() == 2)
                return "Intermediate";
            else if (getFortnite() == 3)
                return "Expert";
        } else if (game.equals("minecraft")) {
            if (getMinecraft() == 0)
                return "Doesn't play";
            else if (getMinecraft() == 1)
                return "Beginner";
            else if (getMinecraft() == 2)
                return "Intermediate";
            else if (getMinecraft() == 3)
                return "Expert";
        } else if (game.equals("gta5")) {
            if (getGta5() == 0)
                return "Doesn't play";
            else if (getGta5() == 1)
                return "Beginner";
            else if (getGta5() == 2)
                return "Intermediate";
            else if (getGta5() == 3)
                return "Expert";
        } else if (game.equals("leagueoflegends")) {
            if (getLeagueoflegends() == 0)
                return "Doesn't play";
            else if (getLeagueoflegends() == 1)
                return "Beginner";
            else if (getLeagueoflegends() == 2)
                return "Intermediate";
            else if (getLeagueoflegends() == 3)
                return "Expert";
        } else if (game.equals("rainbowsixsiege")) {
            if (getRainbowsixsiege() == 0)
                return "Doesn't play";
            else if (getRainbowsixsiege() == 1)
                return "Beginner";
            else if (getRainbowsixsiege() == 2)
                return "Intermediate";
            else if (getRainbowsixsiege() == 3)
                return "Expert";
        } else if (game.equals("overwatch")) {
            if (getOverwatch() == 0)
                return "Doesn't play";
            else if (getOverwatch() == 1)
                return "Beginner";
            else if (getOverwatch() == 2)
                return "Intermediate";
            else if (getOverwatch() == 3)
                return "Expert";
        } else if (game.equals("pubg")) {
            if (getPubg() == 0)
                return "Doesn't play";
            else if (getPubg() == 1)
                return "Beginner";
            else if (getPubg() == 2)
                return "Intermediate";
            else if (getPubg() == 3)
                return "Expert";
        } else if (game.equals("rocketleague")) {
            if (getRocketleague() == 0)
                return "Doesn't play";
            else if (getRocketleague() == 1)
                return "Beginner";
            else if (getRocketleague() == 2)
                return "Intermediate";
            else if (getRocketleague() == 3)
                return "Expert";
        }

        return "Invalid request";

    }

    /**
     * Parses user as a String
     * @return User as a String
     */
    public String toString(){
        return  "Name: " + getName() + "\n" +
                "Password: " + getPassword() + "\n" +
                "ID: " + getId() + "\n" +
                "User Type: " + getUsertype() + "\n" +
                "Gamertag: " + getGamertag() + "\n" +
                "Sex: " + getSex() + "\n" +
                "Date of Birth: " +  getDateofbirth() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Console: " + getConsole() + "\n" +
                "Fortnite Skill: " + getFortnite() + "\n" +
                "Minecraft Skill: " + getMinecraft() + "\n" +
                "Gta V Skill: " + getGta5() + "\n" +
                "League of Legends Skill: " + getLeagueoflegends() + "\n" +
                "Rainbow Six Seige Skill: " + getRainbowsixsiege() + "\n" +
                "Overwatch Skill: " + getOverwatch() + "\n" +
                "Player Unknown Battlegrounds Skill: " + getPubg() + "\n" +
                "Rocket League Skill: " + getRocketleague() + "\n" +
                "Search Preference: " + getSearchPref() +
                "Game Preference: " + getGamepref();
    }

    /**
     * Parses a user as a JSONString
     * @return a String in JSON format
     */
    public String toJsonString(){
        return "{\"name\" : \"" + getName() +
                "\",\"gamertag\" : \"" + getGamertag() +
                "\",\"id\" : \"" + getId() +
                "\", \"dateofbirth\" : \"" + getDateofbirth() +
                "\",\"password\" : \"" + getPassword() +
                "\",\"sex\" : \"" + getSex() +
                "\", \"email\" : \"" + getEmail() +
                "\", \"usertype\" : \"" + getUsertype() +
                "\",\"console\" : \"" + getConsole() +
                "\", \"fortnite\" : \"" + getFortnite() +
                "\", \"minecraft\" : \"" + getMinecraft() +
                "\", \"gta5\" : \"" + getGta5() +
                "\", \"leagueoflegends\" : \"" + getLeagueoflegends() +
                "\", \"rainbowsixsiege\" : \"" + getRainbowsixsiege() +
                "\", \"overwatch\" : \"" + getOverwatch() +
                "\", \"pubg\" : \"" + getPubg() +
                "\", \"rocketleague\" : \"" + getRocketleague() +
                "\", \"searchpref\" : \"" + getSearchPref() +
                "\", \"gamepref\" : \"" + getGamepref() +"\"}";
    }

    /**
     * Parses a user as a JSONObject
     * @return a user in JSONObject format
     */
    public JSONObject toJSON(){
        JSONObject json = null;
        try{
            json = new JSONObject(this.toJsonString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
