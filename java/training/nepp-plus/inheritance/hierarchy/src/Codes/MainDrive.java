package Codes;

public class MainDrive {

	public static void main(String[] args) {

		Dog dog1 = new Dog();

		dog1.keepHome();
		dog1.playWithHuman();
		dog1.eat();

//		toString 메소드 연습
		System.out.println(dog1.toString());

		Cat cat1 = new Cat();
		cat1.name = "마루";

		System.out.println(cat1);

		Cat cat2 = new Cat();
		cat2.name = "마루";

//		equals 메소드 연습
		System.out.println(cat1.equals(cat2));

		String str1 = new String("안녕");
		String str2 = new String("안녕");

		System.out.println(str1);
		System.out.println(str2);

		System.out.println(str1 == str2);

	}

}