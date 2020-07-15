package com.example.application.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * This method is simply creating our interface using Spring CrudRespository
 *
 */
public interface SwipeRepository extends CrudRepository<Swipe,Long> {

	@Transactional
	@Modifying
	@Query("delete from Swipe s where s.swipedOn=:u or s.swipingUser=:u")
	public void clearSwipesFromUser(@Param("u") User u);

}
