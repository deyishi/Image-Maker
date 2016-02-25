///////////////////////////////////////////////////////////////////////////////
// Main Class File Title:            RedditDB
// Files:            RedditDB.java
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
 * The redditDB class stores a List of all the user information.
 * 
 * @author Deyi Shi
 * 
 */
public class RedditDB {
	private List<User> users;

	/**
	 * Constructs an empty database.
	 */
	public RedditDB() {
		this.users = new ArrayList<User>();
	}

	/**
	 * Returns a copy of the list of users; the list itself should not be exposed.
	 */
	public List<User> getUsers() {
		List<User> a = users;
		return a;
	}

	/**
	 * If a user with the given name does not already exist, 
	 * add a user with the given name to the database and return it; 
	 * else simply return null.

	 * @param name
	 * @return
	 */
	public User addUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		ListIterator<User> itr = users.listIterator();
		while (itr.hasNext()){
			if (name.equals( itr.next().getName())){
				return null;
			}
		}
		User a = new User(name);
		users.add(a);
		return a;
	}
	/**
	 * Search the database for a user with the given name and
	 *  return the information if found; else return null.
	 * @param name
	 * @return
	 */
	public User findUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		ListIterator<User> itr = users.listIterator();
		while (itr.hasNext()){
			if (name.equals( itr.next().getName())){
				return itr.previous();
			}
		}
		return null;
	}

	/**
	 * Search the database for a user with the given name 
	 * and delete the information and return true if found; 
	 * else return false. Deletion also involves undoing all information related to the user:
	 *  (i) For each posted post, remove it from the liked and disliked information of all users. 
	 *  (ii) Undo all likes. (iii) Undo all dislikes.
	 * @param name
	 * @return
	 */
	public boolean delUser(String name) {
		if (name == null){
			throw new IllegalArgumentException();
		}
		boolean flag = false;
		ListIterator<User> itr = users.listIterator();
		while (itr.hasNext()){
			if (name.equals( itr.next().getName())){
				flag = true;
			}
		}
		if(flag){
			User a = findUser(name);
			List<String> subList = a.getSubscribed();
			List<Post> likeList = a.getLiked();
			List<Post> disList = a.getDisliked();
			for(int i = 0; i<likeList.size();i++){
				a.undoLike(likeList.get(i));
			}			
			for(int i = 0; i<disList.size();i++){
				a.undoLike(disList.get(i));
			}
			for(int i = 0; i<subList.size();i++){
				a.unsubscribe(subList.get(i));
			}
			users.remove(a);
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Get all the posts to be displayed on the front page of a particular user and return them. 
	 * If the user is null this is simply all posts from all users. 
	 * If a user is specified, return all the posts from the user's subscribed subreddits only; 
	 * posts which have previously been liked or disliked by the user should not be returned,
	 * except when the post was created by the user themselves.
	 * @param user
	 * @return
	 */
	public List<Post> getFrontpage(User user) {
		List<Post> allPost = new ArrayList<Post>();
		List<Post> userPost = new ArrayList<Post>();
		for(int i = 0; i < users.size();i++){
			allPost.addAll(users.get(i).getPosted());
		}
		if(user == null){
			return allPost;
		}
		else{
			List<String> sublist = user.getSubscribed();
			ArrayList<String> likeAndDis = new ArrayList<String>();
			for(int k = 0; k< user.getLiked().size();k++){
				if(!user.getLiked().get(k).getUser().getName().equals(user.getName())){
					likeAndDis.add(user.getLiked().get(k).getSubreddit());
				}
			}
			for(int k = 0; k< user.getDisliked().size();k++){
				if(!user.getDisliked().get(k).getUser().getName().equals(user.getName())){
					likeAndDis.add(user.getDisliked().get(k).getSubreddit());			
				}
			}
			for(int k = 0; k< sublist.size();k++){
				for(int i = 0; i < likeAndDis.size(); i++ ){
					if(sublist.get(k).equals(likeAndDis.get(i))){
						sublist.remove(k);
					}
				}
			}
			for(int i = 0; i < allPost.size();i++){
				for(int k = 0; k< sublist.size();k++){
					if(allPost.get(i).getSubreddit().equals(sublist.get(k))){
						userPost.add(allPost.get(i));
					}
				}
			}


			return userPost;
		}
	}

	/**
	 * Get all the posts from the specified subreddit 
	 * to be displayed on the front page of a particular user and return them. 
	 * If the user is null this is simply all relevant posts from all users.
	 * If a user is specified, return all the posts from 
	 * the subreddit which have not previously been liked or disliked by the user ,
	 * except when the post was created by the user themselves.
	 * @param user
	 * @return
	 */
	public List<Post> getFrontpage(User user, String subreddit) {
		List<Post> allPost = new ArrayList<Post>();
		List<Post> userPost = new ArrayList<Post>();
		for(int i = 0; i < users.size();i++){
			allPost.addAll(users.get(i).getPosted());
		}
		if(user == null){
			for(int i = 0; i < allPost.size();i++){
				if(allPost.get(i).getSubreddit().equals(subreddit)){
					userPost.add(allPost.get(i));
				}
			}
			return userPost;
		}
		else{
			for(int i =0; i < getFrontpage(user).size();i++){
				if(getFrontpage(user).get(i).getSubreddit().equals(subreddit)){
					userPost.add(getFrontpage(user).get(i));
				}
			}
			return userPost;
		}
	}
}
