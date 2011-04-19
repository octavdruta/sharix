package src.WebServiceClient;

import java.util.Vector;

import src.Mediator.SharixMediator;
import src.SharixInterface.User;

public class SharixWebServiceClientMock extends SharixWebServiceClient {
	
	public SharixWebServiceClientMock(SharixMediator mediator) {
		super(mediator);
	}

	@Override
	public boolean connectToServer(String address, Integer port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector<User> getUserList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isServerConnected() {
		// TODO Auto-generated method stub
		return false;
	}

}
