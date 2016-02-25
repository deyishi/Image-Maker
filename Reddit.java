///////////////////////////////////////////////////////////////////////////////
// Title:            Reddit
// Files:            Karama.java, Post.java, PostType.java, Reddit.java, RedditDB.java, User.Java
// Semester:         CS367 Spring 2015
//
// Author:           Deyi Shi
// Email:            dshi4@wisc.edu
// CS Login:         deyi
// Lecturer's Name:  Jim Skrentny
// Lab Section:      NA
//
//////////////////////////////////////////////////////////////////////////
import java.awt.SystemColor;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


/**
 * The application program, Reddit, 
 * creates and uses an RedditDB to represent and process information. 
 * The user and post information is read from a text files 
 * and then the program processes user commands. 
 * 
 * @author Deyi Shi
 * 
 */

public class Reddit {
	private static RedditDB redditDB = new RedditDB();
	private static User curr;

	/**
	 * Checks whether at least one command-line argument is given;
	 *  if not, displays "Usage: java Reddit <FileNames>" and quit.
	 * Checks whether the input files exists and is readable;
	 *  if not, displays: "File <FileName> not found." and quit.
	 *  Loads the data from the input files and uses it to construct the database.
	 *  Prompts the user to enter command options and processes them until the user exits. Prompts should be as [anon@reddit]$ if not user is logged in or [<userName>@reddit]$ else..
	 * @param args
	 */
	public static void main(String[] args){
		redditDB.addUser("admin");
		if (args.length < 1) {
			System.err.println("Usage: java Reddit FileName");
			System.exit(1);
		}
		ArrayList<File> file = new ArrayList<File>();
		for( int i = 0; i < args.length; i++){
			File newfile = new File(args[i]);
			if(newfile.exists()){
				file.add(newfile);
			}
			else{
				System.err.println("File "+ args[i] +" cannot be read.");
				System.exit(2);
			}
		}

		User s;
		ArrayList<String> input = new ArrayList<String>();
		for( int i = 0; i < file.size(); i++){ 
			s = redditDB.addUser(args[i].substring(0, args[i].length()-4));
			input = trans(file.get(i));
			String[] word;
			word = input.get(0).split(", ");
			for(int c = 0; c < word.length; c++){
				s.subscribe(word[c]);
			}
			for(int j = 1; j < input.size();j++){
				word = input.get(j).split(", ");
				String title = "";
				for(int e = 2; e < word.length; e++){
					title = title + word[e]+", "; 
				}
				title = title.substring(0, title.length()-2);
				s.addPost(word[0], PostType.valueOf(word[1]), title);
			}
		}

		boolean flag = true;
		curr = new User("anon");
		while(flag){
			menu();	
		}

	}

	/**
	 * a set of commands that user can user with reddit database
	 */

	private static void menu(){
		String userinput;
		Scanner inp = new Scanner(System.in);
		System.out.println("["+ curr.getName() +"@reddit]$ ");
		userinput = inp.nextLine();
		if(userinput.equals("s")&&curr.getName().equals("admin")){
			ListIterator<User> w = i();
			while(w.hasNext()){
				display(w.next());
			}
		}
		else if(userinput.charAt(0)=='1'){
			if(userinput.length() >1){
				String[] word = userinput.split(" ");
				ListIterator<User> u = i();
				boolean userfind = false;
				while(u.hasNext()){
					if(u.next().getName().equals(word[1])){
						userfind = true;
					}
				}
				if(userfind){
					login(word[1]);
				}
				else{
					System.out.println("User " + word[1] + " not found.");
				}
			}
			else{
				logout();
			}
		}
		else if(userinput.charAt(0)=='d'&& curr.getName().equals("admin")){
			if(userinput.length()>1){
				String[] word = userinput.split(" ");
				String del = redditDB.findUser(word[1]).getName();
				redditDB.delUser(del);
			}
			else{
				System.out.println("Invalid command!");
			}
		}
		else if(userinput.charAt(0)=='f'){
			displayfrontpage();
		}
		else if(userinput.charAt(0)=='x'){
			System.out.println("Exiting to the real world...");
			System.exit(0);
		}
		else if(userinput.charAt(0) == 'r'){
			String[] word = userinput.split(" ");
			displayr(word[1]);
		}
		else if(userinput.charAt(0) == 'u'){
			String[] word = userinput.split(" ");
			 userName(word[1]);
		}
		else{
			System.out.println("Invalid command!");
		}
	}

	/**
	 * Display "Displaying /u/<userName>..." 
	 * and prompt for sub-menu options (given in another table below)
	 *  for each post of the specified user one-by-one till the sub-menu exits.
	 *  Display "No posts left to display." when all posts have displayed and exit the sub-menu.
	 *  Display "Exiting to the main menu..." when leaving the sub-menu.
	 * @param u
	 */
	private static void userName(String u){
		ListIterator<User> a = i();
		User input = null;
		while(a.hasNext()){
			User c = a.next();
			if((c.getName().equals(u))){
				input = c;
			}
		}
		List<Post> ip = input.getPosted();
		ListIterator<Post> ic = ip.listIterator();
		String d;
		boolean checker = true;
		while(ic.hasNext()&&checker){
			Post p = ic.next();
			d = subMenu(p);
			if(d.equals("x")){
				checker = false;
			}
			if(!ic.hasNext()){
				System.out.println("No posts left to display.");
				System.out.println(	"Exiting to the main menu...");
			}
		}
	}



	/**
	 * logout a user 
	 */
	private static void logout(){
		if(curr.getName().equals("anon")){
			System.out.println("No user logged in.");
		}
		else{
			System.out.println("User " + curr.getName() +" logged out");
			curr = new User("anon");
		}
	}

	/**
	 * return frontpage of current user
	 */
	private static List<Post> frontPage(){
		List<Post> front;
		if(curr.getName().equals("anon")){
			front = redditDB.getFrontpage(null);
		}
		else{
			front = redditDB.getFrontpage(curr);
		}
		return front;
	}

	/**
	 * return frontpage of current user's specific subreddit
	 */
	private static List<Post> frontPager(String t){
		List<Post> front;

		if(curr.getName().equals("anon")){
			front = redditDB.getFrontpage(null, t);
		}
		else{
			front = redditDB.getFrontpage(curr, t);
		}

		return front;
	}


	/**
	 * Display "Displaying /r/<subredditName>..."
	 *  and prompt for sub-menu options (given in another table below) 
	 *  for each subreddit frontPage post one-by-one till the sub-menu exits.
	 *  Display "No posts left to display." when all posts have displayed and exit the sub-menu.
	 *  Display "Exiting to the main menu..." when leaving the sub-menu.
	 */
	private static void displayr(String s){
		ListIterator<Post> a = frontPager(s).listIterator();
		boolean checker = true;
		String d;
		if(!a.hasNext()){
			System.out.println("No posts left to display.");
			System.out.println(	"Exiting to the main menu...");
		}
		while(a.hasNext()&&checker){
			Post p = a.next();
			d = subMenu(p);
			if(d.equals("x")){
				checker = false;
			}
			if(!a.hasNext()){
				System.out.println("No posts left to display.");
				System.out.println(	"Exiting to the main menu...");
			}
		}
	}

	/**
	 *Display "Displaying the front page..." 
	 *and prompt for sub-menu options (given in another table below) 
	 *for each frontPage post one-by-one till the sub-menu exits.
	 *Posts are to be displayed as <postKarma><horizontalTab><Title>
	 *Display "No posts left to display." when all posts have displayed and exit the sub-menu.
	 *Display "Exiting to the main menu..." when leaving the sub-menu.
	 */
	private static void displayfrontpage(){
		ListIterator<Post> a = frontPage().listIterator();
		boolean checker = true;
		String d;
		while(a.hasNext()&&checker){
			Post p = a.next();
			d = subMenu(p);
			if(d.equals("x")){
				checker = false;
			}
			if(!a.hasNext()){
				System.out.println("No posts left to display.");
				System.out.println(	"Exiting to the main menu...");
			}
		}
	}
	
	/**
	 * a set of commands that user can use to modify a post
	 * @param p
	 * @return
	 */
	private static String subMenu(Post p){
		String output = "a";
		ListIterator<Post> c = frontPage().listIterator();
		boolean flag = true;
		String userinput;
		Scanner inp = new Scanner(System.in);
		while(output.equals("a")){
			System.out.println(p.getKarma()+"\t"+p.getTitle());
			System.out.println("["+ curr.getName() +"@reddit]$ ");
			userinput = inp.nextLine();
			if(userinput.equals("a")){
				if(curr.getName().equals("anon")){
					System.out.println("Login to like post.");
					output = "q";
				}
				else{
					curr.like(p);
					output = "q";
				}
			}
			else if(userinput.equals("z")){
				if(curr.getName().equals("anon")){
					System.out.println("Login to dislike post.");
					output = "q";
				}
				else{
					curr.dislike(p);
					output = "q";
				}
			}
			else if(userinput.equals("j")){
				output = "j";
			}
			else if(userinput.equals("x")){
				System.out.println("Exiting to the main menu...");
				output = "x";
			}
			else{
				System.out.println("Invalid command!");
			}
		}
		return output;
	}
	
	/**
	 * display user's karma 
	 * @param t
	 */
	private static void display(User t){
		System.out.println(t.getName()+
				"\t"+t.getKarma().getLinkKarma()
				+"\t"+t.getKarma().getCommentKarma());
	}
	/**
	 * login a user
	 * @param t
	 */
	private static void login(String t){
		curr = redditDB.findUser(t);
		System.out.println("User " + curr.getName()+ " logged in.");
	}

	/**
	 * create and return a listIterator of a list of users
	 * @return
	 */
	private static ListIterator<User> i(){
		List<User> u = redditDB.getUsers();
		ListIterator<User> w = u.listIterator();
		return w;
	}
	/**
	 * convert input file to a list of strings
	 * @param file
	 * @return
	 */
	private static ArrayList<String> trans(File file){
		ArrayList<String> output = new ArrayList<String>();
		Scanner fileIn = new Scanner(System.in);
		try {
			fileIn = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("cannot read file");
		}
		while(fileIn.hasNextLine()){
			output.add(fileIn.nextLine());
		}
		return output;
	} 
}






