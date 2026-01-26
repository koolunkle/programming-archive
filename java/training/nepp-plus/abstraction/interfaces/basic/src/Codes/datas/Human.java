package Codes.datas;

import Codes.Interfaces.Student;
import Codes.Interfaces.Worker;

public class Human extends Animal implements Student, Worker {

	@Override
	public void earnMoney() {
		System.out.println("사람이 근로자로서 돈을 법니다.");

	}

	@Override
	public void doStudy() {
		System.out.println("사람이 학생으로서 공부를 합니다.");

	}

	@Override
	public void goToSchool() {
		System.out.println("사람이 학생으로서 학교에 갑니다.");

	}

}
