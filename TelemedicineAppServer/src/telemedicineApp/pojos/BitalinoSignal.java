package telemedicineApp.pojos;

import java.time.LocalDate;


public class BitalinoSignal {
	private Integer id; //AUTOINCREMENT
	private String patient_id; //TEXT
	private String signal_duration; //TEXT
	private LocalDate dateSignal; //TEXT
	private String filePath; //TEXT
	
	public BitalinoSignal(Integer id, String patient_id, String signal_duration, LocalDate date, String filePath) {
		super();
		this.id=id;
		this.patient_id = patient_id;
		this.signal_duration = signal_duration;
		this.dateSignal = date;
		this.filePath = filePath;
	}
	
	public BitalinoSignal() {
		super();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getDateSignal() {
		return dateSignal;
	}
	public void setDateSignal(LocalDate dateSignal) {
		this.dateSignal = dateSignal;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public String getSignal_duration() {
		return signal_duration;
	}
	public void setSignal_duration(String signal_duration) {
		this.signal_duration = signal_duration;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	
	
}
