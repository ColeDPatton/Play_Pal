package com.example.application.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
/*
 * This class is using Spring Crud Respository to query through Users by their email, password, matched with 
 * and next swipe
 */
public interface UserRepository extends CrudRepository<User,Long> {
	
	@Query("select u from User u where u.email=:email")
	public List<User> getUserByEmail(@Param("email") String email);
	
	@Query("select u from User u where u.email=:email and u.password=:password")
	public List<User> loginUser(@Param("email") String email, @Param("password") String password);

//	@Query(	"select u from User u "
//			+ "where u IN "
//				+ "(select s.swipedOn from Swipe s and p.swipingUser from Swipe p "
//				+ "where s.swipingUser.id=:id "
//				+ "and p.swipedOn.id=:id "
//				+ "and p.swipingUser.id=s.swipedOn.id)")
	@Query(	"select u from User u "
			+ "where u IN "
				+ "(select s.swipedOn from Swipe s "
				+ "inner join Swipe as p on p.swipingUser.id=s.swipedOn.id "
				+ "where s.swipingUser.id=:id "
				+ "and p.swipedOn.id=:id)")
	public List<User> getUsersMatchedWithUser(@Param("id") long id);
	
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "order by RAND()")
	public List<User> nextUser(@Param("id") long id);
	
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.console=:console "
			+ "order by RAND()")
	public List<User> searchByConsole(@Param("console") String console, @Param("id") long id);

	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.rocketleague > 0 "
			+ "order by RAND()")
	public List<User> searchByGamerocketleague(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.fortnite > 0 "
			+ "order by RAND()")
	public List<User> searchByGamefortnite(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGamegta5(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGameleagueoflegends(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGameminecraft(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGameoverwatch(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGamepubg(@Param("id") long id);
	@Query("select u from User u "
			+ "where u NOT IN "
			+ "(select s.swipedOn from Swipe s where s.swipingUser.id=:id) "
			+ "and u.id!=:id "
			+ "and u.id>0 "
			+ "and u.gta5 > 0 "
			+ "order by RAND()")
	public List<User> searchByGamerainbowsixsiege(@Param("id") long id);
	
}
