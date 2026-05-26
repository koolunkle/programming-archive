package Codes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainDrive {

	public static void main(String[] args) {

//		Calendar 변수 만들기

		Calendar myBirthDay = Calendar.getInstance();

//		저장된 날짜 변경 (set)

//		연도를 1966로 변경
		myBirthDay.set(Calendar.YEAR, 1996);

		myBirthDay.set(Calendar.MONTH, Calendar.MAY);

//		년/월/일/시/분 한번에 대입.

		myBirthDay.set(1996, Calendar.MAY, 9, 8, 30);

//		출생 일자의 요일 가져오기

		System.out.println(myBirthDay.get(Calendar.DAY_OF_WEEK));

		Calendar yourBirthDay = Calendar.getInstance();

		yourBirthDay.set(1995, Calendar.DECEMBER, 27);

//		 내 생일 / 상대 생일 중 더 빠른 것은? 선후 관계 비교 -> 날짜를 long 숫자로 변환. 

		System.out.println(String.format("내 생일 : %,d", myBirthDay.getTimeInMillis()));
		System.out.println(String.format("상대 생일 : %,d", yourBirthDay.getTimeInMillis()));

		if (myBirthDay.getTimeInMillis() < yourBirthDay.getTimeInMillis()) {

//			내가 먼저 태어났다. (내 생일이 더 오래 된 일) : 숫자가 작을수록 오래됨. 

			System.out.println("내가 먼저 태어남");
		} else {
			System.out.println("상대가 먼저 태어남");
		}

//		본인의 생일을 -> 1996년 5월 9일 (목) 오전 8:30 양식으로 표현.

		SimpleDateFormat firstFormat = new SimpleDateFormat("yyyy년 M월 d일");

		String myBirthDayStr1 = firstFormat.format(myBirthDay.getTime());

		System.out.println(myBirthDayStr1);

//		1. 960509 양식으로 

		SimpleDateFormat format1 = new SimpleDateFormat("yyMMdd");
		String myBirthDay1 = format1.format(myBirthDay.getTime());
		System.out.println(myBirthDay1);

//		2. 1996-05-09 양식으로

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String myBirthDay2 = format2.format(myBirthDay.getTime());
		System.out.println(myBirthDay2);

//		3. 1996년 5월 9일 (목) 오전 8시 30분 

		SimpleDateFormat format3 = new SimpleDateFormat("yyyy년 M월 d일 (E) a h시 mm분");
		String myBirthDay3 = format3.format(myBirthDay.getTime());
		System.out.println(myBirthDay3);

	}

}
