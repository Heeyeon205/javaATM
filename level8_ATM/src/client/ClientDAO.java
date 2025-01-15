package client;

import java.util.ArrayList;

import account.AccountDAO;
import utils.Utils;

public class ClientDAO {
	private ArrayList<Client> cliList;
	private int number;
	private int logIdx;
	private Utils utils;

	public ClientDAO() {
		cliList = new ArrayList<>();
		utils = Utils.getInstance();
		number = 1001;
	}
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getLogIdx() {
		return logIdx;
	}

	public void setLogNum(int logIdx) {
		this.logIdx = logIdx;
	}

	public ArrayList<Client> getCliList() {
		return cliList;
	}

	public void setCliList(ArrayList<Client> cliList) {
		this.cliList = cliList;
	}

	public void userJoin() {
		System.out.println("[회원가입]");
		String id = utils.getString("id: ");
		if (isDupId(id))
			return;
		String pw = utils.getString("pw: ");
		String name = utils.getString("name: ");
		cliList.add(new Client(number, id, pw, name));
		System.out.printf("%d/%s/%s/%s 회원가입 완료\n", number, id, pw, name);
		number++;
	}

	private boolean isDupId(String id) {
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(id)) {
				System.out.println("중복 id는 사용할 수 없습니다.");
				return true;
			}
		}
		return false;
	}

	public boolean userLogIn() {
		System.out.println("[로그인]");
		String id = utils.getString("id: ");
		if (!isValidId(id))
			return false;
		;
		String pw = utils.getString("pw: ");
		if (!isValidLogin(id, pw)) {
			return false;
		} else {
			int idx = getIdx(id);
			logIdx = idx;
			System.out.println(id + "님 로그인 완료!");
			return true;
		}
	}

	private int getIdx(String id) {
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isValidId(String id) {
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(id)) {
				return true;
			}
		}
		System.out.println("유효하지 않은 id입니다.");
		return false;
	}

	private boolean isValidLogin(String id, String pw) {
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(id) && cliList.get(i).getPw().equals(pw)) {
				return true;
			}
		}
		System.out.println("id 또는 pw 를 다시 입력해주세요");
		return false;
	}

	public int getLogNum() {
		int logNum = cliList.get(logIdx).getClientNo();
		return logNum;
	}

	public String getLogId() {
		String logId = cliList.get(logIdx).getId();
		return logId;
	}

	public void deleteUser(AccountDAO accDAO) {
		System.out.println("[회원 탈퇴]");
		String deletePw = utils.getString("pw입력: ");
		if (!checkDeletePw(deletePw))
			return;
		int logNo = getLogNum();
		accDAO.deleteAllAcc(logNo);
		cliList.remove(logIdx);
		logIdx = -1;
		System.out.println("[회원 탈퇴 완료]");
	}

	private boolean checkDeletePw(String deletePw) {
		if (cliList.get(logIdx).getPw().equals(deletePw)) {
			return true;
		}
		System.out.println("비밀번호가 일치하지 않습니다.");
		return false;
	}

	public void printMyPage(AccountDAO accDAO) {
		System.out.println("[마이 페이지]");
		System.out.println(cliList.get(logIdx));
		accDAO.printMyAcc(cliList.get(logIdx).getClientNo());
	}

	public void printClientALL() {
		System.out.println("[회원 목록]");
		boolean hasData = false;
		for (Client c : cliList) {
			System.out.println(c);
			hasData = true;
		}
		if (!hasData) {
			System.out.println("[No Client Data]");
		}
	}

	public void updateUser() {
		if (!hasClientData())
			return;
		System.out.println("[회원 수정]");
		printClientALL();
		String updateId = utils.getString("수정할 id: ");
		if (!isValidId(updateId))
			return;
		String updatePw = utils.getString("수정할 pw: ");
		if (checkSamePw(updateId))
			return;
		String updateName = utils.getString("수정할 name: ");
		if (checkSameName(updateName))
			return;
		cliList.add(new Client(updateId, updatePw, updateName));
		System.out.printf("[회원 수정 완료]\n%s/%s/%s\n", updateId, updatePw, updateName);
	}

	private boolean checkSameName(String updateName) {
		if (cliList.get(logIdx).getPw().equals(updateName)) {
			System.out.println("현재와 동일한 이름으로 변경할 수 없습니다.");
			return true;
		}
		return false;
	}

	private boolean checkSamePw(String updateId) {
		if (cliList.get(logIdx).getPw().equals(updateId)) {
			System.out.println("현재와 동일한 비밀번호로 변경할 수 없습니다.");
			return true;
		}
		return false;
	}

	public boolean hasClientData() {
		if (cliList.size() > 0) {
			return true;
		}
		System.out.println("데이터가 없습니다.");
		return false;
	}

	public void deleteUserForManager(AccountDAO accDAO) {
		if (!hasClientData())
			return;
		System.out.println("[회원 삭제]");
		printClientALL();
		String deleteId = utils.getString("삭제할 id: ");
		if (!isValidId(deleteId))
			return;
		int[] deleteList = getIdxWithNo(deleteId);
		accDAO.deleteAllAcc(deleteList[1]);
		cliList.remove(deleteList[0]);
		System.out.println("[회원 삭제 완료]");
	}

	private int[] getIdxWithNo(String deleteId) {
		int[] arr = new int[2];
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(deleteId)) {
				arr[0] = i;
				arr[1] = cliList.get(i).getClientNo();
				break;
			}
		}
		return arr;
	}
}
