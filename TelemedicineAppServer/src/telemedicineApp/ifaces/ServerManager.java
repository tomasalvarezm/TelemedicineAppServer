package telemedicineApp.ifaces;

import telemedicineApp.pojos.BitalinoSignal;

public interface ServerManager {

	public void saveSignal (BitalinoSignal bs);
	public BitalinoSignal exportSignal (String id);
}
