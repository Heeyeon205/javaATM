  package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import account.Account;
import account.AccountDAO;
import client.Client;
import client.ClientDAO;

public class Utils {
	private static Utils instance;
	private Scanner sc = new Scanner(System.in);
	private final String CUR_PATH = System.getProperty("user.dir") + "/src/" + Utils.class.getPackageName() + "/";
	
	private Utils() {}
	public static Utils getInstance() {
		if(instance == null) instance = new Utils();
		return instance;
	}
	
	public int getValue(String msg, int min, int max) {
		while(true) {
		System.out.println(msg);
		int value = 0;
		try {
			value = sc.nextInt();
			sc.nextLine();
			if(value < min || value >= max) {
				System.out.println("입력 범위 오류입니다.");
				continue;
			}
			return value;
		}catch(InputMismatchException e) {
			sc.nextLine();
			System.out.println("숫자만 입력 가능합니다.");
		}catch(Exception e) {
			System.out.println("오류 발생");
		}
		}
	}
	
	public String getString(String msg) {
		System.out.println(msg);
		String str = sc.nextLine();
		return str;
	}
	
	public String isValidAccNum() {
		System.out.println("[계좌번호 생성 양식] 숫자 4자리 - 숫자 4자리 - 숫자 4자리");
		String regex = "^\\d{4}-\\d{4}-\\d{4}$";
		while(true) {
			System.out.println("계좌번호 입력: ");
			String accNum = sc.nextLine();
			boolean matches = Pattern.matches(regex, accNum);
	        if (matches) {
	        		return accNum;
	        } else {
	            System.out.println("생성 양식을 확인해주세요.");
	            continue;
	        }
		}
	}
	
	public void saveClientData(ClientDAO cliDAO, String msg) {
		String filePath = CUR_PATH + msg;
		try (FileWriter fw = new FileWriter(filePath);){
			for(Client c : cliDAO.getCliList()) {
				fw.write(c.getClientNo() + "/" + c.getId() + "/" + c.getPw() + "/" + c.getName() + "\n");
			}
			System.out.println("Client Data 저장 완료!\n경로: " + filePath);
		}catch(FileNotFoundException e) {
			System.out.println("경로를 찾을 수 없습니다.\n경로: " + filePath);
		}catch (IOException e) {
			System.out.println("오류 발생");
		}
	}
	
	public void saveAccountData(AccountDAO accDAO, String msg) {
		String filePath = CUR_PATH + msg;
		try (FileWriter fw = new FileWriter(filePath);){
			for(Account a : accDAO.getAccList()) {
				fw.write(a.getClientNo() + "/" + a.getClientId() + "/" + a.getAccNumber() + "/" + a.getMoney() + "\n");
			}
			System.out.println("Account Data 저장 완료!\n경로: " + filePath);
		}catch(FileNotFoundException e) {
			System.out.println("경로를 찾을 수 없습니다.\n경로: " + filePath);
		}catch (IOException e) {
			System.out.println("오류 발생");
		}
	}
	public void loadClientData(ClientDAO cliDAO, String msg) {
		String filePath = CUR_PATH + msg;
		try (FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr);){
			cliDAO.getCliList().clear();
			String line;
			while((line = br.readLine()) != null) {
				String[] temp = line.split("/");
				cliDAO.getCliList().add(new Client(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3]));
			}
			System.out.println("Client Data 불러오기 완료\n경로: " + filePath);
		} catch (FileNotFoundException e) {
			System.out.println("저장된 파일이 존재하지 않습니다.\n경로: " + filePath);
		} catch (IOException e1) {
			System.out.println("오류 발생");
		}
	}
	public void loadAccountData(AccountDAO accDAO, String msg) {
		String filePath = CUR_PATH + msg;
		try (FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr);){
			accDAO.getAccList().clear();
			String line;
			while((line = br.readLine()) != null) {
				String[] temp = line.split("/");
				accDAO.getAccList().add(new Account(Integer.parseInt(temp[0]), temp[1], temp[2], Integer.parseInt(temp[3])));
			}
			System.out.println("Account Data 불러오기 완료\n경로: " + filePath);
		} catch (FileNotFoundException e) {
			System.out.println("저장된 파일이 존재하지 않습니다.\n경로: " + filePath);
		} catch (IOException e1) {
			System.out.println("오류 발생");
		}
	}
}
