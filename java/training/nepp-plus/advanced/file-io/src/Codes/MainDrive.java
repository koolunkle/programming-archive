package Codes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainDrive {

	public static void main(String[] args) {

//		writeToFile();

//		readFile();

		Scanner myScanner = new Scanner(System.in);

		System.out.print("파일에 저장할 문구 : ");
		String inputStr = myScanner.nextLine();

		writeToFile(inputStr);

	}

	static void writeToFile(String content) {

//		연습문제.  입력받은 내용을 파일에 추가로 저장. (밑줄로)

		File myFile = new File("mytest.txt");

		try {
			FileWriter fw = new FileWriter(myFile, true);

			BufferedWriter bw = new BufferedWriter(fw);

			bw.append(content);
			bw.newLine();

			bw.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void readFile() {

//		파일 LOAD (Input) 실습

//		1. 어디서 파일을 불러올건지? 경로 설정.
		File myFile = new File("mytest.txt");

//		2. 파일 불러내기 클래스 활용 -> 파일이 지워져있으면? 예외 대응 try / catch 필요.

		try {
			FileReader fr = new FileReader(myFile);

//			여러 글자를 뭉쳐서 String으로 읽어오게 하는 클래스. => 한번에 한 줄씩 불러내자.
			BufferedReader br = new BufferedReader(fr);

//			몇줄이나 읽어와야할지 알 수 없다. (횟수 불명확 반복)
			while (true) {

//				readLine은 IOException 추가 대비 필요.
				String line = br.readLine();

				if (line == null) {

//					더이상 읽을 내용이 없었다.
//					반복 탈출.
					System.out.println("끝까지 읽음");
					break;

				}

				System.out.println(line);

			}

//			사용이 끝났으면 br / fr 닫아주자.
			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void writeToFile() {

//		파일 SAVE (Output) 실습

//		1. 어디에 파일을 저장할지? 경로 설정.

		File myFile = new File("mytest.txt");

//		2. 파일 작성 클래스 활용. => 위험소지 : try / catch로 엮어서 사용.

		try {

//			한 글자를 2byte 다룬다. -> 한번에 한글자씩만 처리함.
			FileWriter fw = new FileWriter(myFile, true);

//			"안녕하세요" 문장을 통째로 다루게 도와주는 클래스 사용.

			BufferedWriter bw = new BufferedWriter(fw);

//			실제 문장 기록 : bw를 이용하자.
			bw.append("수고하셨습니다.");

//			다음줄로 내리자.
			bw.newLine();

//			파일은 다 쓰면 닫아주자. (다른곳에서도 사용 가능하게)

			bw.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
