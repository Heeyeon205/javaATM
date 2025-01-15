package controller;

import account.AccountDAO;
import client.ClientDAO;
import utils.Utils;

public class Controller {
	private AccountDAO accDAO;
	private ClientDAO cliDAO;
	private Utils utils;

	private void init() {
		accDAO = new AccountDAO();
		cliDAO = new ClientDAO();
		utils = Utils.getInstance();
	}

	private void mainMenu() {
		while (true) {
			System.out.println("[메인 메뉴]");
			int sel = utils.getValue("[1] 관리자 [2] 사용자 [0] 종료", 0, 3);
			if (sel == 1) {
				managerMenu();
			} else if (sel == 2) {
				userMenu();
			} else if (sel == 0) {
				System.out.println("[ATM 프로그램 종료]");
				break;
			}
		}
	}

	private void managerMenu() {
		while (true) {
			System.out.println("[관리자 메뉴]");
			int sel = utils.getValue("[1] 회원 목록 [2] 회원 수정 [3] 회원 삭제 [4] 데이터 저장 [5] 데이터 불러오기 [0] 뒤로가기", 0, 6);
			if (sel == 1) {
				cliDAO.printClientALL();
			} else if (sel == 2) {
				cliDAO.updateUser();
			} else if (sel == 3) {
				cliDAO.deleteUserForManager(accDAO);
			} else if (sel == 4) {
				utils.saveClientData(cliDAO, "client.txt");
				utils.saveAccountData(accDAO, "Account.txt");
			} else if (sel == 5) {
				utils.loadClientData(cliDAO, "client.txt");
				utils.loadAccountData(accDAO, "Account.txt");
			} else if (sel == 0) {
				return;
			}
		}
	}

	private void userMenu() {
		while (true) {
			System.out.println("[사용자 메뉴]");
			int sel = utils.getValue("[1] 회원 가입 [2] 로그인 [0] 뒤로가기", 0, 3);
			if (sel == 1) {
				cliDAO.userJoin();
			} else if (sel == 2) {
				if (!cliDAO.userLogIn()) {
					return;
				} else {
					userLoginMenu();
				}
			} else if (sel == 0) {
				return;
			}
		}
	}

	private void userLoginMenu() {
		while (true) {
			System.out.println("[메뉴]");
			int sel = utils.getValue("[1] 계좌 추가 [2] 계좌 삭제 [3] 입금 [4] 출금 [5] 이체 [6] 탈퇴 [7] 마이 페이지 [0] 로그아웃", 0, 8);
			if (sel == 1) {
				accDAO.createAcc(cliDAO);
			} else if (sel == 2) {
				accDAO.deleteAcc(cliDAO);
			} else if (sel == 3) {
				accDAO.depositAcc(cliDAO);
			} else if (sel == 4) {
				accDAO.withdrawAcc(cliDAO);
			} else if (sel == 5) {
				accDAO.transferAcc(cliDAO);
			} else if (sel == 6) {
				cliDAO.deleteUser(accDAO);
				if (cliDAO.getLogIdx() == -1) {
					System.out.println("사용자 메뉴로 돌아갑니다.");
					return;
				}
			} else if (sel == 7) {
				cliDAO.printMyPage(accDAO);
			} else if (sel == 0) {
				System.out.println("[로그아웃]");
				return;
			}
		}
	}

	public void run() {
		init();
		mainMenu();
	}
}
