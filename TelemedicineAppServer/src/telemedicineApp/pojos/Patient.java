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
	private ArrayList<Symptom> symptoms;
	private Medication medication;
	private Doctor doctor;
	
	
	//CONSTRUCTORS
	
	public Patient() {
		super();
	}
	
	
	public Patient(String id, String name, String email, Integer age, Sex sex, Integer phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
	}
	
	
	public Patient(String id, String name, String email, Integer age, Sex sex, Integer phoneNumber,
			ArrayList<Symptom> symptoms, Medication medication) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
		this.symptoms = symptoms;
		this.medication = medication;
	}
	
	
	public Patient(String id, String name, String email, Integer age, Sex sex, Integer phoneNumber,
			ArrayList<Symptom> symptoms, Medication medication, Doctor doctor) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.phoneNumber = phoneNumber;
		this.symptoms = symptoms;
		this.medication = medication;
		this.doctor=doctor;
	}



	//GETTERS AND SETTERS
	
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


	public ArrayList<Symptom> getSymptoms() {
		return symptoms;
	}


	public void setSymptoms(ArrayList<Symptom> symptoms) {
		this.symptoms = symptoms;
	}


	public Medication getMedication() {
		return medication;
	}


	public void setMedication(Medication medication) {
		this.medication = medication;
	}



	public Doctor getDoctor() {
		return doctor;
	}


	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}


	@Override
	public String toString() {
		return "Patient [id=" + id + ", name=" + name + ", email=" + email + ", dob=" + dob + ", age=" + age + ", sex="
				+ sex + ", phoneNumber=" + phoneNumber + ", symptoms=" + symptoms + ", medication=" + medication
				+ ", doctor=" + doctor + "]";
	}
	
	

}
