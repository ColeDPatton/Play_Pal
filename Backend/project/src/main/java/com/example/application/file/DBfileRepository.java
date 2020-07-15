package com.example.application.file;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.application.user.User;
/*
 * This class creates a repository for teh Database files using Spirng Crud. 
 */
public interface DBfileRepository extends CrudRepository<DBfile, String> {

	@Query("select f.fileName from DBfile f where f.currentUser.id=:id")
	public List<String> getListOfFileNames(@Param("id") long id);

	@Query("select f.fileName from DBfile f where f.currentUser.id=:id and f.game=:game")
	public String getFileName(@Param("id") long id, @Param("game") String game);

	@Transactional
	@Modifying
	@Query("delete from DBfile f where f.currentUser=:u")
	public void clearImagesFromUser(@Param("u") User u);
	
//	@Query("select f.fileName from DBfile f where f.fileName=:name")
//	public String getFileName(String name);
	
}