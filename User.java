///////////////////////////////////////////////////////////////////////////////
// Main Class File Title:            User
// Files:            User.java
// Semester:         CS367 Spring 2015
//
// Author:           Deyi Shi
// Email:            dshi4@wisc.edu
// CS Login:         deyi
// Lecturer's Name:  Jim Skrentny
// Lab Section:      NA
//
//////////////////////////////////////////////////////////////////////////

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
/**
 * The user class represents the data associated with a single user.
 * The class will store the name of the suer as a String, the karma as an
 * object of type karma, a List<String> denoting the subreddits
 * the user is subscribed to and different List<String>s which store
 * information regarding which posts were posted, liked or disliked by the user.
 * 
 * 
 * @author Deyi Shi
 * 
 */
public class User {
	final private String name;
	final private Karma karma;
	private List<String> subscribed;
	private List<Post> posted;
	private List<Post> liked;
	private List<Post> disliked;

	/**
	 *Creates an instance of the User class with the specified name 
	 *and a newly initialized instance of Karma. 
	 *The Lists will be initialized to new ArrayLists of the relevant types as applicable.
	 * @param name 
	 * @param karma 
	 * @param posted (list of users posts)
	 * @param subscribed (list of subreddits that user subscribed)
	 * @param liked (list of posts that user liked)
	 * @param disliked (list of posts that user disliked)
	 */
	public User (String name) {
		this.name = name;
		karma = new Karma();
		subscribed = new ArrayList<String>();
		posted = new ArrayList<Post>(); 
		liked = new ArrayList<Post>();
		disliked = new ArrayList<Post>();
	}

	
	public String getName() {
		return name;
	}

	public Karma getKarma() {
		return karma;
	}

	public List<String> getSubscribed() {
		List<String> a = subscribed;
		return a;
	}

	public List<Post> getPosted() {
		List<Post> a = posted;
		return a;
	}

	public List<Post> getLiked() {
		List<Post> a = liked;
		return a;
	}

	public List<Post> getDisliked() {
		List<Post> a = disliked;
		return a;
	}
	
	/**
	 * Add the specified subreddit to the List of subscribed subreddits 
	 * if the user is not already subscribed. 
	 * If the user is already subscribed, unsubscribe from the subreddit.
	 */
	public void subscribe(String subreddit) {
		if (subreddit == null){
			throw new IllegalArgumentException();
		}
		else if(subscribed.isEmpty()){
			subscribed.add(subreddit);
		}
		else{
			
			ListIterator<String> itr = subscribed.listIterator();
			boolean flag = true;
			while (itr.hasNext()){
				if (subreddit.equals(itr.next())){
					flag = false;
				}
			}
			if(flag){
				subscribed.add(subreddit);
			}
			else{
				unsubscribe(subreddit);
			}
		}
	}

	/**
	 *Remove the specified subreddit from the List of subscribed subreddits if present; 
	 *if not, do nothing.
	 */
	public void unsubscribe(String subreddit) {
		if (subreddit == null){
			throw new IllegalArgumentException();
		}
		else if(subscribed.isEmpty()){
			throw new RuntimeException("there is nothing to unscribe");
		}
		else if(subscribed.contains(subreddit)){
			subscribed.remove(subreddit);
		}
		else{
			throw new RuntimeException("there is no such subreddit to unscribe");
		}
		
	}
	
	/**
	 * Instantiate a new post with the appropriate parameters 
	 * and add it to the list of posts posted by the user.
	 */
	public Post addPost(String subreddit, PostType type, String title) {
		if(subreddit == null || title == null || type == null){
			throw new IllegalArgumentException();
		}
		else{
			Post post = new Post(this,subreddit,type,title);
			this.like(post);
			posted.add(post);
			return post;
		}
	}

	/**
	 * Upvote the post and add it to the List of liked posts if not already liked; else undo the like.
	 * If the post is currently disliked by the user, the dislike should be undone
	 */
	public void like(Post post) {
		if(post == null){
			throw new IllegalArgumentException();
		}
		else{
			if(liked.contains(post)){
				undoLike(post);
			}
			else if(disliked.contains(post)){
				undoDislike(post);
				post.upvote();
				liked.add(post);
			}
			else{
				post.upvote();
				liked.add(post);
			}
		}
	}
	
	/**
	 *Remove the post from the list of liked posts and update its karma appropriately.
	 */
	public void undoLike(Post post) {
		post.downvote();
		post.downvote();
		liked.remove(post);
	}
	
	/**
	 * Downvote the post and add it to the List of disliked posts if not already disliked;
	 *   else undo the dislike.
	 *   If the post is currently liked by the user, the like should be undone.
	 */
	public void dislike(Post post) {
		if(post == null){
			throw new IllegalArgumentException();
		}
		else{
			if(liked.contains(post)){
				undoLike(post);
				dislike(post);
			}
			else if(disliked.contains(post)){
				undoDislike(post);
			}
			else{
				post.downvote();
				disliked.add(post);
			}
		}
	}
	
	/**
	 * 	 Remove the post from the list of disliked posts and update its karma appropriately.
	 */
	public void undoDislike(Post post) {
		post.upvote();
		post.downvote();
		disliked.remove(post);
	}
}
