package account;

public class Account {
	private int clientNo;
	private String clientId;
	private String accNumber;
	private int money;
	
	public Account(int clientNo, String clientId, String accNumber) {
		this.clientNo = clientNo;
		this.clientId = clientId;
		this.accNumber = accNumber;
	}
	public Account(int clientNo, String clientId, String accNumber, int money) {
		this.clientNo = clientNo;
		this.clientId = clientId;
		this.accNumber = accNumber;
		this.money = money;
	}
	@Override
	public String toString() {
		return String.format("[%d] ID: %s, 계좌번호: %s, 잔액: %d원", clientNo, clientId, accNumber, money);
	}
	public int getClientNo() {
		return clientNo;
	}
	public void setClientNo(int clientNo) {
		this.clientNo = clientNo;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void addMoney(int money) {
		this.money += money;
	}
}