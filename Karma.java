///////////////////////////////////////////////////////////////////////////////
// Main Class File Title:            Karma
// Files:            Karma.java
// Semester:         CS367 Spring 2015
//
// Author:           Deyi Shi
// Email:            dshi4@wisc.edu
// CS Login:         deyi
// Lecturer's Name:  Jim Skrentny
// Lab Section:      NA
//
//////////////////////////////////////////////////////////////////////////

/**
 * The Karma class represents the points accrued by a single user. Karma
 * is of two subtypes, linkedKarma and commentKarma, relating to Link
 * and Comment posttypes respectively; self posts do not affect the 
 * karma of the creating user.
 * @author Deyi Shi
 * 
 */

public class Karma {
	private int linkKarma;
	private int commentKarma;
	
	/**
	 *Creates an instance of the Karma class 
	 *with link and comment karmas initialized to zero.
	 * @param commentKarma
	 * @param linkKarma
	 */
	public Karma() {
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	/**
	 *Increases the karma of this post and 
	 *the relevant karma of the user who created the post by two each.
	 * @param commentKarma
	 * @param linkKarma
	 */
	public void upvote(PostType type) {
		if(type.equals(PostType.COMMENT)){
			commentKarma+=2;
		}
		if(type.equals(PostType.LINK)){
			linkKarma+=2;
		}
	}

	/**
	 *Decrease the karma of this post and 
	 *the relevant karma of the user who created the post by one each.
	 * @param commentKarma
	 * @param linkKarma
	 */
	public void downvote(PostType type) {
		if(type.equals(PostType.COMMENT)){
			commentKarma--;
		}
		if(type.equals(PostType.LINK)){
			linkKarma--;
		}
	}

	/**
	 * return linkkarma
	 * @param linkKarma
	 */
	public int getLinkKarma() {
		return this.linkKarma;
	}

	/**
	 * return commentkarma
	 * @param commentkarma
	 */
	public int getCommentKarma() {
		return this.commentKarma;
	}
}
