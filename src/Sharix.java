package src;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import src.GUI.SharixGUI;
import src.Mediator.SharixMediator;
import src.Network.SharixNetwork;
import src.WebServiceClient.SharixWebServiceClientMock;

public class Sharix {
	String username;
	SharixGUI gui;
	SharixNetwork network;
	SharixWebServiceClientMock webserviceclient;
	SharixMediator mediator;
	private static final String DEFAULT_CONFIG = "config";

	public Sharix(String username, String configPath) {
		mediator = new SharixMediator(username);
		gui = new SharixGUI(mediator);
		webserviceclient = new SharixWebServiceClientMock(mediator, configPath);
		network = new SharixNetwork(mediator);
	}

	public void buildGUI() {
        gui.buildGUI();
    }

    public void simulate() {
        try {
            (new ListWorker()).doInBackground();
        } catch(Exception e) {}
    }
   
	public static void main(String[] args) {
		if (args.length != 2 && args.length != 1) {
			System.err.println("The program takes one parameter, the username, and another optional parameter: the path to the config dir.");
			System.exit(-1);
		}
		final Sharix app = new Sharix(args[0], args.length == 1 ? DEFAULT_CONFIG : args[1]);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.buildGUI();
            }
        });
        app.simulate();
	}

	private class ListWorker extends SwingWorker<Void, Integer> {
        public ListWorker() { }

        @Override
        protected Void doInBackground() throws Exception {
            /*publish(1);
            boolean newUser = false;
            boolean removeUser = false;
            Random rnd = new Random();
            for (int i = 0; i < 60 * 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) { }
                ((SharixGUI) gui).randomUpdateTransfers();
                int val = rnd.nextInt(9);
                if (val == 0 && !removeUser) {
                    publish(2);
                    removeUser = true;
                }
                if (val == 1 && !newUser) {
                    publish(3);
                    newUser = true;
                }
            }
            */
            return null;
        }

        @Override
        protected void process(List<Integer> list) {
        	/*
            Vector<String> filesAndrei = new Vector<String>();
            filesAndrei.add("A1.txt");
            filesAndrei.add("A2.txt");
            filesAndrei.add("A3.txt");

            Vector<String> filesIon = new Vector<String>();
            filesIon.add("I1.txt");
            filesIon.add("I2.txt");

            Vector<String> filesDan = new Vector<String>();
            filesDan.add("D1.txt");
            filesDan.add("D2.txt");
            filesDan.add("D3.txt");
            filesDan.add("D4.txt");

            Vector<String> filesVlad = new Vector<String>();
            filesVlad.add("V1.txt");
            filesVlad.add("V2.txt");
            filesVlad.add("V3.txt");

            for (Integer i : list) {
                switch(i.intValue()) {
                case 1:
                    gui.addUser("Andrei", filesAndrei);
                    gui.addUser("Dan", filesDan);
                    gui.addUser("Ion", filesIon);
                    break;
                case 2:
                    gui.removeUser("Andrei");
                    break;
                case 3:
                    gui.addUser("Vlad", filesVlad);
                    break;
                }
            }
            */
        }

        @Override
        public void done() {
        }
    }
}
