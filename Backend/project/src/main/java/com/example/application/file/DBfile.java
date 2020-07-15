package com.example.application.file;

import javax.persistence.*;
//import com.example.application.user.User;
import com.example.application.user.User;

/*
 * This class create a table "files' that stored current user, file name,file type, data, and game
 */
@Entity
@Table(name = "files")
@IdClass(FileCompositeId.class)
public class DBfile {

	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private User currentUser;

	private String fileName;
	private String fileType;

	@Lob
	private byte[] data;
	@Id
	@Column(length = 100)
	private String game;

	public DBfile() {

	}

	/*
	 * sets filename, filetype, data, current user, and game
	 */
	public DBfile(String fileName, String fileType, byte[] data, User currentUser, String game) {
		this.setFileName(fileName);
		this.setFileType(fileType);
		this.data = data;
		this.currentUser = currentUser;
		this.setGame(game);
	}

	/*
	 * returns user
	 */
	public User getUser() {
		return currentUser;
	}

	/*
	 * sets user
	 */
	public void setUser(User user) {
		this.currentUser = user;
	}

	/*
	 * returns fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/*
	 * sets Filename
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 *returns fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/*
	 * setsFileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/*
	 * return game
	 */
	public String getGame() {
		return game;
	}

	/*
	 * set game
	 */
	public void setGame(String game) {
		this.game = game;
	}
}
