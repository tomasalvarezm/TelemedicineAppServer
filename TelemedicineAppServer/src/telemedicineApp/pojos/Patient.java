package telemedicineApp.pojos;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


public class Patient implements Serializable{
	
	private static final long serialVersionUID = 8862781101827611209L;
	
	private String id;
	private String name;
	private String email;
	private LocalDate dob;
	private Integer age;
	private Sex sex;
	private Integer phoneNumber;
	private ArrayList <MedicalHistory> medhists;
	private ArrayList<BitalinoSignal> allsignals;
	private Doctor doctor;
	
	//CONSTRUCTORS
	
	public Patient() {
		super();
	}
	
	public Patient(String id, String name, String email, LocalDate dob, Integer age, Sex sex, Integer phoneNumber,
			ArrayList<MedicalHistory> medhists, ArrayList<BitalinoSignal> allsignals, Doctor doctor) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.dob = dob;
		this.age = age;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
		this.medhists = medhists;
		this.allsignals = allsignals;
		this.doctor = doctor;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public LocalDate getDob() {
		return dob;
	}


	public void setDob(LocalDate dob) {
		this.dob = dob;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Sex getSex() {
		return sex;
	}


	public void setSex(Sex sex) {
		this.sex = sex;
	}


	public Integer getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public ArrayList<MedicalHistory> getMedhists() {
		return medhists;
	}


	public void setMedhists(ArrayList<MedicalHistory> medhists) {
		this.medhists = medhists;
	}


	public ArrayList<BitalinoSignal> getAllsignals() {
		return allsignals;
	}


	public void setAllsignals(ArrayList<BitalinoSignal> allsignals) {
		this.allsignals = allsignals;
	}


	public Doctor getDoctor() {
		return doctor;
	}


	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}


}
