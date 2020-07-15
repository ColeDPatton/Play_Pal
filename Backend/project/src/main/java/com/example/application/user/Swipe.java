package com.example.application.user;

import javax.persistence.*;

/**
 * This class handles the swiping part of the application including the users
 * that the current user has swiped on and those who have swiped on the user
 * 
 *
 */
@Entity
@Table(name = "swipes")
@IdClass(SwipeCompositeId.class)
public class Swipe {

	/*
	 * User that is conducting the swiping
	 */
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "swiper")
	private User swipingUser;

	/*
	 * User that is being swiped on
	 */
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "swiped_on")
	private User swipedOn;

	/*
	 * 0 for not liked, 1 for liked
	 */
	@Column(name = "liked")
	private int liked; // 0 for not liked, 1 for liked

	/*
	 * initialzes swiper and swipe on users
	 */
	public Swipe(User a, User b, int liked) {
		swipingUser = a;
		swipedOn = b;
		this.liked = liked;
	}

	public Swipe() {
	}

	/*
	 * return swipingUser
	 */
	public User getSwipingUser() {
		return swipingUser;
	}

	/*
	 * set swiping User
	 */
	public void setSwipingUser(User swipingUser) {
		this.swipingUser = swipingUser;
	}

	/*
	 * return user swiped on
	 */
	public User getSwipedOn() {
		return swipedOn;
	}

	/*
	 * set user swiped on
	 */
	public void setSwipedOnUser(User user) {
		this.swipedOn = user;
	}

	/*
	 * return 0 or 1
	 */
	public int getLiked() {
		return liked;
	}

	/*
	 * set to either 0 or 1
	 */
	public void setLiked(int liked) {
		this.liked = liked;
	}

}