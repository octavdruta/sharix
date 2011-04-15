package src.Mediator;

import src.GUI.*;
import java.util.*;
import javax.swing.*;

public class SharixMediatorMock {
    private SharixGUI gui;

    public SharixMediatorMock() {
        gui = new SharixGUI("eu");
    }

    public void gogogo() {
        gui.buildGUI();
    }

    public void simulate() {
        try {
            (new ListWorker()).doInBackground();
        } catch(Exception e) {}
    }

    public static void main(String[] args) {
        final SharixMediatorMock mock = new SharixMediatorMock();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mock.gogogo();
            }
        });
        mock.simulate();
    }

    private class ListWorker extends SwingWorker<Void, Integer> {
        public ListWorker() { }

        @Override
        protected Void doInBackground() throws Exception {
            publish(1);
            boolean newUser = false;
            boolean removeUser = false;
            Random rnd = new Random();
            for (int i = 0; i < 60 * 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) { }
                gui.randomUpdateTransfers();
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
            return null;
        }

        @Override
        protected void process(List<Integer> list) {
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
        }

        @Override
        public void done() {
        }
    }
}
