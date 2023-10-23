import java.util.Random;
import java.util.Scanner;

public class NumberGame {

	public static void main(String[] args) {
		generateRandom();

	}

	private static void generateRandom() {
		Random rand=new Random();
		int randomNum= rand.nextInt(100);
		guess(randomNum);
	}

	private static void guess(int randomNum) {
		Scanner sc=new Scanner(System.in);
		System.out.println("Number Guessing Game");
		System.out.println("Guess a number between 0-100:");
		int guess=sc.nextInt();
		System.out.println("");
		//input validation
		while(guess<0||guess>100) {
			System.out.println("Guess a number between 0-100");
			guess=sc.nextInt();
			System.out.println("");
		}
		
		//try again until you guess the coreect number
		int  tries=0;
		while(guess!=randomNum) {
			tries++;
			System.out.println("Wrong Guess!");
			System.out.println("Guess Again:");
			guess=sc.nextInt();
			System.out.println("");
		}
		
		//input validation
		while(guess<0||guess>100) {
			System.out.println("Guess a number between 0-100");
			guess=sc.nextInt();
			System.out.println("");
		}
		
		//Game Won
		System.out.println("Correct Answer.You Won");
		System.out.println("Wrong Tries: "+tries);
		System.out.println(" ");
		
		//play again or exit?
		System.out.println("Press 0 to exit.");
		int choice=sc.nextInt();
		if(choice==1) {
			generateRandom();
		}else
			return;
			
	}
	
}
