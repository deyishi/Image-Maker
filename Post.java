
///////////////////////////////////////////////////////////////////////////////
// Main Class File Title:            Post
// Files:            Post.java
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
 * The post class represents a single post that keeps track
 * of the user who posted it, the subreddit it was posted to, the type
 * of the post, the title of the post and the karma received by it.
 * 
 * @author Deyi Shi
 * 
 */
public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;
	
	/**
	 * Constructs a post with the specified attributes as applicable. 
	 * A newly created post should be assigned a karma of zero.
	 * @param user
	 * @param subreddit
	 * @param type
	 * @param title
	 * @param karma
	 */
	public Post (User user, String subreddit, PostType type, String title) {
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		karma = 0;
	}
	
	/**
	 *Increases the karma of this post 
	 *and the relevant karma of the user who created the post by two each.
	 * 
	 */
	public void upvote() {
		karma+=2;
		user.getKarma().upvote(type);
	}

	/**
	 * Decreases the karma of this post and 
	 * the relevant karma of the user who created the post by one each.
	 */
	public void downvote() {
		karma-=1;
		user.getKarma().downvote(type);
	}

	/**
	 * Returns the user who created this post.
	 */
	public User getUser() {
		return user;
	}
	/**
	 * Returns the subreddit this was posted to.
	 */
	public String getSubreddit() {
		return subreddit;
	}

	/**
	 * Returns the type of the post.
	 */
	public PostType getType() {
		return type;
	}
	
	/**
	 * Returns the title of the post.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the karma aggregated by the post.
	 */

	public int getKarma() {
		return karma;
	}
}
