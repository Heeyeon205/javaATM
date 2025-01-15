package account;

import java.util.ArrayList;

import client.ClientDAO;
import utils.Utils;

public class AccountDAO {
	private ArrayList<Account> accList;
	private Utils utils;

	public AccountDAO() {
		accList = new ArrayList<>();
		utils = Utils.getInstance();
	}

	public ArrayList<Account> getAccList() {
		return accList;
	}

	public void setAccList(ArrayList<Account> accList) {
		this.accList = accList;
	}

	public void createAcc(ClientDAO cliDAO) {
		if (!checkAccCnt(cliDAO.getLogNum()))
			return;
		System.out.println("[계좌 추가]");
		String accNum = utils.isValidAccNum();
		if (isDupAccNum(accNum))
			return;
		int cliNo = cliDAO.getLogNum();
		String cliId = cliDAO.getLogId();
		accList.add(new Account(cliNo, cliId, accNum));
		System.out.printf("[계좌 추가 완료]\n(%d) ID: %s, 계좌번호: %s\n", cliNo, cliId, accNum);
	}

	private boolean checkAccCnt(int clientNo) {
		int cnt = 0;
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() == clientNo) {
				cnt++;
				if (cnt == 3) {
					System.out.println("계좌는 최대 3개까지 보유할 수 있습니다.");
					return false;
				}
			}
		}
		return true;
	}

	private boolean isDupAccNum(String accNum) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getAccNumber().equals(accNum)) {
				System.out.println("이미 존재하는 계좌번호입니다.");
				return true;
			}
		}
		return false;
	}

	public void deleteAcc(ClientDAO cliDAO) {
		if (!hasAcc(cliDAO.getLogNum()))
			return;
		System.out.println("[계좌 삭제]");
		printMyAcc(cliDAO.getLogNum());
		String delAccNum = utils.getString("삭제할 계좌 번호를 입력해주세요: ");
		int delIdx = getMyAccIdx(cliDAO.getLogNum(), delAccNum);
		if (delIdx == -1)
			return;
		accList.remove(delIdx);
		System.out.println(delAccNum + " [계좌 삭제 완료!]");
	}

	private int getMyAccIdx(int logNum, String delAccNum) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() == logNum) {
				if (accList.get(i).getAccNumber().equals(delAccNum)) {
					return i;
				}
			}
		}
		System.out.println("입력하신 계좌번호를 확인해주세요");
		return -1;
	}

	private boolean hasAcc(int clientNo) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() == clientNo) {
				return true;
			}
		}
		System.out.println("고객님 명의의 계좌가 없습니다.");
		return false;
	}

	public void printMyAcc(int clientNo) {
		boolean hasData = false;
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() == clientNo) {
				System.out.println(accList.get(i).toString());
				hasData = true;
			}
		}
		if (!hasData) {
			System.out.println("[No Account Data]");
		}
	}

	public void depositAcc(ClientDAO cliDAO) {
		if (!hasAcc(cliDAO.getLogNum()))
			return;
		System.out.println("[계좌 입금]");
		printMyAcc(cliDAO.getLogNum());
		String depositAccNum = utils.getString("입금할 계좌 번호를 입력해주세요: ");
		int depositIdx = getMyAccIdx(cliDAO.getLogNum(), depositAccNum);
		if (depositIdx == -1)
			return;
		int depositMoney = utils.getValue("입금할 금액: ", 100, 1000000);
		accList.get(depositIdx).addMoney(depositMoney);
		System.out.println(accList.get(cliDAO.getLogIdx()).toString());
	}

	public void withdrawAcc(ClientDAO cliDAO) {
		if (!hasAcc(cliDAO.getLogNum()))
			return;
		System.out.println("[계좌 출금]");
		printMyAcc(cliDAO.getLogNum());
		String withdrawAccNum = utils.getString("출금할 계좌 번호를 입력해주세요: ");
		int withdrawIdx = getMyAccIdx(cliDAO.getLogNum(), withdrawAccNum);
		if (withdrawIdx == -1)
			return;
		int max = getMyMaxMoney(withdrawIdx);
		if (max == -1)
			return;
		int withdrawMoney = utils.getValue("출금할 금액: ", 100, max);
		accList.get(withdrawIdx).setMoney(max - withdrawMoney);
		System.out.println(accList.get(withdrawIdx).toString());
	}

	private int getMyMaxMoney(int withdrawIdx) {
		int max = accList.get(withdrawIdx).getMoney();
		if (max < 100) {
			System.out.println("잔액이 부족합니다.");
			return -1;
		}
		return max;
	}

	public void transferAcc(ClientDAO cliDAO) {
		if (!hasAcc(cliDAO.getLogNum()))
			return;
		if (!hasOtherAcc(cliDAO.getLogNum()))
			return;
		System.out.println("[계좌 이체]");
		printMyAcc(cliDAO.getLogNum());
		String transAccNum = utils.getString("이체할 내 계좌 번호를 입력해주세요: ");
		int transMyIdx = getMyAccIdx(cliDAO.getLogNum(), transAccNum);
		if (transMyIdx == -1)
			return;
		printOtherAcc(cliDAO.getLogNum());
		String transferNum = utils.getString("이체할 상대 계좌 번호를 입력해주세요: ");
		int transferIdx = getOtherAccIdx(cliDAO.getLogNum(), transferNum);
		if (transferIdx == -1)
			return;
		int max = getMyMaxMoney(transMyIdx);
		if (max == -1)
			return;
		int transMyMoney = utils.getValue("이체할 금액: ", 100, max);
		accList.get(transMyIdx).setMoney(max - transMyMoney);
		accList.get(transferIdx).addMoney(transMyMoney);
		System.out.println("[이체 성공]");
		System.out.println("내 계좌: " + accList.get(transMyIdx).toString());
		System.out.println("상대 계좌: " + accList.get(transferIdx).toString());
	}

	private int getOtherAccIdx(int logNum, String transAccNum) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() != logNum) {
				if (accList.get(i).getAccNumber().equals(transAccNum)) {
					return i;
				}
			}
		}
		System.out.println("입력하신 계좌번호를 확인해주세요");
		return -1;
	}

	private boolean hasOtherAcc(int clientNo) {
		int cnt = 0;
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() != clientNo) {
				cnt++;
			}
		}
		if (cnt > 0) {
			return true;
		} else {
			System.out.println("타인 명의의 계좌가 없습니다.");
			return false;
		}
	}

	private void printOtherAcc(int clientNo) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() != clientNo) {
				System.out.println(accList.get(i).toString());
			}
		}
	}

	public void deleteAllAcc(int logNo) {
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientNo() == logNo) {
				accList.remove(i);
			}
		}
	}
}
